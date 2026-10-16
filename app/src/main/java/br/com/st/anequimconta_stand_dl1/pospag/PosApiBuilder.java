package br.com.st.anequimconta_stand_dl1.pospag;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.pospag.nenhum.PosApiNenhum;
import br.com.st.anequimconta_stand_dl1.pospag.paystone.PosApiPayStone;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListPix;


public class PosApiBuilder {

    public static PosApi getApi(FragmentListCartao ctx) {
        switch (ConfigPOS.getPosPagType(ctx.getActivity())){
            case posCielo: return new PosApiPayStone(ctx) ;
            default: return new PosApiNenhum(ctx.getActivity()) ;
        }
    }

    public static PosApi getApi(FragmentListPix ctx) {
        switch (ConfigPOS.getPosPagType(ctx.getActivity())){
            case posCielo: return new PosApiPayStone(ctx) ;
            default: return new PosApiNenhum(ctx.getActivity()) ;
        }
    }

    public static PosApi getApi(Context ctx) {
        return new PosApiNenhum(ctx) ;
    }

}
