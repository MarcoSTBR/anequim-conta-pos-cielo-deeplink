package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.dao.GradeVendasDao;
import br.com.st.anequimconta_stand_dl1.dao.GradeVendasItemDao;
import br.com.st.anequimconta_stand_dl1.entidade.GradeVendas;
import br.com.st.anequimconta_stand_dl1.entidade.GradeVendasItem;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoGradeVendas extends ConexaoServer{

    private Activity ctx ;

    public ConexaoGradeVendas(Activity ctx) throws MalformedURLException {
        super(ctx);
        msg = "" ;
        method = "POST" ;
        this.ctx = ctx;
        maps.put("class","AfoodGradeVendas") ;
        maps.put("method","consultar") ;
        maps.put("MAC", UtilSet.getSerialNumberMAC(ctx));
//        maps.put("filters",filters.getJSON()) ;
//        maps.put("order", order) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("grade_vendas", codInt+" "+s) ;
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
        GradeVendasDao.getGradeVendasDao(ctx).deleteAll();
        GradeVendasItemDao.getGradeVendasItensDao(ctx).deleteAll();
        for (int i = 0 ; i < j.length() ; i++) {
            JSONObject g = j.getJSONObject(i) ;
            GradeVendasDao.getGradeVendasDao(ctx).insert(new GradeVendas(g).geJSON());
            for  (int x = 0 ; x < g.getJSONArray("ITENS").length() ; x++) {
                JSONObject item = g.getJSONArray("ITENS").getJSONObject(x) ;
                GradeVendasItemDao.getGradeVendasItensDao(ctx).insert(new GradeVendasItem(item).geJSON());
            }
        }
        ok() ;
    }

    public abstract void ok() ;
    public abstract void erro(String msg) ;
}
