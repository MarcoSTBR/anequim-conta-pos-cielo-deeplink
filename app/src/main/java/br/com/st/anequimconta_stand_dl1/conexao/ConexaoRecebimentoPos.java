package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPos;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoRecebimentoPos extends ConexaoServer{


    public ConexaoRecebimentoPos(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Recebimento...";
        maps.put("class","AfoodRecebimentoPos") ;
        maps.put("method","loadAll2") ; //method resinscrito loaadAll
        maps.put("filters",f.getJSON()) ;
        if (order.length() > 0)
            maps.put("order",order) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaProduto).getUrl() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Recebimento_pos", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                    setJSON(j.getJSONArray("data"));
            } else {
                recebimentoPosErro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            recebimentoPosErro("COD "+codInt+" "+s);
        }
    }

    private void setJSON(JSONArray j) throws JSONException {
        List<RecebimentoPos> l = new ArrayList<>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            l.add(new RecebimentoPos(j.getJSONObject(i)));
        }
        recebimentoPosOk(l) ;
    }

    public abstract void recebimentoPosOk(List<RecebimentoPos> l);
    public abstract void recebimentoPosErro(String msg);
}
