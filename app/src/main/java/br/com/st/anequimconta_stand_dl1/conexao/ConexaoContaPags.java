package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPags;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaPags extends ConexaoServer {


    public ConexaoContaPags(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaPagsPos") ;
        maps.put("method", "loadAll2") ;
        maps.put("filters", f.getJSON()) ;
        if (order.length() > 0)
          maps.put("order", order) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_","ConexaoContaPags "+s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else conexaoContaPagsErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            conexaoContaPagsErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ContaPags> l = new ArrayList<ContaPags>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ContaPags(jaa.getJSONObject(i))) ;
        }
        conexaoContaPagsOk(l);
    }

    public abstract void conexaoContaPagsOk(List<ContaPags> l) ;
    public abstract void conexaoContaPagsErro(String msg) ;
}
