package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.ImpressaoRemota;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoRequisicaoRemota extends ConexaoServer{

    public ConexaoRequisicaoRemota(Activity ctx, JSONArray j) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodRequisicaoRemotaPos") ;
        maps.put("method", "requisicao") ;
        maps.put("requisicao", j) ;
        Log.i("enviar_remota",j.toString()) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_remota",s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else reqRemotaErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            reqRemotaErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ImpressaoRemota> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ImpressaoRemota(jaa.getJSONObject(i))) ;
        }
        reqRemotaOk(l);
    }

    public abstract void reqRemotaOk(List<ImpressaoRemota> l) ;
    public abstract void reqRemotaErro(String msg) ;

}
