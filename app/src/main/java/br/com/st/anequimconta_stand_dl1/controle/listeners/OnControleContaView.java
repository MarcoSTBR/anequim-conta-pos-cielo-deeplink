package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

public interface OnControleContaView {

    void controleContaViewOk(ContaView contaView) ;
    void controleContaViewErro(String msg) ;

}
