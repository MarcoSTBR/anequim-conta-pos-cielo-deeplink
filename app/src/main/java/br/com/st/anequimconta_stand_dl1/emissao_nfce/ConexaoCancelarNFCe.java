package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerCancelarNFce;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultNFCe;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoCancelarNFCe extends ConexaoCloudNFCe {

    private ContaMultNFCe contaMultNFCe;
    private ListenerCancelarNFce listenerCancelarNFce;

    public ConexaoCancelarNFCe(Activity ctx, ContaMultNFCe contaMultNFCe, ListenerCancelarNFce listenerCancelarNFce) throws MalformedURLException {
        super(ctx);
        msg = "Emitindo NFce...";
        method = "POST";
        this.contaMultNFCe = contaMultNFCe;
        this.listenerCancelarNFce = listenerCancelarNFce;
        token = ConfiguracaoCloudNFceNFCe.getCloudNfce_token(ctx);
        url = new URL(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(ctx) + "/nfce/cancela");
    }


    public void executar() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("chave", contaMultNFCe.getChave());
        j.put("justificativa", "Cancelamento efetuado pelo usuário");
        data = j.toString();
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("cloud_nfce", s);
        try {
            JSONObject j = new JSONObject(s);
            if (j.getBoolean("sucesso"))
                listenerCancelarNFce.ok(contaMultNFCe);
            else
                listenerCancelarNFce.erro(j.getString("codigo")+" "+j.getString("mensagem"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerCancelarNFce.erro(s);
        }
    }
}
