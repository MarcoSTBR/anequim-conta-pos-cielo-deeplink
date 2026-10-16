package br.com.st.anequimconta_stand_dl1.impressao;

import java.util.List;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoListTicket;


public interface ImpressoraListTicket {
     void imprimirTickets(List<PedidoTiket> tickets, ListenerImpressaoListTicket listenerImpressaoListTicket) ;

}
