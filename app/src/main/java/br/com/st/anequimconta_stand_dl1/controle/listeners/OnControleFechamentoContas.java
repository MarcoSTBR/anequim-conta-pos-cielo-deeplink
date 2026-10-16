package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;

public interface OnControleFechamentoContas {

    void controleFechamentoContasOk(ContaMult contaMult) ;
    void controleFechamentoContasErro(String msg) ;

}
