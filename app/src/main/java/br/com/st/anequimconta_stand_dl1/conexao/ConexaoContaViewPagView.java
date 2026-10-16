package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaViewPagView extends ConexaoServer{

    public ConexaoContaViewPagView(Activity ctx, List<String> mults) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodMultPagConsultaPos") ;
        maps.put("method", "consultar") ;
        maps.put("mult_uuids", getJSON(mults)) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public ConexaoContaViewPagView(Activity ctx, Caixa caixa) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodMultPagConsultaPos") ;
        maps.put("method", "consultarCaixa") ;
        maps.put("caixa_uuid", caixa.getUUID()) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }


    private JSONArray getJSON(List<String> mults) {
        JSONArray j = new JSONArray() ;
        for (String s :mults)
            j.put(s) ;
        Log.i("enviar_mult","json:"+j) ;
        return j ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_mult",s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else contaViewPagViewErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contaViewPagViewErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ContaViewPagView> l = new ArrayList<ContaViewPagView>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ContaViewPagView(jaa.getJSONObject(i))) ;
        }
        contaViewPagViewOk(l);
    }

    public abstract void contaViewPagViewOk(List<ContaViewPagView> l) ;
    public abstract void contaViewPagViewErro(String msg) ;

}
