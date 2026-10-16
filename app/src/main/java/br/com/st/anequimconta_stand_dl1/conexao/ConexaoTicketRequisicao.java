package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoTicketRequisicao extends ConexaoServer{

    public ConexaoTicketRequisicao(Activity ctx, JSONArray req) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaTicketPos") ;
        maps.put("method", "requisicao") ;
        maps.put("requisicao", req) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_","ticket "+s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                envReqTicketOk(j.getJSONArray("data"));
            } else envReqTicketErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            envReqTicketErro(e.getMessage());
        }
    }

    public abstract void envReqTicketOk(JSONArray jsonArray) ;
    public abstract void envReqTicketErro(String msg) ;

}
