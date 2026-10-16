package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.dao.AcompanhamentoProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoProduto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoAcompanhamentoProduto extends ConexaoServer{

    private Activity ctx ;

    public ConexaoAcompanhamentoProduto(Activity ctx) throws MalformedURLException {
        super(ctx);
        msg = "" ;
        method = "POST" ;
        this.ctx = ctx;
        maps.put("class","AfoodAcompanhamentoProduto") ;
        maps.put("method","loadAll") ;
//        maps.put("filters",filters.getJSON()) ;
//        maps.put("order", order) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Acompanhamento Produto", codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data")) ;
            } else erro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro("COD "+codInt+" "+s);
        }
    }

    private void  setJSON(JSONArray j) throws JSONException {
        AcompanhamentoProdutoDao.getAcompanhamentoProdutoDao(ctx).deleteAll();
        for (int i = 0 ; i < j.length() ; i++) {
            AcompanhamentoProdutoDao.getAcompanhamentoProdutoDao(ctx).insert(new AcompanhamentoProduto(j.getJSONObject(i)).geJSON()) ;
        }
        ok() ;
    }

    public abstract void ok() ;
    public abstract void erro(String msg) ;
}
