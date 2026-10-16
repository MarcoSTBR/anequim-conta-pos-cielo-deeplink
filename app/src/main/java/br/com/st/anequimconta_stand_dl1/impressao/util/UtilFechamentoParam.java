package br.com.st.anequimconta_stand_dl1.impressao.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class UtilFechamentoParam {

    public static final String TAG_FECHA_L = "TAG_FECHA_LAGURA" ;
    public static final String TAG_FECHA_TAM_FONT = "TAG_FECHA_TAN_FONT" ;

    public static int getLarguraBitmap(Context ctx){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt(TAG_FECHA_L, 700);
    }

    public static void setLarguraBitmap(Context ctx, int largura){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_FECHA_L, largura) ;
        editor.commit() ;
    }

    public static int getTamFont(Context ctx){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt(TAG_FECHA_TAM_FONT, 40);
    }

    public static void setTamFont(Context ctx, int tamFont){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_FECHA_TAM_FONT, tamFont) ;
        editor.commit() ;
    }


}
