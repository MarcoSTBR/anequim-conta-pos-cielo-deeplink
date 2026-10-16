package br.com.st.anequimconta_stand_dl1.controle.listeners;

import java.util.List;

import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPixView;

public interface OnConsultaPIX {

    void ConsultaPix(List<PosPagamentoPixView> l) ;
    void ConsultaPixErro(String msg) ;

}
