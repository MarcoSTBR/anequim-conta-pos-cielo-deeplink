package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.activity.ActivityEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleExportTicket;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControlePedidoTicketView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleTicket;
import br.com.st.anequimconta_stand_dl1.dao.PedidoTicketDao;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoTicketDao;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTicketView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.entidade.ProdutoTicket;
import br.com.st.anequimconta_stand_dl1.impressao.ImpressaoBitmapBuilder;
import br.com.st.anequimconta_stand_dl1.impressao.ImpressaoPedidoTicketBitMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ControleTicket implements OnControlePedidoTicketView, ListenerImpressaoBipMap {

    private Activity ctx ;
    private OnControleTicket onControleTicket ;
    private OnImpressaoBitMap onImpressaoBitMap ;

    public ControleTicket(ActivityEnvioPedidos ctx, OnControleTicket onControleTicket) {
        this.ctx = ctx;
        this.onControleTicket = onControleTicket;
        onImpressaoBitMap = new ImpressaoBitmapBuilder(ctx, this).getImpressao(ctx) ;
    }

    public void enviarRequisicao(JSONArray req){
        onControleTicket.impTicketChange("Gravando Tickets...", null);
        try {
            //PedidoTicketDao.getPedidoTicketDao(ctx).deleteAll();
            Log.i("conttickt", req.toString()) ;
           for (int i = 0 ; i < req.length() ; i++) {
              JSONObject j  = req.getJSONObject(i) ;
               Log.i("conttickt", "PEDIDO i-" + i +j.toString()) ;
              for (int ii = 0 ; ii < j.getJSONArray("ITENS").length() ; ii++){
                 JSONObject jj =  j.getJSONArray("ITENS").getJSONObject(ii) ;
                 Log.i("conttickt", "ITEM ii-"+ii+ jj.toString()) ;
                 ProdutoTicket pt = ProdutoTicketDao.getProdutoTicket(ctx).getProdutoTiket(jj.getInt("PRODUTO_ID"));
                 if (pt != null) {
                    Produto prd = ProdutoDao.getProdutoDao(ctx).getProduto(jj.getInt("PRODUTO_ID")) ;
                    Log.i("conttickt", "pt "+ pt.geJSON().toString()) ;
                    if ((prd.getUnidade().substring(0,1).toUpperCase() == "K") || (prd.getUnidade().substring(0,1).toUpperCase() == "L")) {
                        PedidoTiket pedidoTiket = new PedidoTiket(0, UtilSet.getUUID(),
                                UtilSet.getData(j.getString("DATA")), jj.getString("UUID"),
                                0, j.getInt("USUARIO_ID"), UtilSet.getData(j.getString("DATA")),
                                j.getInt("USUARIO_ID"), UtilSet.getTerminalId(ctx));
                        Log.i("conttickt", "pedidoTiket " + pedidoTiket.getJSON().toString());
                        PedidoTicketDao.getPedidoTicketDao(ctx).insert(pedidoTiket.getJSONInsert());
                    } else {
                        int q = (int) jj.getDouble("QUANTIDADE") ;
                        for (int x=1 ; x <= q ; x++){
                            PedidoTiket pedidoTiket = new PedidoTiket(0, UtilSet.getUUID(),
                                    UtilSet.getData(j.getString("DATA")), jj.getString("UUID"),
                                    0, j.getInt("USUARIO_ID"), UtilSet.getData(j.getString("DATA")),
                                    j.getInt("USUARIO_ID"), UtilSet.getTerminalId(ctx));
                            Log.i("conttickt", "pedidoTiket " + pedidoTiket.getJSON().toString());
                            PedidoTicketDao.getPedidoTicketDao(ctx).insert(pedidoTiket.getJSONInsert());
                        }
                    }
                 } //else Log.i("conttickt", "NÃO ENCONTRADO PRODUTO_ID "+jj.getInt("PRODUTO_ID") ) ;
              }
           }
           setTicketServidor() ;
        } catch (JSONException e) {
           e.printStackTrace();
          onControleTicket.impTicketErro(e.getMessage());
        }
    }

    private void setTicketServidor(){
        new ControleExportTicket(ctx, new ListenerControleExportTicket() {
          @Override
          public void ControleExportTicketOk(List<PedidoTiket> listUp) {
            setTicketsView(listUp);
          }

          @Override
          public void ControleExportTicketErro(String msg) {
              onControleTicket.impTicketErro(msg);
          }
         }).executar();
    }

  /*  private void consultarTickesAberto(){
         List<PedidoTiket> l = PedidoTicketDao.getPedidoTicketDao(ctx).getPedidoTicketStatus(0) ;
         if (l.size() > 0)
            setTicketsView(l);
         else onControleTicket.impTicketOk();
    }*/

    private void setTicketsView(List<PedidoTiket> l){
        if (l.size() > 0)
          new ControlePedidoTicketView(ctx, this).setTicketsViews(l);
        else onControleTicket.impTicketOk() ;
    }

    @Override
    public void onControleContaTicketViewOk(List<PedidoTicketView> l) {
        Bitmap bitmap =  new ImpressaoPedidoTicketBitMap(ctx, l).getBitMap(350, 20) ;
        onControleTicket.impTicketChange("Imprimindo Tickets...", bitmap);
        onImpressaoBitMap.imprimirBitMap(bitmap);
    }

    @Override
    public void onControleContaTicketViewErro(String msg) {
        onControleTicket.impTicketErro(msg);
    }

    @Override
    public void imprimirBitMapOk() {
        onControleTicket.impTicketOk();
    }

    @Override
    public void imprimirBitMapErro(String msg) {
        onControleTicket.impTicketErro(msg);
    }

}

