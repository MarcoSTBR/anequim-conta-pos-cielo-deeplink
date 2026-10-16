package br.com.st.anequimconta_stand_dl1.impressao.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.impressao.ContaViewPagBitMap;
import br.com.st.anequimconta_stand_dl1.impressao.ImpressaoBitmapBuilder;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControleImpContaPagView;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;
import br.com.st.anequimconta_stand_dl1.ui.FragmentContasPagas;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;

public class ControleImpContaPagView {

    private Activity ctx ;
    private OnControleImpContaPagView onControleImpContaPagView ;
    private OnImpressaoBitMap onImpressaoBitMap ;

    public ControleImpContaPagView(FragmentPagamento act, OnControleImpContaPagView onControleImpContaPagView) {
        this.ctx = act.getActivity();
        this.onControleImpContaPagView = onControleImpContaPagView;
        onImpressaoBitMap = new ImpressaoBitmapBuilder(ctx, new ListenerImpressaoBipMap() {
            @Override
            public void imprimirBitMapOk() {
                onControleImpContaPagView.controleImpContaPagViewOk();
            }

            @Override
            public void imprimirBitMapErro(String msg) {
                onControleImpContaPagView.controleImpContaPagViewErro(msg);
            }
        }).getImpressao(act) ;
    }

    public ControleImpContaPagView(FragmentContasPagas act, OnControleImpContaPagView onControleImpContaPagView) {
        this.ctx = act.getActivity();
        this.onControleImpContaPagView = onControleImpContaPagView;
        onImpressaoBitMap = new ImpressaoBitmapBuilder(ctx, new ListenerImpressaoBipMap() {
            @Override
            public void imprimirBitMapOk() {
                onControleImpContaPagView.controleImpContaPagViewOk();
            }

            @Override
            public void imprimirBitMapErro(String msg) {
                onControleImpContaPagView.controleImpContaPagViewErro(msg);
            }
        }).getImpressao(act) ;
    }



    public void imprimir(ContaViewPagView contaViewPagView){
        onImpressaoBitMap.imprimirBitMap(new ContaViewPagBitMap(ctx, contaViewPagView).getBitMap(350, 20));
    }

}
