package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoContaPagEdit extends ConexaoServer {


    public ConexaoContaPagEdit(Activity ctx, ContaPag contaPag) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaPagPos") ;
        maps.put("method", "store2") ;
        maps.put("data", contaPag.geJSON()) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
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
                conexaoContaPagOk(new ContaPag(j.getJSONObject("data")));
            } else conexaoContaPagErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            conexaoContaPagErro("ConexaoContaPagEdit "+e.getMessage());
        }
    }

    public abstract void conexaoContaPagOk(ContaPag pag) ;
    public abstract void conexaoContaPagErro(String msg) ;
}
