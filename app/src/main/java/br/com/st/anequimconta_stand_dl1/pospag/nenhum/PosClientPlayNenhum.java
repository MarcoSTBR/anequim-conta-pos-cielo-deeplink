package br.com.st.anequimconta_stand_dl1.pospag.nenhum;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagConnect;
import br.com.st.anequimconta_stand_dl1.pospag.PosClient;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagType;

public class PosClientPlayNenhum implements PosClient {

    private Context ctx ;

    public PosClientPlayNenhum(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    public void conected(OnPosPagConnect onPosPagConnect) {
        onPosPagConnect.connected(PosPagType.posCielo);
    }
}
