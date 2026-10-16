package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoModalidadePos extends ConexaoServer{

    public ConexaoModalidadePos(Activity ctx){
        super(ctx);
        try {
            msg = "Atualizando Modalidade...";
            maps.put("class","AfoodModalidadePOS") ;
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
        f.add(new DaoFilter("LOJA_ID", "=", String.valueOf(UtilSet.getLojaId(ctx))));
        f.add(new DaoFilter("TERMINAL", "=", UtilSet.getSerialNumberMAC(ctx)));
        f.add(new DaoFilter("STATUS", "=", "1"));
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
        List<Modalidade> list = new ArrayList<>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            list.add(new Modalidade(j.getJSONObject(i)));
        }
        ok(list) ;
    }

    public abstract void ok(List<Modalidade> l);
    public abstract void erro(String msg);
}
