package br.com.st.anequimconta_stand_dl1.impressao.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoImpressaoPos;


public class UtilContaParam {

    public static final String TAG_CONTA_LAGURA = "TAG_CONTA_LAGURA" ;
    public static final String TAG_CONTA_TAN_FONT = "TAG_CONTA_TAN_FONT" ;
    public static final String TAG_CONTA_MENSAGEM = "TAG_CONTA_MENSAGEM" ;
    public static final String TAG_LAR_CONTA_IMP = "TAG_LAR_CONTA_IMP" ;
    public static final String TAG_TAM_CONTA_IMP = "TAG_TAM_CONTA_IMP" ;


    //  DISPLAY
    public static int getLarguraBitmap(Context ctx){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt(TAG_CONTA_LAGURA, 600);
    }

    //  DISPLAY
    public static void setLarguraBitmap(Context ctx, int largura){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_CONTA_LAGURA, largura) ;
        editor.commit() ;
    }

    //  DISPLAY
    public static int getTamFont(Context ctx){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt(TAG_CONTA_TAN_FONT, 30);
    }

    //  DISPLAY
    public static void setTamFont(Context ctx, int tamFont){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_CONTA_TAN_FONT, tamFont) ;
        editor.commit() ;
    }

    //IMPRESSÃO CONTA

    public static int getlargMaxConta(Context ctx){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt(TAG_LAR_CONTA_IMP, 350);
    }
    //IMPRESSÃO CONTA
    public static void setlargMaxConta(Context ctx, int largMax){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_LAR_CONTA_IMP, largMax) ;
        editor.commit() ;
    }
    //IMPRESSÃO CONTA
    public static int getLinhaNomalConta(Context ctx){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt(TAG_TAM_CONTA_IMP, 20);
    }
    //IMPRESSÃO CONTA
    public static void setLinhaNomalConta(Context ctx, int i){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_TAM_CONTA_IMP, i) ;
        editor.commit() ;
    }





    public static String getMensagemPromocional(Context ctx){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getString(TAG_CONTA_MENSAGEM, "Obrigado;Volte Sempre!!!");
    }
    public static void setMensagemPromocional(Context ctx, String msg){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(TAG_CONTA_MENSAGEM, msg) ;
        editor.commit() ;
    }

}
