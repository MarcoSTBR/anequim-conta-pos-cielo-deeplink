package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPosPix;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoRecebimentoPixPos extends ConexaoServer{

    public ConexaoRecebimentoPixPos(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        msg = "Atualizando Modalidade...";
        maps.put("class","AfoodRecebimentoPixPos") ;
        maps.put("method","loadAll2") ; //methodo resinscrito loaadAll
        maps.put("filters",f.getJSON()) ;
        if (order.length() > 0)
            maps.put("order",order) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Recebimento_pix_pos", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                    setJSON(j.getJSONArray("data"));
            } else {
                recebimentoPixPosErro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            recebimentoPixPosErro("COD "+codInt+" "+s);
        }
    }

    private void setJSON(JSONArray j) throws JSONException {
        List<RecebimentoPosPix> l = new ArrayList<>() ;
        for (int i = 0 ; i < j.length() ; i++) {
            l.add(new RecebimentoPosPix(j.getJSONObject(i)));
        }
        recebimentoPixPosOk(l) ;
    }

    public abstract void recebimentoPixPosOk(List<RecebimentoPosPix> l);
    public abstract void recebimentoPixPosErro(String msg);
}
