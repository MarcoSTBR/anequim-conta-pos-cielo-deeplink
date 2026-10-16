package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerCloudNFCeJSON;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerEmitirNFce;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultDest;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultNFCe;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoEmitirNFCeCloudNFCe extends ConexaoCloudNFCe implements ListenerCloudNFCeJSON {

    private ListenerEmitirNFce listenerEmitirNFce;
    private ContaMult contaMult;
    private ContaMultDest contaMultDest ;

    public ConexaoEmitirNFCeCloudNFCe(Activity ctx, ContaMult contaMult, ContaMultDest contaMultDest, ListenerEmitirNFce listenerEmitirNFce) throws MalformedURLException {
        super(ctx);
        msg = "Emitindo NFce...";
        method = "POST" ;
        this.listenerEmitirNFce = listenerEmitirNFce;
        this.contaMult = contaMult;
        this.contaMultDest = contaMultDest;
        token = ConfiguracaoCloudNFceNFCe.getCloudNfce_token(ctx) ;
        url =  new URL(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(ctx)+"/nfce") ;
    }

    public void executar(){
            getJSON() ;
           // getLoja() ;
    }

    private void getJSON() {
        try {
           new ConexaoCloudNFCeJSON(ctx, contaMultDest, contaMult, this).execute();
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEmitirNFce.erro("Conversão "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("cloud_nfce", s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getBoolean("sucesso")) {
                    ConfiguracaoCloudNFceNFCe.setCloudNfce_numero(ctx, j.getInt("numero"));
                   // setVendaNFCe(cNfce);
            } else {

                String msg = j.getString("mensagem") ;
                if (!j.isNull("erros")) {
                    msg = msg + " "+ j.getJSONObject("erros").getString("campo") + " " +
                            j.getJSONObject("erros").getString("erro") + " " +
                            j.getJSONObject("erros").getString("descricao");
                }
                listenerEmitirNFce.erro(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerEmitirNFce.erro(s);
        }
    }

    private void setVendaNFCe(ContaMultNFCe it){
           // VendaVistaNFCeDao.getVendaVistaNFCeDao(ctx).insert(it.geJSON());
            listenerEmitirNFce.ok(it);
    }


    @Override
    public void OnJSON(JSONObject j) {
        maps.put("data", j) ;
        execute();
    }

    @Override
    public void OnErroJSON(String msg) {
        listenerEmitirNFce.erro(msg);
    }
}
