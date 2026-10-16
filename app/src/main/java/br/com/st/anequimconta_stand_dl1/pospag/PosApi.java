package br.com.st.anequimconta_stand_dl1.pospag;

public interface PosApi {
    void getList(PosFilter filter, OnListPagamentos listener) ;
    void getPixList(PosFilter filter, OnListPixPagamentos listener) ;
}
