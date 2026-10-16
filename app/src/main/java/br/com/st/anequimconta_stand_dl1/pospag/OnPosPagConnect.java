package br.com.st.anequimconta_stand_dl1.pospag;

public interface OnPosPagConnect {
    void connected(PosPagType tipo) ;
    void erro(String msg) ;
}
