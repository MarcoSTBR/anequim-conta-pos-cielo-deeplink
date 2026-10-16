package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItem;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaItem extends ConexaoServer{

    public ConexaoContaItem(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaItemPos") ;
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
        Log.i("enviar_","ConexaoContaItem " + s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else contaItemErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contaItemErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ContaItem> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ContaItem(jaa.getJSONObject(i))) ;
        }
        contaItemOk(l);
    }

    public abstract void contaItemOk(List<ContaItem> l) ;
    public abstract void contaItemErro(String msg) ;

}
