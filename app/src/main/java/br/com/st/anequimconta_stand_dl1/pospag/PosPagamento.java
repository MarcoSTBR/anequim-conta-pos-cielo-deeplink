package br.com.st.anequimconta_stand_dl1.pospag;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

public interface PosPagamento {
    void pagamento(ContaPag pag, OnPosPagamento onPosPagamento) ;
    void cancelamento(ContaPag pag, OnPosCancelamento onPosCancelamento ) ;

}
