package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoContaItemCancelar extends ConexaoServer{

    public ConexaoContaItemCancelar(Activity ctx, JSONArray jaa) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "Enviando Pedidos..." ;
        maps.put("class", "AfoodContaCancelItmPos") ;
        maps.put("method", "requisicao") ;
        maps.put("requisicao", jaa) ;
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
                contaItemCancelarOk(j.getJSONArray("data"));
            } else contaItemCancelarErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contaItemCancelarErro("COD "+codInt+" "+s);
        }
    }

    public abstract void contaItemCancelarOk(JSONArray jsonArray) ;
    public abstract void contaItemCancelarErro(String msg) ;

}
