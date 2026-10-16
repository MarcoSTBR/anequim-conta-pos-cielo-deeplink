package br.com.st.anequimconta_stand_dl1.pospag;

import android.app.Activity;
import android.content.Context;

public class ControlePosConected {

    private static ControlePosConected controlePosConected;

    public static ControlePosConected getControlePagConected(Activity ctx) {
        if (controlePosConected == null) {
            controlePosConected = new ControlePosConected(ctx);
            return controlePosConected;
        } else return controlePosConected;
    }

    private Activity ctx;

    public ControlePosConected(Activity ctx) {
        this.ctx = ctx;
    }

    public void executar(OnPosPagConnect onPosPagConnect) {
        PosClient posClient = new PosClientBuilder(ctx).getPosClient() ;
        posClient.conected(onPosPagConnect);
    }

}
