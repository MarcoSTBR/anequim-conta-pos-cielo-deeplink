package br.com.st.anequimconta_stand_dl1.pospag;

import br.com.st.anequimconta_stand_dl1.pospag.nenhum.PosPagamentoNenhum;
import br.com.st.anequimconta_stand_dl1.pospag.payCielo.PosPagamentoPayCielo;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListPix;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;


public class PosPagamentoBuilder {

    public static PosPagamento getPosPagamento(FragmentListCartao ctx) {
        if (ConfigPOS.getPosPagType(ctx.getActivity()) == PosPagType.posCielo)
            return new PosPagamentoPayCielo(ctx);
           else return new PosPagamentoNenhum(ctx.getActivity()) ;
    }

    public static PosPagamento getPosPagamento(FragmentPagamento ctx) {
        if (ConfigPOS.getPosPagType(ctx.getActivity()) == PosPagType.posCielo)
            return new PosPagamentoPayCielo(ctx);
        else return new PosPagamentoNenhum(ctx.getActivity()) ;
    }

    public static PosPagamento getPosPagamento(FragmentListPix f) {
        if (ConfigPOS.getPosPagType(f.getActivity()) == PosPagType.posCielo)
            return new PosPagamentoPayCielo(f);
        else return new PosPagamentoNenhum(f.getActivity()) ;
    }




}
