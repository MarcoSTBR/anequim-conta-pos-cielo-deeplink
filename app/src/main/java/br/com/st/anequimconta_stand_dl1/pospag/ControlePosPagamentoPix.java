package br.com.st.anequimconta_stand_dl1.pospag;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

import br.com.st.anequimconta_stand_dl1.pospag.nenhum.PosPagamentoPixNenhum;
import br.com.st.anequimconta_stand_dl1.pospag.paystone.PosPagamentoPixPayStone;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;

public class ControlePosPagamentoPix {

    private Activity ctx ;
    private PosPagamentoPix posPagamentoPix ;

    public ControlePosPagamentoPix(FragmentPagamento f) {
        this.ctx = f.getActivity();
        switch (ConfigPOS.getPosPagType(ctx)) {
            case posCielo: posPagamentoPix = new PosPagamentoPixPayStone(f);
            break;
            default: posPagamentoPix = new PosPagamentoPixNenhum(ctx) ;
        }
    }

    public void pagamento(ContaPag pag, OnPosPagamentoPix onPosPagamentoPix){
        posPagamentoPix.pagamento(pag, onPosPagamentoPix) ;
    }

    public void cancelamento(ContaPag pag, OnPosCancelamentoPix onPosCancelamentoPix){
        posPagamentoPix.cancelamento(pag, onPosCancelamentoPix);
    }

}
