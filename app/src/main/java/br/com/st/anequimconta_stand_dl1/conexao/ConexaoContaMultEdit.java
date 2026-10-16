package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoContaMultEdit extends ConexaoServer{

    public ConexaoContaMultEdit(Activity ctx, ContaMult contaMult) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodMultPagPos") ;
        maps.put("method", "store2") ;
        maps.put("data", contaMult.geJSON()) ;
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
                conexaoContaMultEditOk(new ContaMult(j.getJSONObject("data"))) ;
            } else conexaoContaMultEditErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            conexaoContaMultEditErro(e.getMessage());
        }
    }

    public abstract void conexaoContaMultEditOk(ContaMult contaMult) ;
    public abstract void conexaoContaMultEditErro(String msg) ;

}
