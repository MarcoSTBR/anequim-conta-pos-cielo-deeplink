package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.util.ArrayList;
import java.util.List;

public class ControleMaisVendidos {

    private final static String TAG_GRADE_ID = "GRADE_MAIS_VENDIDOS_ID";
    private final static String TAG_COL = "GRADE_MAIS_VENDIDOS_COL";

    private static ControleMaisVendidos controleMaisVendidos = null;

    public static ControleMaisVendidos getControleMaisVendidos(Activity ctx) {
        if (controleMaisVendidos == null) {
            controleMaisVendidos = new ControleMaisVendidos(ctx);
            return controleMaisVendidos;
        } else  return controleMaisVendidos;
    }

    public void setCtx(Activity ctx) {
        this.ctx = ctx;
    }

    private Activity ctx;
    private List<Produto> produtoList;
    private static boolean fAtualizado = false;


    public ControleMaisVendidos(Activity ctx) {
        this.ctx = ctx;
    }

    public void atualizar() {
        try {
            Log.i("atualizar_", "ControleMaisVendidos");
            int id = getGradeID(ctx) ;
            Log.i("atualizar_", "ControleMaisVendidos id "+id);

            produtoList = ControleGradeView.getControleGradeView(ctx).get(getGradeID(ctx)).getProdutoList() ;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("atualizar_", "ControleMaisVendidos id "+e.getMessage());
            produtoList = new ArrayList<>() ;
        }
        fAtualizado = true;
    }

    public boolean isAtualizado() {
        return fAtualizado;
    }

    public void setAtualizado(boolean fAtualizado) {
        this.fAtualizado = fAtualizado;
    }

    public List<Produto> getList() {
        if (!fAtualizado) {
            atualizar();
            return produtoList;
        } else
            return produtoList;
    }

    public static int getGradeID(Context ctx) {
        try {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
            return preference.getInt(TAG_GRADE_ID, 0);
        } catch (ClassCastException e) {
            return 0 ;
        }
    }

    public static void setGradeID(Context ctx, int cod) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_GRADE_ID, cod);
        editor.commit();
    }

    public static int getColunas(Context ctx) {
        try {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
            int nulCol = preference.getInt(TAG_COL, 1);
            if (nulCol < 1) return 1;
            else return nulCol;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return 1 ;
        }
    }

    public static void setColunas(Context ctx, int nulCol) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(TAG_COL, nulCol);
        editor.commit();
    }


}
