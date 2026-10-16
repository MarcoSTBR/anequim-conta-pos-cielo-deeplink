package br.com.st.anequimconta_stand_dl1.pospag;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.pospag.nenhum.PosClientPlayNenhum;

public class PosClientBuilder {
    private Activity ctx;
    private PosClient posClient;

    public PosClientBuilder(Activity ctx) {
        this.ctx = ctx;
    }

    public PosClient getPosClient() {
        switch (ConfigPOS.getPosPagType(ctx)) {
            case posCielo:
                posClient = new PosClientPlayNenhum(ctx);
                break;
            default:
                posClient = new PosClientPlayNenhum(ctx);
        }

        Log.i("PostCli", "Valor "+ConfigPOS.getPosPagType(ctx).valor) ;
        return posClient;
    }
}
