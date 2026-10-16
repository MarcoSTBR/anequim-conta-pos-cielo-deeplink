package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleVendaCodigo;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemDao;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class ControleProdutoVendaCodigo {

    private Activity act ;
    private PedidoView pedidoView ;
    private Produto produto ;
    private ListenerControleVendaCodigo listenerControleVendaCodigo ;

    public ControleProdutoVendaCodigo(Activity act, ListenerControleVendaCodigo listenerControleVendaCodigo) {
        this.act = act;
        this.listenerControleVendaCodigo = listenerControleVendaCodigo;
    }

    public void setCodigo(PedidoView pedidoView, String cod){
        this.pedidoView = pedidoView ;
        produto = ProdutoDao.getProdutoDao(act).geCodProdutoAtivo(cod) ;
        if (produto != null){
           if (ifUndKL(produto.getUnidade()))
               addKouL();
              else addProduto(1.0, produto.getPreco(), produto.getPreco()) ;
        } else addCodKil_lit(cod) ;
    }

    private void addKouL(){
        new ControleRecebeValor(act, produto.getDescricao() + "\nValor", "#0.000", 3) {
            @Override
            public void valorDigitado(Double v) {
                if (v > 0.0) {
                    BigDecimal prc = new BigDecimal(produto.getPreco()) ;
                    BigDecimal q = BigDecimal.valueOf(v);
                    BigDecimal vl = new BigDecimal(v * produto.getPreco()) ;
                    addProduto(q.doubleValue(), prc.doubleValue(), vl.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                   // addProduto(q.setScale(3, RoundingMode.HALF_EVEN).doubleValue(), produto.getPreco(), v);
                } else listenerControleVendaCodigo.vendaCodigoErro("Valor Zerado!");
            }

            @Override
            public void erro(String msg) {
                listenerControleVendaCodigo.vendaCodigoErro(msg);
            }
        };
    }

    private void addCodKil_lit(String codigo){
       try{
        if (codigo.length() == 13) {
            String c = codigo.substring(1,7) ;
            String vl = codigo.substring(7,12) ;
            Log.i("kilo_l", "p "+c);
            Log.i("kilo_l", "p "+vl);
            produto = ProdutoDao.getProdutoDao(act).geCodProdutoAtivo(c) ;
            if (produto != null){
                if (ifUndKL(produto.getUnidade())){
                    Double v = new BigDecimal(vl).doubleValue() / 100 ;
                    BigDecimal q = new BigDecimal(v/produto.getPreco()) ;
                    addProduto(q.setScale(3, RoundingMode.HALF_EVEN).doubleValue(), produto.getPreco(), v);
                } else {
                    Double v = new BigDecimal(vl).doubleValue() / 100 ;
                    addProduto(1.0, v, v);
                }
            } else throw new Exception("Código "+c+" não encontrado!") ;
        } else throw new Exception("Código ["+codigo+"] não encontrado!") ;
       } catch (Exception e){
           e.printStackTrace();
           listenerControleVendaCodigo.vendaCodigoErro(e.getMessage());
       }
    }

    private void addProduto(Double q, Double prc, Double v) {
     //   Double comissao = v * (produto.getComissao() / 100) ;
        Double desconto = 0.0 ;
        Double comissao = UtilSet.transformarDouble((v - desconto) * (produto.getComissao() / 100 )) ;
        PedidoItem it = new PedidoItem(UtilSet.getUUID(), pedidoView.getPedido().getUUID(),
                produto.getId(), q, prc, 0, comissao, v, 1, "", 0) ;
        try {
            PedidoItemDao.getPedidoItemDao(act).insert(it);
            listenerControleVendaCodigo.vendaCodigoOk();
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControleVendaCodigo.vendaCodigoErro(e.getMessage());
        }
    }

    private boolean ifUndKL(String und){
      return  (und.substring(0,1).equals("K") || und.substring(0,1).equals("L")) ;
    }
}
