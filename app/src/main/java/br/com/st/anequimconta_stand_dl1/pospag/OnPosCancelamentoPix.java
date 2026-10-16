package br.com.st.anequimconta_stand_dl1.pospag;


import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

public interface OnPosCancelamentoPix {
    void onCancelamentoPixSucess(ContaPag it) ;
    void onCancelamentoPixError(String msg) ;
}
