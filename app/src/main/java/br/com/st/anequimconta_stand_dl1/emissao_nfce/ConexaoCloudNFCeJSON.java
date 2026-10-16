package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoServer;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerCloudNFCeJSON;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultDest;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoCloudNFCeJSON extends ConexaoServer {
    private Activity ctx ;
    private ListenerCloudNFCeJSON listenerCloudNFCeJSON ;

    public ConexaoCloudNFCeJSON(Activity ctx, ContaMultDest contaMultDest, ContaMult contaMult, ListenerCloudNFCeJSON listenerCloudNFCeJSON) throws JSONException, MalformedURLException {
        super(ctx);
        this.ctx = ctx;
        this.listenerCloudNFCeJSON = listenerCloudNFCeJSON ;
        maps.put("class","AfoodCloudNFCeJSON") ;
        maps.put("method","loadJSON") ;
        maps.put("terminal", UtilSet.getSerialNumberMAC(ctx)) ;
        maps.put("usuario", UtilSet.getUsuarioId(ctx)) ;
        maps.put("serie", String.valueOf(ConfiguracaoCloudNFceNFCe.getCloudNfce_serie(ctx))) ;
        maps.put("numero", String.valueOf(ConfiguracaoCloudNFceNFCe.getCloudNfce_numero(ctx)+1)) ;
        maps.put("ambiente", String.valueOf(ConfiguracaoCloudNFceNFCe.getOpcaoAmbiente(ctx))) ;
        maps.put("loja_id", String.valueOf(UtilSet.getLojaId(ctx))) ;
        if (contaMult != null)
            maps.put("uuid", contaMult.getUUID()) ;
        if (contaMultDest != null){
            maps.put("dest",contaMultDest.geJSON()) ;
        }
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("conexaocloudjson", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                listenerCloudNFCeJSON.OnJSON(j.getJSONObject("data")) ;
            } else listenerCloudNFCeJSON.OnErroJSON(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerCloudNFCeJSON.OnErroJSON("COD "+codInt+" "+s);
        }
    }
}
