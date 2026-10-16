package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoModalidade extends ConexaoServer{

    public ConexaoModalidade(Activity ctx){
        super(ctx);
        try {
            msg = "";
            maps.put("class","AfoodModalidade") ;
            maps.put("method","loadAll2") ; //methodo resinscrito loaadAll
            maps.put("filters",getJSON()) ;
            url =  new URL(UtilSet.getServidorMaster(ctx)) ; //Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaProduto).getUrl() ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    private JSONArray getJSON(){
        DaoFilters f = new DaoFilters() ;
  //      f.add(new DaoFilter("TERMINAL", "=", UtilSet.getMAC(ctx)));
        f.add(new DaoFilter("LOJA_ID", "=", String.valueOf(UtilSet.getLojaId(ctx))));
        Log.i("Modalidade_", f.getJSON().toString()) ;
        return f.getJSON() ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Modalidade_", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                    setJSON(j.getJSONArray("data"));
            } else {
                erro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erro("COD "+codInt+" "+s);
        }
    }

    private void setJSON(JSONArray j) throws JSONException {
        ModalidadeDao.getModalidadeDao(ctx).deleteAll();
        for (int i = 0 ; i < j.length() ; i++) {
            ModalidadeDao.getModalidadeDao(ctx).insert(new Modalidade(j.getJSONObject(i)).geJSON());
        }
        ok() ;
    }

    public abstract void ok();
    public abstract void erro(String msg);
}
