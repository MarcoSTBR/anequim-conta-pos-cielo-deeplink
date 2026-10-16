package br.com.st.anequimconta_stand_dl1.controle.listeners;


import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;

public interface ListenrControleVenda {
    void vendaOk(PedidoItem it) ;
    void vendaErro(String msg) ;

}
