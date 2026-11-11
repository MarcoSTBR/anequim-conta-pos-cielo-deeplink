package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.CartaoRFid;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public abstract class ConexaoCartaoRFid extends ConexaoServer{

    private final String TAG = "cartaoRFid" ;
    private Activity ctx ;

    public ConexaoCartaoRFid(Activity ctx, String rfid) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Cartão" ;
        method = "POST" ;
        this.ctx = ctx;
        maps.put("class","AfoodCartaoRfid") ;
        maps.put("method","loadAll2") ;
        maps.put("filters",getJSON(rfid).getJSON());
        maps.put("order", "id") ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    private DaoFilters getJSON(String rfid){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("RFID", "=", rfid));
        return f;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i(TAG, codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data")) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro("COD "+codInt+" "+s);
        }
    }

    private void  setJSON(JSONArray j) throws JSONException {
        for (int i = 0 ; i < j.length() ; i++) {
            CartaoRFid c = new CartaoRFid(j.getJSONObject(i)) ;
            Log.i(TAG, c.geJSON().toString()) ;
            ok(c);
            return;
        }
        erro("Cartão Não encontrado!") ;
    }

    public abstract void ok(CartaoRFid rFid) ;
    public abstract void erro(String msg) ;
}
