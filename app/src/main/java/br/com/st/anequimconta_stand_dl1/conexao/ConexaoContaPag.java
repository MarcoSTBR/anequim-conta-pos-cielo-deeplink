package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaPag  extends ConexaoServer {


    public ConexaoContaPag(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaPagPos") ;
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
        Log.i("enviar_","ConexaoContaPag " + s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else conexaoContaPagErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            conexaoContaPagErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ContaPag> l = new ArrayList<ContaPag>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ContaPag(jaa.getJSONObject(i))) ;
        }
        conexaoContaPagOk(l);
    }

    public abstract void conexaoContaPagOk(List<ContaPag> l) ;
    public abstract void conexaoContaPagErro(String msg) ;
}
