package br.com.st.anequimconta_stand_dl1.pospag;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigPOS {


    public static PosPagType getPosPagType(Context ctx) {
        return PosPagType.posCielo;
/*
        try {
            Log.i("ConfigPOS", "ler "+lerString(ctx, "POS_TYPE")) ;
            return PosPagType.getValor(lerString(ctx, "POS_TYPE"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ConfigPOS", "ler EXCEÇÂO "+lerString(ctx, "POS_TYPE")) ;
            return PosPagType.posStone ;
        }
*/
    }

    public static void setPosPagType(Context ctx, PosPagType p) {
        Log.i("ConfigPOS", "gravar "+p.valor) ;
        gravarString(ctx, "POS_TYPE", p.valor);
    }

    public static int getProximoPaymentId(Context ctx) {
        int px = Integer.parseInt(new SimpleDateFormat("ddHHmmSSS").format(new Date()));
        gravarString(ctx, "PAYMENT_ID", String.valueOf(px));
        return px;
    }



    public static int getUltimoPaymentId(Context ctx) {
        return lerInt(ctx, "PAYMENT_ID", 0);
    }

    private static int lerInt(Context ctx, String chave, int padrao) {
        try {
            int i = Integer.parseInt(lerString(ctx, chave));
            return i;
        } catch (Exception e) {
            return padrao;
        }
    }

    private static String lerString(Context ctx, String chave) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        String ret = preference.getString(chave, "");
        return ret;
    }

    private static void gravarString(Context ctx, String chave, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(chave, value);
        editor.commit();
    }


}
