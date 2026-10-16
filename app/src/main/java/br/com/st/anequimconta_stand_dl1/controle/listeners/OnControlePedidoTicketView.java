package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoTicketView;

import java.util.List;

public interface OnControlePedidoTicketView {
    void onControleContaTicketViewOk(List<PedidoTicketView> l) ;
    void onControleContaTicketViewErro(String msg) ;
}
