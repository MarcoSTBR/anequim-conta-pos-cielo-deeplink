package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoImpressaoPos;
import br.com.st.anequimconta_stand_dl1.impressao.util.ImpressaoUtil;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public abstract class ConexaoPayStoneKey extends ConexaoServer {

    public ConexaoPayStoneKey(Activity ctx, DaoFilters filterTables, String order) throws MalformedURLException {
        super(ctx);
        method = "GET";
        msg = "Consultando Chave Stone";
        maps.put("class", "AfoodPayStoneKeyPos");
        maps.put("method", "loadAll2");
        maps.put("filters", filterTables.getJSONCx());
        Log.i("PayStoneKey", "filter "+filterTables.getJSONCx()) ;
        if (!order.isEmpty()){
            maps.put("order", order);
        }
        url = new URL(UtilSet.getServidorMaster(ctx));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("PayStoneKey", codInt + " " + s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success"))
                setJSON(j.getJSONArray("data"));
            else
                erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(s);
        } catch (Exception e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    private void setJSON(JSONArray j) throws JSONException {
        TipoImpressaoPos tp = null ;
        for (int i = 0; i < j.length(); i++) {
            JSONObject json = j.getJSONObject(i);
            tp = TipoImpressaoPos.getTipoImpressaoPos(json.getString("TERMINAL_MOD_POS")) ;
            ImpressaoUtil.setTipoImpressaoPos(ctx, tp);
        }
       // ok(false);
        if (tp != null) {
            ok(((tp == TipoImpressaoPos.GENERICO) || (tp == TipoImpressaoPos.SUNMIP2))? false : true);
        } else ok(false) ;
    }

    public abstract void ok(boolean b);

    public abstract void erro(String msg);
}
