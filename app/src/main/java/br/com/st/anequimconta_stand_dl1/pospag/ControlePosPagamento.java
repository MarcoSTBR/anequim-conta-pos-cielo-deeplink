package br.com.st.anequimconta_stand_dl1.pospag;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListPix;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;

public class ControlePosPagamento {

    private Activity ctx ;
    private PosPagamento posPagamento ;

    public ControlePosPagamento(FragmentListCartao f) {
        this.ctx = f.getActivity();
        posPagamento = PosPagamentoBuilder.getPosPagamento(f);
    }

    public ControlePosPagamento(FragmentListPix f) {
        this.ctx = f.getActivity();
        posPagamento = PosPagamentoBuilder.getPosPagamento(f);
    }


    public ControlePosPagamento(FragmentPagamento f) {
        this.ctx = f.getActivity();
        posPagamento = PosPagamentoBuilder.getPosPagamento(f);
    }

    public void pagamento(ContaPag pag, OnPosPagamento onPosPagamento){
        posPagamento.pagamento(pag, onPosPagamento) ;
    }

    public void cancelamento(ContaPag pag, OnPosCancelamento onPosCancelamento){
        posPagamento.cancelamento(pag, onPosCancelamento);
    }

}
