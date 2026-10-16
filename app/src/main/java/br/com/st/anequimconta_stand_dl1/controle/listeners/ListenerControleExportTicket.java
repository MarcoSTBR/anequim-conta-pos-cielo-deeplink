package br.com.st.anequimconta_stand_dl1.controle.listeners;

import java.util.List;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;

public interface ListenerControleExportTicket {
    void ControleExportTicketOk(List<PedidoTiket> l) ;
    void ControleExportTicketErro(String msg) ;

}
