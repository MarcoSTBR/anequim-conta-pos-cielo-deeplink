package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoTicketPedidoEdit;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaTickets;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleValidaTicket;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ControleValidaTicket {
    private Activity ctx ;
    private OnControleValidaTicket onControleValidaTicket;

    public ControleValidaTicket(Activity ctx, OnControleValidaTicket onControleValidaTicket) {
        this.ctx = ctx;
        this.onControleValidaTicket = onControleValidaTicket;
    }


    public void validarUUID(String uuid){
        try {
            DaoFilters f = new DaoFilters() ;
            f.add(new DaoFilter("UUID", "=", uuid));
            new ConexaoContaTickets(ctx, f, "") {
                @Override
                public void ticketOk(List<PedidoTiket> l) {
                    verificaVendaWeb(l.get(0)) ;
                }

                @Override
                public void ticketErro(String msg) {
                    onControleValidaTicket.ticketInvalido(msg);
                }

            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleValidaTicket.ticketErro(e.getMessage());
        }

    }

    private void verificaVendaWeb(PedidoTiket vt) {
        if (vt.getSTATUS() == 1) {
            validarTicket(vt) ;
        } else {
            onControleValidaTicket.ticketInvalido("ESTE TICKET JÁ FOI USADO!\nEm "+
                    new SimpleDateFormat("dd/MM/yy HH:mm").format(vt.getDATA_FECHAMENTO()));
        }
    }

    private void validarTicket(PedidoTiket vt) {
        vt.setSTATUS(2);
        vt.setDATA_FECHAMENTO(new Date());
        vt.setUSUARIO_F_ID(UtilSet.getUsuarioId(ctx));
        try {
            new ConexaoTicketPedidoEdit(ctx, vt.getJSON()) {
                @Override
                public void conexaoPedidoTicketEditOk(PedidoTiket p) {
                    onControleValidaTicket.ticketRecebido(getMensageOK(p));
                }

                @Override
                public void conexaoPedidoTicketEditErro(String msg) {
                    onControleValidaTicket.ticketErro(msg) ;
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleValidaTicket.ticketErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleValidaTicket.ticketErro(e.getMessage());
        }
    }

    private String getMensageOK(PedidoTiket vts) {
       String s =  vts.getUUID()+"\nFoi Validado Com Sucesso!" ;
       return s ;
    }
}
