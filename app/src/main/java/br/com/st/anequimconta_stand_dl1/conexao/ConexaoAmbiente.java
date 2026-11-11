package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.st.anequimconta_stand_dl1.dao.AcompanhamentoDao;
import br.com.st.anequimconta_stand_dl1.dao.AmbienteDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Acompanhamento;
import br.com.st.anequimconta_stand_dl1.entidade.Ambiente;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public abstract class ConexaoAmbiente extends ConexaoServer{

    private final String TAG = "ambiente" ;
    private Activity ctx ;

    public ConexaoAmbiente(Activity ctx) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Ambiente" ;
        method = "POST" ;
        this.ctx = ctx;
        maps.put("class","AfoodAmbiente") ;
        maps.put("method","loadAll2") ;
        maps.put("filters",getJSON().getJSON());
        maps.put("order", "CODIGO") ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    private DaoFilters getJSON(){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("LOJA_ID", "=", String.valueOf(UtilSet.getLojaId(ctx))));
        return f;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i(TAG, codInt+" "+s) ;
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
        //List<Acompanhamento> l = new ArrayList<Acompanhamento>();
        AmbienteDao.getAmbienteDao(ctx).deleteAll();
        for (int i = 0 ; i < j.length() ; i++) {
            Ambiente a = new Ambiente(j.getJSONObject(i)) ;
            AmbienteDao.getAmbienteDao(ctx).insert(a) ;
            Log.i(TAG, codInt+" "+a.geJSON().toString()) ;
        }
        ok();
    }

    public abstract void ok() ;
    public abstract void erro(String msg) ;
}
