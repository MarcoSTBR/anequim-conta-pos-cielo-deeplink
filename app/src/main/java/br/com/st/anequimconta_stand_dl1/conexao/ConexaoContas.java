package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPags;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContas extends ConexaoServer{

    public ConexaoContas(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaPos") ;
        maps.put("method", "loadAll2") ;
        maps.put("filters", f.getJSON()) ;
        if (order.length() > 0)
            maps.put("order", order) ;

        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public ConexaoContas(Activity ctx, List<ContaPags> l) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaPos") ;
        maps.put("method", "contas") ;
        maps.put("contas", getJON(l)) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    private JSONArray getJON(List<ContaPags> l) throws JSONException {
        JSONArray jaa = new JSONArray() ;
        for (ContaPags c : l){
            JSONObject j = new JSONObject() ;
            j.put("UUID",c.getCONTA_UUID()) ;
            jaa.put(j) ;
        }
        Log.i("ConexaoContas_","jaa " + jaa.toString()) ;
        return jaa;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("ConexaoContas_","ConexaoContas " + s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else contasErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contasErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<Conta> l = new ArrayList<Conta>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new Conta(jaa.getJSONObject(i))) ;
        }
        contasOk(l);
    }

    public abstract void contasOk(List<Conta> l) ;
    public abstract void contasErro(String msg) ;

}
