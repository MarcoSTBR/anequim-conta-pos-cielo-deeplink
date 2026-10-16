package br.com.st.anequimconta_stand_dl1.pospag;

import android.content.Context;

public class PosDesfazimentoBuilder {
    private Context ctx ;

    private OnPosDesfazimento onPosDesfazimento ;

    public PosDesfazimentoBuilder(Context ctx, OnPosDesfazimento onPosDesfazimento) {
        this.ctx = ctx;
        this.onPosDesfazimento = onPosDesfazimento;
    }

    public PosDesfazimento getPosdesfazimento(){
        return new PosDesfazimento() {
            @Override
            public void desfazerPayment(PosPagamentoView p) {

            }
        };
    }
}
