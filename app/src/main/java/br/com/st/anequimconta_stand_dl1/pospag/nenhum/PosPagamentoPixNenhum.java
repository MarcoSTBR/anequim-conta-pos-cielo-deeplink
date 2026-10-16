package br.com.st.anequimconta_stand_dl1.pospag.nenhum;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPix;


public class PosPagamentoPixNenhum implements PosPagamentoPix {
    private Activity ctx ;
    public PosPagamentoPixNenhum(Activity ctx) {
        this.ctx = ctx;
    }

    @Override
    public void pagamento(ContaPag pag, OnPosPagamentoPix onPosPagamentoPix) {
        onPosPagamentoPix.onPixSucess(pag);
    }

    @Override
    public void cancelamento(ContaPag pag, OnPosCancelamentoPix onPosCancelamentoPix) {
        onPosCancelamentoPix.onCancelamentoPixSucess(pag);
    }
}
