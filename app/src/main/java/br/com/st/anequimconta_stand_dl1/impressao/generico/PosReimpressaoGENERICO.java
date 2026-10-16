package br.com.st.anequimconta_stand_dl1.impressao.generico;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControlePosReimpressao;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;
import br.com.st.anequimconta_stand_dl1.pospag.PosReimpressao;

public class PosReimpressaoGENERICO implements PosReimpressao {
    private Context ctx ;
    private PosPagamentoView posPagamentoView ;
    private OnControlePosReimpressao onControlePosReimpressao ;
    public PosReimpressaoGENERICO(Context ctx, PosPagamentoView posPagamentoView, OnControlePosReimpressao onControlePosReimpressao) {
        this.ctx = ctx;
        this.posPagamentoView = posPagamentoView;
        this.onControlePosReimpressao = onControlePosReimpressao;
    }
    @Override
    public void reimpressao(PosPagamentoView posPagamentoView, OnControlePosReimpressao onControlePosReimpressao) {
        onControlePosReimpressao.impressaoErro("Reimpressão não habilitada para este terminal!");
    }
}
