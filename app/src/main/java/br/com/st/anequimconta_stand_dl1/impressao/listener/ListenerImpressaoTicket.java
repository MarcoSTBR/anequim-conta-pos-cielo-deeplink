package br.com.st.anequimconta_stand_dl1.impressao.listener;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;

public interface ListenerImpressaoTicket {
    void ticketOK(PedidoTiket vt) ;
    void ticketErro(int cod, String msg) ;
}
