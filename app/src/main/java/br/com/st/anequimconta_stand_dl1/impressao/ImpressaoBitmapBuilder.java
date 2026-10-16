package br.com.st.anequimconta_stand_dl1.impressao;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.activity.ActivityEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.activity.ActivityImprimirContas;
import br.com.st.anequimconta_stand_dl1.impressao.generico.ImpressaoBitpmapGENERICO;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;
import br.com.st.anequimconta_stand_dl1.impressao.stone.ImpressaoBitmapStone;
import br.com.st.anequimconta_stand_dl1.impressao.util.ImpressaoUtil;
import br.com.st.anequimconta_stand_dl1.ui.FragmentCaixa;
import br.com.st.anequimconta_stand_dl1.ui.FragmentContasPagas;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;

public class ImpressaoBitmapBuilder {

    private Activity ctx ;
    private ListenerImpressaoBipMap listenerImpressaoBipMap ;

    public ImpressaoBitmapBuilder(Activity ctx, ListenerImpressaoBipMap listenerImpressaoBipMap) {
        this.ctx = ctx;
        this.listenerImpressaoBipMap = listenerImpressaoBipMap;
    }

    public OnImpressaoBitMap getImpressao(FragmentPagamento act){
        switch (ImpressaoUtil.getTipoImpressaoPos(act.getActivity())){
            case SUNMIP2: return new ImpressaoBitmapStone(act, listenerImpressaoBipMap) ;
            default:return new ImpressaoBitpmapGENERICO(act.getActivity(), listenerImpressaoBipMap) ;
        }
    }

    public OnImpressaoBitMap getImpressao(FragmentCaixa act){
        switch (ImpressaoUtil.getTipoImpressaoPos(act.getActivity())){
            case SUNMIP2: return new ImpressaoBitmapStone(act, listenerImpressaoBipMap) ;
            default:return new ImpressaoBitpmapGENERICO(act.getActivity(), listenerImpressaoBipMap) ;
        }
    }


    public OnImpressaoBitMap getImpressao(FragmentContasPagas act){
        switch (ImpressaoUtil.getTipoImpressaoPos(act.getActivity())){
            case SUNMIP2: return new ImpressaoBitmapStone(act, listenerImpressaoBipMap) ;
            default:return new ImpressaoBitpmapGENERICO(act.getActivity(), listenerImpressaoBipMap) ;
        }
    }

    public OnImpressaoBitMap getImpressao(ActivityEnvioPedidos act){
        switch (ImpressaoUtil.getTipoImpressaoPos(act)){
            case SUNMIP2: return new ImpressaoBitmapStone(act, listenerImpressaoBipMap) ;
            default:return new ImpressaoBitpmapGENERICO(act, listenerImpressaoBipMap) ;
        }
    }

    public OnImpressaoBitMap getImpressao(ActivityImprimirContas act){
        switch (ImpressaoUtil.getTipoImpressaoPos(act)){
            case SUNMIP2: return new ImpressaoBitmapStone(act, listenerImpressaoBipMap) ;
            default:return new ImpressaoBitpmapGENERICO(act, listenerImpressaoBipMap) ;
        }
    }

}
