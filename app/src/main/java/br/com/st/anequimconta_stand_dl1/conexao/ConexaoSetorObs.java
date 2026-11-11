package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.SetorObs;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public abstract class ConexaoSetorObs extends ConexaoServer{

    private final String TAG = "setor_obs" ;

    public ConexaoSetorObs(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Observaçöes..." ;
        method = "POST" ;
        maps.put("class","AfoodSetorObs") ;
        maps.put("method","loadAll2") ;
        maps.put("filters",f.getJSON()) ;
        maps.put("order", order) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
        Log.i(TAG, "filters "+f.getJSON().toString()+" order "+order) ;
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
        List<SetorObs> l = new ArrayList<>() ;
        for (int i = 0 ; i < j.length() ; i++) {
           l.add(new SetorObs(j.getJSONObject(i))) ;
        }
        setorObsOk(l);
    }

    public abstract void setorObsOk(List<SetorObs> l) ;
    public abstract void erro(String msg) ;
}
