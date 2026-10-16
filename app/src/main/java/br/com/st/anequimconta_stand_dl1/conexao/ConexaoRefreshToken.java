package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleRefreshToken;
import br.com.st.anequimconta_stand_dl1.util.TokenSet;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ConexaoRefreshToken extends ConexaoServer {

    ListenerControleRefreshToken listenerRefreshToken ;

    public ConexaoRefreshToken(Activity ctx, ListenerControleRefreshToken listenerRefreshToken) {
        super(ctx);
        this.listenerRefreshToken = listenerRefreshToken ;
        method = "GET" ;
        msg = "Renovação Token" ;
        maps.put("class", "AfoodRefreshToken") ;
//        maps.put("class", "AfoodRefreshTokenV2") ;
        maps.put("method", "refreshtoken") ;
        Log.i("token_", UtilSet.getToken(ctx)) ;
        try {
            url = new URL(UtilSet.getServidorMaster(ctx)) ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
       // super.onPostExecute(s);
        Log.i("token_", "refhesh "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                UtilSet.setToken(ctx, j.getString("data"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    UtilSet.setLojaId(ctx, TokenSet.getLojaId(ctx));
                }
                listenerRefreshToken.ok() ;
            } else listenerRefreshToken.erro(codInt, j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            listenerRefreshToken.erro(codInt,s);
        }
    }


}
