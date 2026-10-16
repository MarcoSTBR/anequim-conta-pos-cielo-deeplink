package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoCaixa extends ConexaoServer {

    private int tipo ;
    private Caixa caixa ;

    public ConexaoCaixa(Activity ctx, Caixa caixa) throws MalformedURLException, JSONException {
        super(ctx);
        this.caixa = caixa ;
        method = "POST";
        msg = "Incluindo Caixa";
        maps.put("class", "AfoodCaixaPos");
        maps.put("method", "store2");
        maps.put("data", caixa.geJSON());
        url = new URL(UtilSet.getServidorMaster(ctx));
        tipo = 1 ;
    }

    public ConexaoCaixa(Activity ctx) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST";
        msg = "Incluindo Caixa";
        maps.put("class", "AfoodCaixaPos");
        maps.put("method", "loadAll2");
        maps.put("filters", getCaixaAberto());
        url = new URL(UtilSet.getServidorMaster(ctx));
        tipo = 2 ;
    }

    private JSONArray getCaixaAberto(){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("STATUS", "=", "1"));
        f.add(new DaoFilter("USUARIO_ID", "=", String.valueOf(UtilSet.getUsuarioId(ctx))));
        return f.getJSON() ;

    }

    public void executar() {
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("caixa_enviar", s) ;
        try {
            JSONObject j = new JSONObject(s);
            if (j.getString("status").equals("success")) {
                if (tipo == 1)
                    ok(caixa);
                else setJSON(j.getJSONArray("data"));
            } else erro("COD " + codInt + " " + j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            erro(s);
        }
    }

    private void setJSON(JSONArray j) throws JSONException {
        Caixa c = null ;
        for (int i = 0; i < j.length(); i++) {
            c = new Caixa(j.getJSONObject(i));
        }
        ok(c) ;
    }

    public abstract void ok(Caixa c);

    public abstract void erro(String msg);

}
