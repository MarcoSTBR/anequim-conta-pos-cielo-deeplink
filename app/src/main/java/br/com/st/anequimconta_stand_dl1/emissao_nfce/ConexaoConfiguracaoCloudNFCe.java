package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoServer;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerConfiguracaoCloudNFCe;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoConfiguracaoCloudNFCe extends ConexaoServer {

    private ListenerConfiguracaoCloudNFCe listenerConfiguracaoCloudNFCe ;


    public ConexaoConfiguracaoCloudNFCe(Activity ctx, ListenerConfiguracaoCloudNFCe listenerConfiguracaoCloudNFCe) throws MalformedURLException {
        super(ctx);
        this.listenerConfiguracaoCloudNFCe = listenerConfiguracaoCloudNFCe ;
        msg = "Recuperando Token" ;
        maps.put("class", "AfoodCloudNFCe" );
        maps.put("method", "loadAll" );
        maps.put("filters", getFilter());
        url =  new URL(UtilSet.getServidorMaster(ctx)) ;
    }
    private JSONArray getFilter(){
        return new JSONArray() ;
    }

    public void getToken(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("clod", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerConfiguracaoCloudNFCe.ok(j.getJSONArray("data").getJSONObject(0).getString("TOKEN")) ;
            } else listenerConfiguracaoCloudNFCe.erro(codInt, j.getString("data")) ;
        } catch (JSONException e) {
            e.printStackTrace();
            listenerConfiguracaoCloudNFCe.erro(codInt, e.getMessage()) ;
        }
    }

}
