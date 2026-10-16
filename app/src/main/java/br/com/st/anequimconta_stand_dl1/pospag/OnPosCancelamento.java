package br.com.st.anequimconta_stand_dl1.pospag;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

public interface OnPosCancelamento {
    void onCancelamentoSucess(ContaPag contaPag) ;
    void onCancelamentoErro(String msg) ;

}
