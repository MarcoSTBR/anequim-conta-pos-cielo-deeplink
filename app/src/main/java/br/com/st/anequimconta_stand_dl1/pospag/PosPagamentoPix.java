package br.com.st.anequimconta_stand_dl1.pospag;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

public interface PosPagamentoPix {

    void pagamento(ContaPag pag, OnPosPagamentoPix onPosPagamentoPix) ;
    void cancelamento(ContaPag pag, OnPosCancelamentoPix onPosCancelamentoPix) ;


}
