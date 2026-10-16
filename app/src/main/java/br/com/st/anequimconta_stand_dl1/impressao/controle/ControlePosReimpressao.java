package br.com.st.anequimconta_stand_dl1.impressao.controle;

import br.com.st.anequimconta_stand_dl1.impressao.generico.PosReimpressaoGENERICO;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControlePosReimpressao;
import br.com.st.anequimconta_stand_dl1.impressao.stone.PosReimpressaoPayStone;
import br.com.st.anequimconta_stand_dl1.pospag.ConfigPOS;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;
import br.com.st.anequimconta_stand_dl1.pospag.PosReimpressao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;

public class ControlePosReimpressao {
    private FragmentListCartao ctx ;
    private PosReimpressao posReimpressao = null ;
    public ControlePosReimpressao(FragmentListCartao ctx) {
        this.ctx = ctx;
        posReimpressao = new PosReimpressaoPayStone(ctx);
    }

    public void reimpressao(PosPagamentoView pag, OnControlePosReimpressao onControlePosReimpressao){
        switch (ConfigPOS.getPosPagType(ctx.getActivity())) {
            case posCielo:  posReimpressao.reimpressao(pag, onControlePosReimpressao);
            break;
            default:  new PosReimpressaoGENERICO(ctx.getActivity(), pag, onControlePosReimpressao).reimpressao(pag, onControlePosReimpressao);
        }


    }


}
