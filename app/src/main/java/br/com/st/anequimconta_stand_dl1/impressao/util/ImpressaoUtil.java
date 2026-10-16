package br.com.st.anequimconta_stand_dl1.impressao.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoImpressaoPos;


public class ImpressaoUtil {

    public static TipoImpressaoPos getTipoImpressaoPos(Context ctx){
/*
       SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
       String ret = preference.getString("POS_TIPO", "GENERICO");
       return TipoImpressaoPos.valueOf(ret) ;
*/
       return TipoImpressaoPos.SUNMIP2 ;
    }



    public static void setTipoImpressaoPos(Context ctx, TipoImpressaoPos tp){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("POS_TIPO", tp.descricao) ;
        editor.commit() ;
    }

}
