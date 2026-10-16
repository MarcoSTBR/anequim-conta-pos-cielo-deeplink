package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoTicketPedidoEdit;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleExportTicket;
import br.com.st.anequimconta_stand_dl1.dao.PedidoTicketDao;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;

public class ControleExportTicket {

    private Activity ctx ;
    private ListenerControleExportTicket listenerControleExportTicket ;
    private List<PedidoTiket> list ;
    private List<PedidoTiket> listUp ;
    private int contador ;

    public ControleExportTicket(Activity ctx, ListenerControleExportTicket listenerControleExportTicket) {
        this.ctx = ctx;
        this.listenerControleExportTicket = listenerControleExportTicket;
    }

    public void executar(){
        listUp = new ArrayList<>() ;
        list = PedidoTicketDao.getPedidoTicketDao(ctx).getPedidoTicketStatus(0) ;
        Log.i("ticketexe", "list size "+list.size()) ;
        if (list.size() == 0)
            listenerControleExportTicket.ControleExportTicketOk(listUp);
         else {
             contador = -1 ;
             proximo();
        }
    }

    private void proximo(){
        ++contador ;
        Log.i("ticketexe", "contador "+contador+" size "+list.size()) ;
        if (list.size() >= contador+1){
           send(list.get(contador));
        } else listenerControleExportTicket.ControleExportTicketOk(listUp);
    }


    private void send(PedidoTiket pt){
        Log.i("ticketexe", "send "+contador+" size "+list.size()+" pt "+pt.toJSONString()) ;
        try {
            new ConexaoTicketPedidoEdit(ctx, pt.getJSONInsert()) {
                @Override
                public void conexaoPedidoTicketEditOk(PedidoTiket p) {
                    listUp.add(p) ;
                    PedidoTicketDao.getPedidoTicketDao(ctx).delete(p.getUUID());
                    Log.i("ticketexe", "PedidoTiket "+p.toJSONString()) ;
                    proximo();
                }

                @Override
                public void conexaoPedidoTicketEditErro(String msg) {
                    listenerControleExportTicket.ControleExportTicketErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerControleExportTicket.ControleExportTicketErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControleExportTicket.ControleExportTicketErro(e.getMessage());
        }

    }



}
