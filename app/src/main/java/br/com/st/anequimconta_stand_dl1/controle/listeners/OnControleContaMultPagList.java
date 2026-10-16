package br.com.st.anequimconta_stand_dl1.controle.listeners;

import java.util.List;

import br.com.st.anequimconta_stand_dl1.controle.ControleContaMultPagList;

public interface OnControleContaMultPagList {

    void ok(List<ControleContaMultPagList.PagamentoView> ls) ;
    void erro(String msg) ;
}
