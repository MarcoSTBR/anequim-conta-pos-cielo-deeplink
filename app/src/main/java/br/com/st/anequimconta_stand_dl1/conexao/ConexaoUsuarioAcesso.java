package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoUsuarioAcesso extends ConexaoServer{
    private int usuario_id  ;
    private int codigo_acesso ;
    public ConexaoUsuarioAcesso(Activity ctx, int usuario_id, int codigo_acesso) throws MalformedURLException, JSONException {
        super(ctx);
        this.usuario_id = usuario_id ;
        this.codigo_acesso = codigo_acesso ;
        msg = "Consulta Acessos" ;
        maps.put("class","AfoodUsuarioAcesso") ;
        maps.put("method","loadAll2") ;
        maps.put("filters",getJSON()) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }
    private JSONArray getJSON() throws JSONException {
        JSONArray jaa = new JSONArray() ;
        jaa.put(new JSONArray("[\"ACESSO_USUARIO_ID\",\"=\",\""+String.valueOf(usuario_id)+"\"]")) ;
        jaa.put(new JSONArray("[\"ACESSO_DESCRICAO_ID\",\"=\",\""+String.valueOf(codigo_acesso)+"\"]")) ;
        Log.i("acesso", "filtro "+jaa.toString()) ;
        return jaa ;
    }

    public void executar(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("usuarioAcesso", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro("COD "+codInt+" "+s);
        }
    }
    private void setJSON(JSONArray j) throws JSONException {
        boolean encontrado = false ;
        for (int i = 0 ; i < j.length() ; i++) {
            encontrado = true ;
        }
        ok(encontrado) ;
    }
    public abstract void ok(boolean encontrado) ;
    public abstract void erro(String msg) ;
}
