package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerImprimirNFCe;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultNFCe;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConexaoBuscarNFCeCloudNFCe extends ConexaoCloudNFCe{

    private ListenerImprimirNFCe listenerImprimirNFCe ;

    public ConexaoBuscarNFCeCloudNFCe(Activity ctx, ContaMultNFCe contaMultNFCe, ListenerImprimirNFCe listenerImprimirNFCe)  throws MalformedURLException, JSONException {
        super(ctx);
        method = "GET" ;
        this.listenerImprimirNFCe = listenerImprimirNFCe;
        token = ConfiguracaoCloudNFceNFCe.getCloudNfce_token(ctx) ;
        url =  new URL(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(ctx)+"/nfce/"+contaMultNFCe.getChave()) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("imprimirNFce", s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getBoolean("sucesso")) {
                String jxml = j.getString("xml") ;
                byte[] bt = Base64.decode(jxml.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
                String xml = new String(bt);
                Log.i("imprimirNFce", xml) ;
                listenerImprimirNFCe.ok(xml);
            } else listenerImprimirNFCe.erro(j.getString("codigo")+" "+j.getString("mensagem"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerImprimirNFCe.erro(s);
        }

    }
}
