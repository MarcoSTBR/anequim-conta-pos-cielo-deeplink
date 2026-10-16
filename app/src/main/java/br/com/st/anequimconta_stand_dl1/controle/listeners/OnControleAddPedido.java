package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.Conta;

public interface OnControleAddPedido {

    void numeroPedidoLiberado(String num) ;
    void numeroPedidoTravado(Conta conta) ;
    void numeroPedidoErro(String msg) ;
}
