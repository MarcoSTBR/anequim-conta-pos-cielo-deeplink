package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoCNPJ extends ConexaoServer{
    private String cnpj ;
    public ConexaoCNPJ(Activity ctx, String cnpj) {
        super(ctx);
        this.cnpj = cnpj;
        token = "" ;
        method = "GET" ;
        maps.put("class", "AfoodCNPJ");
        maps.put("method", "autenticacao");
        maps.put("cnpj", cnpj);
        try {
            url =  new URL(UtilSet.getServidorMaster(ctx)) ;
           // url =  new URL("https://gileade.com.br/anequimfood/rest.php") ;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(0, e.getMessage());
        }
    }

    public void executar(){
        super.execute();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("ConexaoCNPJ", Integer.toString(codInt)+" "+s);
        try {
            Log.i("LOGADO", s) ;
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success"))
            {
                if (j.getJSONArray("data").length() > 0) {
                    JSONObject obj = (JSONObject) j.getJSONArray("data").get(0);
                    UtilSet.setServidorMaster(ctx, obj.getString("URL_MOBILE"));
                    //Dao.getLinkAcessoADO(ctx).iniciarPadrao() ;
                    //oK(cnpj, obj.getString("URL_MOBILE"));
                    oK(cnpj, url.toString());
                } else erro(codInt,"CNPJ "+cnpj+" Inexistente!");
            } else {
                erro(codInt, j.getString("data"));
            }
        } catch (JSONException e) {
            erro(codInt, s);
        }

    }

    public abstract void erro(int cod, String msg) ;
    public abstract void oK(String cnpj, String url) ;
}
