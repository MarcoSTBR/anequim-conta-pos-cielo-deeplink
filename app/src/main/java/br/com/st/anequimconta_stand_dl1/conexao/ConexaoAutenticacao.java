package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import br.com.st.anequimconta_stand_dl1.util.TokenSet;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoAutenticacao extends ConexaoServer {

    private String cnpj ;
    private String login ;
    private String password ;

    public ConexaoAutenticacao(Activity ctx , String cnpj, String login, String password) throws MalformedURLException {
        super(ctx);
        //method = "POST" ;
        msg = "Autenticação";
        token = "" ;
        this.cnpj = cnpj;
        this.login = login;
        this.password = password ;
        maps.put("class", "AfoodAutenticacao2");
        maps.put("method", "autenticacao");
        maps.put("cnpj", this.cnpj);
        maps.put("login", this.login);
        maps.put("password", this.password);
        Log.i("login_", "cnpj "+this.cnpj) ;
        Log.i("login_", "login "+this.login) ;
        Log.i("login_", "password "+UtilSet.md5(ctx, this.password)) ;

        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fAutenticacao).getUrl();
    }

    public void executar(){
        super.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("ConexaoAutenticacarest","Cod "+codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                String token = j.optJSONObject("data").getString("access_token") ;
                UtilSet.setToken(ctx, token);
                UtilSet.setLojaId(ctx, TokenSet.getLojaId(ctx));
                UtilSet.setLojaNome(ctx, TokenSet.getLojaNome(ctx));
                UtilSet.setCnpj(ctx, cnpj);
                Log.i("ConexaoAutenticacarest", "UtilSet.getToken "+UtilSet.getToken(ctx)) ;
                Log.i("ConexaoAutenticacarest", "UtilSet.setLojaId "+UtilSet.getLojaId(ctx)) ;
                Log.i("ConexaoAutenticacarest", "UtilSet.setLojaNome "+UtilSet.getLojaNome(ctx)) ;
                Log.i("ConexaoAutenticacarest", "UtilSet.setCnpj "+UtilSet.getCnpj(ctx)) ;
                oK(cnpj, login);
                } else erro(codInt, j.getString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
                erro(codInt, s);
            }
    }

    public abstract void oK(String cnpj, String login) ;
    public abstract void erro(int cod, String msg) ;

}
