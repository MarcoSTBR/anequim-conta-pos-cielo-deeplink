package br.com.st.anequimconta_stand_dl1.pospag;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

public interface OnPosPagamento {
    void onSucess(ContaPag it) ;
    void onError(String msg) ;
}
