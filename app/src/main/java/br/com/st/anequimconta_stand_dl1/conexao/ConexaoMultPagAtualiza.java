package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoMultPagAtualiza extends ConexaoServer{

    public ConexaoMultPagAtualiza(Activity ctx, String mult_uuid) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodMultPagPos") ;
        maps.put("method", "atualiza") ;
        maps.put("mult_uuid", mult_uuid) ;
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
                setJSON(j.getJSONArray("data"));
            } else multPagAtualizaErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            multPagAtualizaErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ContaMult> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ContaMult(jaa.getJSONObject(i))) ;
        }
        multPagAtualiza(l);
    }

    public abstract void multPagAtualiza(List<ContaMult> l) ;
    public abstract void multPagAtualizaErro(String msg) ;

}
