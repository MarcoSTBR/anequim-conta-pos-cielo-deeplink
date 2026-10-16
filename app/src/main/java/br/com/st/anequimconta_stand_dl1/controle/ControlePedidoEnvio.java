package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoEnviarPedidos;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.dao.PedidoDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemAddDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemDao;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAdd;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ControlePedidoEnvio {

    private Activity ctx ;
    private List<PedidoView> list ;
    private OnControleEnvioPedidos onControleEnvio ;
    private JSONArray req ;

    public ControlePedidoEnvio(Activity ctx, List<PedidoView> list, OnControleEnvioPedidos onControleEnvio) {
        this.ctx = ctx;
        this.list = list;
        this.onControleEnvio = onControleEnvio;
    }

    public void enviar() {
        try {
            onControleEnvio.envioChange("Iniciando Envio...");
            req = getJSON() ;
            enviarJson();
        } catch (JSONException e) {
            e.printStackTrace();
            onControleEnvio.envioErro(e.getMessage());
        }
    }

    private void enviarJson(){

        try {
            new ConexaoEnviarPedidos(ctx, req) {
                @Override
                public void envPedidosOk(JSONArray jsonArray) {
                    apagarPedidos() ;

                }

                @Override
                public void anvPedidosErro(String msg) {
                    onControleEnvio.envioErro(msg);

                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleEnvio.envioChange(e.getMessage());
            onControleEnvio.envioErro(e.getMessage());
        }

    }

    private void apagarPedidos(){
        PedidoDao.getPedidoDao(ctx).deleteAll();
        PedidoItemDao.getPedidoItemDao(ctx).deleteAll();
        PedidoItemAddDao.getPedidoItemAddDao(ctx).deleteAll();
        onControleEnvio.envioOk(req, list);
    }

    private JSONArray getJSON() throws JSONException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray jaa = new JSONArray() ;
        Produto produto = null ;
        onControleEnvio.envioChange("Construindo Envio...");
        for (PedidoView pw : list){
            JSONObject j = new JSONObject() ;
            j.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
            j.put("UUID", UtilSet.getUUID()) ;
            j.put("USUARIO_ID", UtilSet.getUsuarioId(ctx)) ;
            j.put("TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
            j.put("PEDIDO", pw.getPedido().getNUMERO()) ;
            j.put("REFERENCIA", pw.getPedido().getREFERENCIA()) ;
            j.put("DATA", dt.format(new Date())) ;
            JSONArray jItens = new JSONArray();
            for (PedidoItemView it : pw.getPedidoItemView()){
                produto = ProdutoDao.getProdutoDao(ctx).getProduto(it.getPedidoItem().getPRODUTO_ID()) ;
                Log.i("comissao__", "valor "+(it.getPedidoItem().getVALOR() - it.getPedidoItem().getDESCONTO()) * (produto.getComissao() / 100 )) ;
                Double comissao =  UtilSet.transformarDouble((it.getPedidoItem().getVALOR() - it.getPedidoItem().getDESCONTO()) * (produto.getComissao() / 100 )) ;//(it.getPedidoItem().getVALOR() - it.getPedidoItem().getDESCONTO()) * (produto.getComissao() / 100 ) ;
                Log.i("comissao__", "cm  "+comissao) ;
                JSONObject jit = new JSONObject();
                jit.put("UUID",it.getPedidoItem().getUUID()) ;
                jit.put("UUID_ACOMP",it.getPedidoItem().getUUID()) ;
                jit.put("PRODUTO_ID",it.getPedidoItem().getPRODUTO_ID()) ;
                jit.put("QUANTIDADE",it.getPedidoItem().getQUANTIDADE()) ;
                jit.put("PRECO",it.getPedidoItem().getPRECO()) ;
                jit.put("VALOR",it.getPedidoItem().getVALOR()) ;
                jit.put("COMISSAO",comissao) ;
                jit.put("DESCONTO",it.getPedidoItem().getDESCONTO()) ;
                jit.put("OBS",it.getPedidoItem().getOBS()) ;
                jItens.put(jit) ;
                for (PedidoItemAdd itemAdd : it.getPedidoItemAdd()){
                    produto = ProdutoDao.getProdutoDao(ctx).getProduto(itemAdd.getPRODUTO_ID()) ;
                    Double quantidade = it.getPedidoItem().getQUANTIDADE() * itemAdd.getQUANTIDADE() ;
                    Double valor      = quantidade * itemAdd.getPRECO() ;
                    Double desconto   = quantidade * itemAdd.getDESCONTO() ;
                    comissao          = UtilSet.transformarDouble((valor - desconto) * (produto.getComissao() / 100 )) ;//cm.doubleValue() ;// valor - desconto * (produto.getComissao() / 100 ) ;
                    JSONObject jitAdd = new JSONObject();
                    jitAdd.put("UUID",itemAdd.getUUID()) ;
                    jitAdd.put("UUID_ACOMP",it.getPedidoItem().getUUID()) ;
                    jitAdd.put("PRODUTO_ID",itemAdd.getPRODUTO_ID()) ;
                    jitAdd.put("QUANTIDADE",quantidade) ;
                    jitAdd.put("PRECO",itemAdd.getPRECO()) ;
                    jitAdd.put("VALOR",valor) ;
                    jitAdd.put("COMISSAO",comissao) ;
                    jitAdd.put("DESCONTO",desconto) ;
                    jitAdd.put("OBS",itemAdd.getOBS()) ;
                    jItens.put(jitAdd) ;
                }
            }
            j.put("ITENS",jItens) ;
            jaa.put(j) ;
        }
        return jaa ;
    }

}
