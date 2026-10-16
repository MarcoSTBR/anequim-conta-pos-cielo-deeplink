package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaView extends ConexaoServer{

    public ConexaoContaView(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaViewPos") ;
        maps.put("method", "conta_view") ;
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
        Log.i("ConexaoContaView",s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else contaViewErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contaViewErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ContaView> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ContaView(jaa.getJSONObject(i))) ;
        }
        contaViewOk(l);
    }

    public abstract void contaViewOk(List<ContaView> l) ;
    public abstract void contaViewErro(String msg) ;

}
