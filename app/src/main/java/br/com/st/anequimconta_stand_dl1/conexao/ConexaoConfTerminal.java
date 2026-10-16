package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.Terminal;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoConfTerminal extends ConexaoServer {

    public ConexaoConfTerminal(Activity ctx, String mac) {
        super(ctx);
        method = "GET" ;
        try {
            msg = "Consultando Terminal..." ;
            maps.put("class","AfoodConf_Terminal") ;
            maps.put("method","consultar2") ;
            maps.put("LOJA_ID", UtilSet.getLojaId(ctx)) ;
            maps.put("MAC",mac) ;
            url =  new URL(UtilSet.getServidorMaster(ctx)) ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            erroMsg(0, "Url inválida!");
        }
    }

    public ConexaoConfTerminal(Activity ctx, Terminal t)  {
        super(ctx);
        try {

            msg = "Atualizando Terminal..." ;
            maps.put("class","AfoodConf_Terminal") ;
            maps.put("method","atualizar") ;
            maps.put("terminal",  t.getJson().toString()) ;
            url =  new URL(UtilSet.getServidorMaster(ctx)) ;
        } catch (MalformedURLException  e) {
            e.printStackTrace();
            erroMsg(0, "Url inválida!");
        } catch (JSONException e) {
            e.printStackTrace();
            erroMsg(0, "Erro ao converter dados!");
        }
    }

    public ConexaoConfTerminal(Activity ctx, int loja_id, String terminal, String descricao)  {
        super(ctx);
        try {
            msg = "Atualizando Terminal..." ;
            maps.put("class","AfoodConf_Terminal") ;
            maps.put("method","atualizar2") ;
            maps.put("LOJA_ID",  loja_id) ;
            maps.put("MAC",  terminal) ;
            maps.put("DESCRICAO",  descricao) ;
            url =  new URL(UtilSet.getServidorMaster(ctx)) ;
        } catch (MalformedURLException  e) {
            e.printStackTrace();
            erroMsg(0, "Url inválida!");
        }
    }

    public void executar(){
        super.execute() ;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("onPostExTerminal", codInt+' '+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                Terminal t = new Terminal(j.getJSONObject("data")) ;
                UtilSet.setTerminalId(ctx, t.getId() );
                ok(t) ;
                //ok(j.getJSONObject("data").getInt("id"), j.getJSONObject("data").getString("DESCRICAO"));
            } else {
                erroMsg(codInt, j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            erroMsg(codInt, s) ;
        }

    }

   public abstract void ok(Terminal t) ;
   public abstract void erroMsg(int cod, String msg) ;
}
