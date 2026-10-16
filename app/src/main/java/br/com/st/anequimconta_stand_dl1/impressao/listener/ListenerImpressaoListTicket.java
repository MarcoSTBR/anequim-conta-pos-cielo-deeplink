package br.com.st.anequimconta_stand_dl1.impressao.listener;

import java.util.List;


import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;

public interface ListenerImpressaoListTicket {
    void ticketOK(List<PedidoTiket> listTickets) ;
    void ticketErro(int cod, String msg) ;
}
