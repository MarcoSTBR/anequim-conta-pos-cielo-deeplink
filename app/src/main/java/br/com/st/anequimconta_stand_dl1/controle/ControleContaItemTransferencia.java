package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaItemtransferencia;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnContaItemTransferencia;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemTransferenciaView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControleContaItemTransferencia {
    private Activity ctx ;
    private OnContaItemTransferencia onContaItemTransferencia;
    private int usuario_aut ;
    private String numero_origem ;
    private String numero_destino;

    public ControleContaItemTransferencia(Activity ctx, OnContaItemTransferencia onContaItemTransferencia) {
        this.ctx = ctx;
        this.onContaItemTransferencia = onContaItemTransferencia;
    }

    public void transferenciaItem(ContaItemTransferenciaView contaItemTransferenciaView, String numero_origem, String numero_destino){
        this.numero_origem = numero_origem;
        if ((numero_destino != null) && (!numero_destino.equals("0")) && (!numero_origem.equals(numero_destino)) ) {
            this.numero_destino = numero_destino;
            usuario_aut = UtilSet.getUsuarioId(ctx);
            new ControleLiberacaoAcesso(ctx, new ListenerControleLiberacaoAcesso() {
                @Override
                public void acessoOk(int usuario_id) {
                    usuario_aut = usuario_id;
                    transferenciaAutorizada(contaItemTransferenciaView);
                }

                @Override
                public void acessoErro(String msg) {
                    onContaItemTransferencia.contaItemTransferenciaErro(msg);
                }
            }).acesso(16);
        } else onContaItemTransferencia.contaItemTransferenciaErro("Número da transferência inválido!");
    }

    private void transferenciaAutorizada(ContaItemTransferenciaView contaItemTransferenciaView){
        try {
            new ConexaoContaItemtransferencia(ctx, getJSON(contaItemTransferenciaView)) {
                @Override
                public void contaItemtransferenciaOk(JSONArray jsonArray) {
                    onContaItemTransferencia.contaItemTransferenciaOk();
                }

                @Override
                public void contaItemtransferenciaErro(String msg) {
                    onContaItemTransferencia.contaItemTransferenciaErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onContaItemTransferencia.contaItemTransferenciaErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onContaItemTransferencia.contaItemTransferenciaErro(e.getMessage());
        }
    }

    private JSONArray getJSON(ContaItemTransferenciaView itv) throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray jaa = new JSONArray() ;
        JSONObject j = new JSONObject() ;
        j.put("UUID", UtilSet.getUUID()) ;
        j.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
        j.put("DATA", df.format(new Date())) ;
        j.put("USUARIO_ID", UtilSet.getUsuarioId(ctx)) ;
        j.put("USUARIO_AUT_ID", usuario_aut) ;
        j.put("TERMINAL_ID", UtilSet.getTerminalId(ctx)) ;
        j.put("QUANTIDADE", itv.getQuantidade());
        j.put("PRECO", itv.getContaItemView().getContaItem().getPRECO());
        Double quantidade_fica = itv.getContaItemView().getContaItem().getQUANTIDADE() - itv.getQuantidade() ;

        Produto prd = ProdutoDao.getProdutoDao(ctx).getProduto(itv.getContaItemView().getContaItem().getPRODUTO_ID()) ;
        if (quantidade_fica > 0) {
            JSONObject jcontOrigem = new JSONObject();
            Double valor = quantidade_fica * (itv.getContaItemView().getContaItem().getPRECO());
/*
            BigDecimal cm = new BigDecimal(new DecimalFormat("#0.000").format(valor * (prd.getComissao() / 100)))
                    .setScale(2, RoundingMode.DOWN);
*/
            Log.i("comissao__", "valor " + valor);
            Double comissao = UtilSet.transformarDouble(valor * (prd.getComissao() / 100))  ;//cm.doubleValue();//(it.getPedidoItem().getVALOR() - it.getPedidoItem().getDESCONTO()) * (produto.getComissao() / 100 ) ;
            Log.i("comissao__", "cm  " + comissao);

            jcontOrigem.put("NUMERO", numero_origem);
            jcontOrigem.put("CONTA_UUID", itv.getContaItemView().getContaItem().getCONTA_UUID());
            jcontOrigem.put("ITEM_UUID", itv.getContaItemView().getContaItem().getUUID());
            jcontOrigem.put("QUANTIDADE", quantidade_fica);
            jcontOrigem.put("VALOR", valor);
            jcontOrigem.put("COMISSAO", comissao);
            jcontOrigem.put("STATUS", 1);
            j.put("CONTA_ORIGEM", jcontOrigem);

            valor = itv.getQuantidade() * (itv.getContaItemView().getContaItem().getPRECO());
/*
            cm = new BigDecimal(new DecimalFormat("#0.000").format(valor * (prd.getComissao() / 100)))
                    .setScale(2, RoundingMode.DOWN);
*/
            comissao = UtilSet.transformarDouble(valor * (prd.getComissao() / 100)) ;//cm.doubleValue();//(it.getPedidoItem().getVALOR() - it.getPedidoItem().getDESCONTO()) * (produto.getComissao() / 100 ) ;


            JSONObject jcontDestino = new JSONObject();
            jcontDestino.put("NUMERO", numero_destino);
            jcontDestino.put("ITEM_UUID", UtilSet.getUUID());
            jcontDestino.put("QUANTIDADE", itv.getQuantidade());
            jcontDestino.put("VALOR", valor);
            jcontDestino.put("COMISSAO", comissao);
            jcontDestino.put("PRODUTO_ID", prd.getId());
            jcontDestino.put("DESCRICAO", prd.getDescricao());
            jcontDestino.put("STATUS", 1);
            j.put("CONTA_DESTINO", jcontDestino);
        } else {
            JSONObject jcontOrigem = new JSONObject();
            jcontOrigem.put("NUMERO", numero_origem);
            jcontOrigem.put("CONTA_UUID", itv.getContaItemView().getContaItem().getCONTA_UUID());
            jcontOrigem.put("ITEM_UUID", itv.getContaItemView().getContaItem().getUUID());
            jcontOrigem.put("QUANTIDADE", itv.getContaItemView().getContaItem().getQUANTIDADE());
            jcontOrigem.put("VALOR", itv.getContaItemView().getContaItem().getVALOR());
            jcontOrigem.put("COMISSAO", itv.getContaItemView().getContaItem().getCOMISSAO());
            jcontOrigem.put("STATUS", 0);
            j.put("CONTA_ORIGEM", jcontOrigem);

            JSONObject jcontDestino = new JSONObject();
            jcontDestino.put("NUMERO", numero_destino);
            jcontDestino.put("ITEM_UUID", UtilSet.getUUID());
            jcontDestino.put("QUANTIDADE", itv.getContaItemView().getContaItem().getQUANTIDADE());
            jcontDestino.put("VALOR", itv.getContaItemView().getContaItem().getVALOR());
            jcontDestino.put("COMISSAO", itv.getContaItemView().getContaItem().getCOMISSAO());
            jcontDestino.put("STATUS", 1);
            jcontDestino.put("PRODUTO_ID", prd.getId());
            jcontDestino.put("DESCRICAO", prd.getDescricao());


            j.put("CONTA_DESTINO", jcontDestino);
        }

        jaa.put(j) ;
        Log.i("trans_", jaa.toString()) ;
        return jaa ;
    }
}
