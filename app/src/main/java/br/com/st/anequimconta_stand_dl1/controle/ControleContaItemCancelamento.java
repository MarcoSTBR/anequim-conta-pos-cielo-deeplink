package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaItemCancelar;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnContaItemCancelamento;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.ContaCancelamentoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControleContaItemCancelamento {
    private Activity ctx ;
    private OnContaItemCancelamento onContaItemCancelamento ;
    private int usuario_aut ;
    private ContaView contaView ;

    public ControleContaItemCancelamento(Activity ctx, OnContaItemCancelamento onContaItemCancelamento) {
        this.ctx = ctx;
        this.onContaItemCancelamento = onContaItemCancelamento;
    }

    public void cancelarItem(ContaView contaView, ContaCancelamentoItemView contaCancelamentoItemView){
        this.contaView = contaView ;
        usuario_aut = UtilSet.getUsuarioId(ctx) ;
        new ControleLiberacaoAcesso(ctx, new ListenerControleLiberacaoAcesso() {
           @Override
           public void acessoOk(int usuario_id) {
               usuario_aut = usuario_id ;
               cancelamentoAutorizado(contaCancelamentoItemView) ;
           }

           @Override
           public void acessoErro(String msg) {
               onContaItemCancelamento.contaItemCancelamentoErro(msg);
           }
       }).acesso(18);
    }

    private void cancelamentoAutorizado(ContaCancelamentoItemView contaCancelamentoItemView){
        try {
            new ConexaoContaItemCancelar(ctx, getJSON(contaCancelamentoItemView)) {
                @Override
                public void contaItemCancelarOk(JSONArray jsonArray) {
                    onContaItemCancelamento.contaItemCancelamentoOk();
                }

                @Override
                public void contaItemCancelarErro(String msg) {
                    onContaItemCancelamento.contaItemCancelamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onContaItemCancelamento.contaItemCancelamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onContaItemCancelamento.contaItemCancelamentoErro(e.getMessage());
        }
    }

    private JSONArray getJSON(ContaCancelamentoItemView itv) throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray jaa = new JSONArray() ;
        JSONObject j = new JSONObject() ;
        Produto prd = ProdutoDao.getProdutoDao(ctx).getProduto(itv.getContaItemView().getContaItem().getPRODUTO_ID()) ;


        j.put("UUID", UtilSet.getUUID()) ;
        j.put("PEDIDO", contaView.getConta().getNUMERO()) ;
        j.put("DATASEQUENCIAL",  df.format(contaView.getConta().getDATA())) ;
        j.put("CONTA_PEDIDO_ID", itv.getContaItemView().getContaItem().getId()) ;
        j.put("CONTA_PEDIDO_ITEM_UUID", itv.getContaItemView().getContaItem().getUUID()) ;
        j.put("DATA", df.format(new Date())) ;
        j.put("USUARIO_ID", UtilSet.getUsuarioId(ctx)) ;
        j.put("USUARIO_AUT_ID", usuario_aut) ;
        j.put("TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
        j.put("PRODUTO_ID", itv.getContaItemView().getContaItem().getPRODUTO_ID()) ;
        j.put("QUANTIDADE", itv.getQuantidade());
        j.put("DESCRICAO", prd.getDescricao()) ;
        j.put("PRECO", itv.getContaItemView().getContaItem().getPRECO()) ;


        Double quantidade_fica = itv.getContaItemView().getContaItem().getQUANTIDADE() - itv.getQuantidade() ;
        if (quantidade_fica > 0) {
            JSONObject jit = new JSONObject();
            Double valor = quantidade_fica * (itv.getContaItemView().getContaItem().getPRECO());
/*
            BigDecimal cm = new BigDecimal(new DecimalFormat("#0.000").format(valor * (prd.getComissao() / 100)))
                    .setScale(2, RoundingMode.DOWN);
*/
            Log.i("comissao__", "valor " + valor);
            Double comissao = UtilSet.transformarDouble(valor * (prd.getComissao() / 100)) ;// cm.doubleValue();//(it.getPedidoItem().getVALOR() - it.getPedidoItem().getDESCONTO()) * (produto.getComissao() / 100 ) ;
            Log.i("comissao__", "cm  " + comissao);
            jit.put("QUANTIDADE", quantidade_fica);
            jit.put("VALOR", valor);
            jit.put("COMISSAO", comissao);
            jit.put("STATUS", 1);
            j.put("ITEM", jit) ;
        } else {
            JSONObject jit = new JSONObject();
            jit.put("QUANTIDADE", itv.getContaItemView().getContaItem().getQUANTIDADE());
            jit.put("VALOR", itv.getContaItemView().getContaItem().getVALOR());
            jit.put("COMISSAO", itv.getContaItemView().getContaItem().getCOMISSAO());
            jit.put("STATUS", 0);
            j.put("ITEM", jit) ;
        }
        jaa.put(j) ;
        Log.i("cancelamento_", jaa.toString()) ;
        return jaa ;
    }
}
