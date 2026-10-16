package br.com.st.anequimconta_stand_dl1.impressao.generico;

import android.app.Activity;
import android.graphics.Bitmap;

import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;

public class ImpressaoBitpmapGENERICO implements OnImpressaoBitMap {
    private Activity ctx;
    private ListenerImpressaoBipMap listenerImpressaoBipMap;

    public ImpressaoBitpmapGENERICO(Activity ctx, ListenerImpressaoBipMap listenerImpressaoBipMap) {
        this.ctx = ctx;
        this.listenerImpressaoBipMap = listenerImpressaoBipMap;
    }

    @Override
    public void imprimirBitMap(Bitmap bitmap) {
     //   listenerImpressaoBipMap.imprimirBitMapErro("Implementar GENERICO");
        listenerImpressaoBipMap.imprimirBitMapOk();

    }

}
