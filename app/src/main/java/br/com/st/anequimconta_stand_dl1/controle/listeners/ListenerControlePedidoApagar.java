package br.com.st.anequimconta_stand_dl1.controle.listeners;

public interface ListenerControlePedidoApagar {

    void pedidoApagado(int quantidadeRestante) ;
    void pedidoApagarErro(String msg) ;

}
