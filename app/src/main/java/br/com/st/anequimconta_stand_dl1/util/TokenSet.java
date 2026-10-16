package br.com.st.anequimconta_stand_dl1.util;

import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenSet {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getUsuarioId(Context ctx) throws JSONException {

        JWT jwt = getJWT(ctx) ;
        Log.i("jwt", jwt.toString()) ;

        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getInt("usuarioId") ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getUsuarioNome(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getString("usuarioNome") ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getLojaId(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getInt("loja_id") ;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getLojaNome(Context ctx) throws JSONException {
        JSONObject j = new JSONObject(getPayload(ctx)) ;
        return j.getString("loja") ;
    }


    public static String getPayload(Context ctx) {
        //Base64.Decoder base64 = Base64.getDecoder() ;
        String token = TokenSet.getToken(ctx) ;

        String[] arr = token.split("\\.");
        int i = 1 ;
        Log.i("token",token) ;
        for (String a : arr){
            Log.i("token",a) ;
            if (i == 2){
                return new String(Base64.decode(a, 0));
            }
            i++ ;
        }
        return "não encontrou" ;
    }

    public static boolean ifTokenVazio(Context ctx){
        return UtilSet.getToken(ctx).equals("");
    }

    public static String getToken(Context ctx) {
        return "Bearer " + UtilSet.getToken(ctx) ;
    }

    private static JWT getJWT(Context ctx) {
        String t = UtilSet.getToken(ctx) ;
        return new JWT(t);
    }

    public static void validate(Context ctx) throws ExceptionTokenExpirado {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") ;
       // String msg = String.format("Token Expirado! "msg, new String(){}) ;
       // jwt.getExpiresAt() ;
        //Toast.makeText(ctx, String.format("Token Expirado "+jwt.isExpired(0)+", desde " + s.format( jwt.getExpiresAt())), Toast.LENGTH_LONG).show();

        Date df = getTokenValidade(ctx) ;
        Date hj  = new Date() ;

        if (hj.after(df)){
            throw new ExceptionTokenExpirado(String.format("Token Expirado, desde " + s.format(df))) ;
        }
    }

    public static Date getTokenValidade(Context ctx){
        JWT jwt = getJWT(ctx) ;
        return jwt.getExpiresAt() ;
    }

    public static class ExceptionTokenExpirado extends Exception{
        private String msg ;
        public ExceptionTokenExpirado(String msg){
            this.msg = msg ;
        }

        @Nullable
        @Override
        public String getMessage(){
            return msg ;
        }
    } ;
}
