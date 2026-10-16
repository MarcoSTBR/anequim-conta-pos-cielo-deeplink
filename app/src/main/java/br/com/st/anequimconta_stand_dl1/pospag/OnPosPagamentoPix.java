package br.com.st.anequimconta_stand_dl1.pospag;


import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

public interface OnPosPagamentoPix {

    void onPixSucess(ContaPag it) ;
    void onPixError(String msg) ;
}
