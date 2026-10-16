package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoContaEdit extends ConexaoServer{

    public ConexaoContaEdit(Activity ctx, Conta conta) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaPos") ;
        maps.put("method", "store2") ;
        maps.put("data", conta.getJSON()) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    private JSONArray getJON() {
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("STATUS", "=", "1"));
        //f.add(new DaoFilter("TERMINAL_ID", "=", String.valueOf(UtilSet.getTerminalId(ctx))));
        return f.getJSON() ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_",s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                contasOk(new Conta(j.getJSONObject("data")));
            } else contasErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contasErro(e.getMessage());
        }
    }
/*
    private void setJSON(JSONArray jaa) throws JSONException {
        List<Conta> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new Conta(jaa.getJSONObject(i))) ;
        }
        contasOk(l);
    }*/

    public abstract void contasOk(Conta conta) ;
    public abstract void contasErro(String msg) ;

}
