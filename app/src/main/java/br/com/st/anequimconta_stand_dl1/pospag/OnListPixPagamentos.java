package br.com.st.anequimconta_stand_dl1.pospag;

import java.util.List;

public interface OnListPixPagamentos {
    void OnPixSucess (List<PosPagamentoPixView> l) ;
    void OnPixError (String msg) ;

}
