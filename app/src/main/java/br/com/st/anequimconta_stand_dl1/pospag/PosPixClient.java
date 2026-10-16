package br.com.st.anequimconta_stand_dl1.pospag;

import android.app.Activity;

public class PosPixClient {

   private static PosPixClient pixClient ;
   public static PosPixClient getPixClient(Activity ctx){
       if (pixClient == null){
           pixClient = new PosPixClient(ctx) ;
           return pixClient ;
       } else return pixClient ;
   }

   private Activity ctx ;

    public PosPixClient(Activity ctx) {
        this.ctx = ctx;
    }

}
