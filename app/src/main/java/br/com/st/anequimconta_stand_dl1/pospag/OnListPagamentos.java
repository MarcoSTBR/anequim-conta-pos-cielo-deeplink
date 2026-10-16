package br.com.st.anequimconta_stand_dl1.pospag;

import java.util.List;

public interface OnListPagamentos {
    void OnSucess (List<PosPagamentoView> l) ;
    void OnErro (String msg) ;

}
