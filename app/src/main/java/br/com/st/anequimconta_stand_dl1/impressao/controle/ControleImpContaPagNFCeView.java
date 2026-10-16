package br.com.st.anequimconta_stand_dl1.impressao.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControleImpContaPagView;
import br.com.st.anequimconta_stand_dl1.ui.FragmentContasPagas;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;

public class ControleImpContaPagNFCeView {

    private Activity ctx ;
    private OnControleImpContaPagView onControleImpContaPagView ;

    public ControleImpContaPagNFCeView(FragmentPagamento f, OnControleImpContaPagView onControleImpContaPagView) {
        this.ctx = f.getActivity();
        this.onControleImpContaPagView = onControleImpContaPagView;
    }

    public ControleImpContaPagNFCeView(FragmentContasPagas f, OnControleImpContaPagView onControleImpContaPagView) {
        this.ctx = f.getActivity();
        this.onControleImpContaPagView = onControleImpContaPagView;
    }

    public void imprimir(ContaViewPagView contaViewPagView){
        onControleImpContaPagView.controleImpContaPagViewErro("Sem contrato de serviço de NFCe!");
    }



}
