package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.Usuario;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoUsuarioLogin extends ConexaoServer{

    public ConexaoUsuarioLogin(Activity ctx, String login, String senha) throws MalformedURLException {
        super(ctx);
        method = "GET" ;
        msg = "Consultando Usuários" ;
        maps.put("class", "AfoodUsuarioLoginPos") ;
        maps.put("method", "logar") ;
        maps.put("login", login) ;
        maps.put("senha", senha) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else erro("COD "+codInt+" "+j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro("COD "+codInt+" "+s);
        }
    }

    private void setJSON(JSONArray j) throws JSONException {
        if (j.length() > 0) {
            Usuario u = new Usuario(j.getJSONObject(0))  ;
            if (u.getSTATUS() != 1)
                erro("Operador Bloqueado!");
            else oK(u);
        } else erro("Operador Não Encontrado!");
    }

    public abstract void oK(Usuario usu) ;
    public abstract void erro(String msg) ;

}
