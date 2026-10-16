package br.com.st.anequimconta_stand_dl1.pospag.nenhum;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamento;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagamento;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamento;


public class PosPagamentoNenhum implements PosPagamento {

    private Activity ctx ;
    private OnPosPagamento onPosPagamento ;
    private OnPosCancelamento onPosCancelamento ;

    public PosPagamentoNenhum(Activity ctx) {
        this.ctx = ctx;
    }

    @Override
    public void pagamento(ContaPag pag, OnPosPagamento onPosPagamento) {
        this.onPosPagamento = onPosPagamento ;
        this.onPosPagamento.onSucess(pag);
    }

    @Override
    public void cancelamento(ContaPag pag, OnPosCancelamento onPosCancelamento) {
        this.onPosCancelamento = onPosCancelamento ;
        this.onPosCancelamento.onCancelamentoSucess(pag);

    }
}
