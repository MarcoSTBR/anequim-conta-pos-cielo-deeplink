package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;

import java.util.List;

public interface OnControleModalidade {
    void controleModalidadeOk(List<Modalidade> l) ;
    void controleModalidadeErro(String msg) ;
}
