package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPags;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaPagsAtualiza extends ConexaoServer{

    public ConexaoContaPagsAtualiza(Activity ctx, ContaMult mult_pag, List<Conta> l) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaPagsPos") ;
        maps.put("method", "atualiza") ;
        maps.put("requisicao", getJON(mult_pag, l)) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    private JSONArray getJON(ContaMult mult_pag, List<Conta> l) throws JSONException {
        JSONArray f = new JSONArray() ;
        for (Conta c :l){
            JSONObject j = new JSONObject() ;
            j.put("CAIXA_UUID", mult_pag.getCAIXA_UUID()) ;
            j.put("CONTA_UUID", c.getUUID()) ;
            j.put("MULT_UUID", mult_pag.getUUID()) ;
            f.put(j) ;
        }
        return f ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_",s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else contaPagsAtualizaErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contaPagsAtualizaErro("ConexaoContaPagsAtualiza "+e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ContaPags> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new ContaPags(jaa.getJSONObject(i))) ;
        }
        contaPagsAtualizaOk(l);
    }

    public abstract void contaPagsAtualizaOk(List<ContaPags> l) ;
    public abstract void contaPagsAtualizaErro(String msg) ;

}
