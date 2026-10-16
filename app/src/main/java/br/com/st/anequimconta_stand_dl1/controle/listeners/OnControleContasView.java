package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.util.List;

public interface OnControleContasView {

    void controleContaViewOk(List<ContaView> contaViews) ;
    void controleContaViewErro(String msg) ;

}
