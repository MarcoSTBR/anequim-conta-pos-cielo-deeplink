package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoProdutos extends ConexaoServer{

    public ConexaoProdutos(Activity ctx){
        super(ctx);
        try {
            msg = "";
            maps.put("class","AfoodProduto") ;
            maps.put("method","consultar") ;
            maps.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
            maps.put("MAC", UtilSet.getSerialNumberMAC(ctx)) ;
            url =  new URL(UtilSet.getServidorMaster(ctx)) ; //Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaProduto).getUrl() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public void executar(){
        super.execute() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Produtos_", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                    setProdutos(j.getJSONArray("data"));
            } else {
                erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro("COD "+codInt+" "+s);
          //  Toast.makeText(ctx, "JSON "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setProdutos(JSONArray j) throws JSONException {
        ProdutoDao.getProdutoDao(ctx).deleteAll();
        List<Produto> l = new ArrayList<Produto>();
        for (int i = 0 ; i < j.length() ; i++) {
            Produto it = new Produto(j.getJSONObject(i)) ;
            ProdutoDao.getProdutoDao(ctx).insert(it.geJSON());
        }
        ok() ;
    }

    public abstract void ok();
    public abstract void erro(String msg);
}
