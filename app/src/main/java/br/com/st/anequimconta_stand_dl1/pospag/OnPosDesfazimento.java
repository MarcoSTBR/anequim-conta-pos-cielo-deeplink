package br.com.st.anequimconta_stand_dl1.pospag;

public interface OnPosDesfazimento {
    void desfazimentoOK(PosPagamentoView pay) ;
    void desfazimentoErro(String msg) ;

}
