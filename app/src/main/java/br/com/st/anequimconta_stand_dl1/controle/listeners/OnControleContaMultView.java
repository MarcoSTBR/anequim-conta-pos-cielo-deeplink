package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.ContaMultView;

public interface OnControleContaMultView {

    void ControleContaMultViewOk(ContaMultView vc) ;
    void ControleContaMultViewErro(String msg) ;
}
