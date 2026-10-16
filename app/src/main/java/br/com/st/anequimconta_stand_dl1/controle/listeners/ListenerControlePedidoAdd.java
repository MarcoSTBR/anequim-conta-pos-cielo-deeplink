package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.Pedido;

public interface ListenerControlePedidoAdd {

    void pedidoOk(Pedido pedido) ;
    void pedidoErro(String msg) ;

}
