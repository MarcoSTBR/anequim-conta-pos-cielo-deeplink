package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerCloudNFCeCertificado;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoCloudNFceCertificado extends ConexaoCloudNFCe{

    private ListenerCloudNFCeCertificado listenerCloudNFCeCertificado ;

    public ConexaoCloudNFceCertificado(Activity ctx, ListenerCloudNFCeCertificado listenerCloudNFCeCertificado) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        this.listenerCloudNFCeCertificado = listenerCloudNFCeCertificado ;
        token = ConfiguracaoCloudNFceNFCe.getCloudNfce_token(ctx) ;
        url = new URL(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(ctx)+"/certificado") ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("cluodnfce", method+" codigo "+codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getBoolean("sucesso")) {
                listenerCloudNFCeCertificado.ok(j.getString("mensagem"), UtilSet.getData( j.getString("validade")));
            } else listenerCloudNFCeCertificado.erro(j.getInt("codigo"), j.getString("mensagem"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerCloudNFCeCertificado.erro(codInt, s);
        }
    }
}
