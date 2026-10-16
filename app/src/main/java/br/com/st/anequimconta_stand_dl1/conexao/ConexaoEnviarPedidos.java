package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoEnviarPedidos extends ConexaoServer{

    public ConexaoEnviarPedidos(Activity ctx, JSONArray jaa) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "Enviando Pedidos..." ;
        maps.put("class", "AfoodRequisicaoPos") ;
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
                envPedidosOk(j.getJSONArray("data"));
            } else anvPedidosErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            anvPedidosErro("COD "+codInt+" "+s);
        }
    }

    public abstract void envPedidosOk(JSONArray jsonArray) ;
    public abstract void anvPedidosErro(String msg) ;

}
