package br.com.st.anequimconta_stand_dl1.impressao.controle;

import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControlePosReimpressao;
import br.com.st.anequimconta_stand_dl1.impressao.cielo.PosReimpressaoPayCielo;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;
import br.com.st.anequimconta_stand_dl1.pospag.PosReimpressao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;

public class ControlePosReimpressao {

    private final String TAG = "reimpressao" ;
    private FragmentListCartao ctx ;
    private PosReimpressao posReimpressao = null ;

    public ControlePosReimpressao(FragmentListCartao ctx) {
        this.ctx = ctx;
        posReimpressao = new PosReimpressaoPayCielo(ctx);
    }

    public void reimpressao(PosPagamentoView pag, OnControlePosReimpressao onControlePosReimpressao){
        posReimpressao.reimpressao(pag, onControlePosReimpressao);
    }
}
