package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;

import java.util.List;

public interface OnControleContasPagas {

    void onControleContasPagasOk(List<ContaViewPagView> l) ;
    void onControleContasPagasErro(String msg) ;
}
