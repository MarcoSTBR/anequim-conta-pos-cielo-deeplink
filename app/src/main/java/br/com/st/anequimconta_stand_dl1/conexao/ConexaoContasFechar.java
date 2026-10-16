package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContasFechar extends ConexaoServer{

    private ContaMult contaMult ;
    public ConexaoContasFechar(Activity ctx, Caixa caixa, ContaMult contaMult) throws MalformedURLException, JSONException {
        super(ctx);
        this.contaMult = contaMult;
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaFechamentoPos") ;
        maps.put("method", "fechar") ;
        maps.put("caixa", caixa.geJSON()) ;
        maps.put("contaMult", contaMult.geJSON()) ;
        Log.i("fechamento", caixa.geJSON().toString());
        Log.i("fechamento", contaMult.geJSON().toString());
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("fechamento_",s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                contasFechamentoOk(contaMult);
            } else contaFechamentoErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            contaFechamentoErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<Conta> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new Conta(jaa.getJSONObject(i))) ;
        }
//        contasFechamentoOk(l);
    }

    public abstract void contasFechamentoOk(ContaMult contaMult) ;
    public abstract void contaFechamentoErro(String msg) ;

}
