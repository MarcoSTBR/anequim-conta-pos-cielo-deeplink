package br.com.st.anequimconta_stand_dl1.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import cielo.sdk.info.InfoManager;

public class UtilSet {

    public static void setToken(Context ctx, String token){
        UtilSet.gravaParamString(ctx, "TOKEN",token) ;
    }
    public static String getToken(Context ctx){
        //return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjbnBqIjoiOTAwMiIsInVzdWFyaW9JZCI6IjciLCJ1c3VhcmlvTm9tZSI6IlN1cG9ydGUgU1RCUiIsImVtYWlsIjoic3Vwb3J0ZUBzdGJyLmNvbS5iciIsImxvamFfaWQiOiIxIiwibG9qYSI6IkRFU01PTlNUUkFcdTAwYzdBTyIsInN0Z2VyZSI6InN0Z2VyZV85MDAyIiwiZXhwIjoxNzEwNzg1MTAyLCJpYXQiOjE3MTA1MjU5MDIsIm5iZiI6MTcxMDUyNTkwMn0.fPDoMpdxX9PXoAsNEWQIEoNvrqzSGieER-TVbD_Xo3Y" ;
        return UtilSet.lerParamString(ctx, "TOKEN") ;
    }
    public static String getCnpj(Context ctx) {
        return lerParamString(ctx,"CNPJ") ;
    }

    public static void setCnpj(Context ctx , String cnpj) {
        gravaParamString(ctx,"CNPJ",cnpj);
    }

    public static String lerParamString(Context ctx, String chave) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        String ret = preference.getString(chave, "");
        return ret;
    }

    public static void gravaParamString(Context ctx, String chave, String valor) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(chave, valor) ;
        editor.commit() ;
    }

    public static String Md5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            //e1.printStackTrace();
            password = "ERRO";
        }
        return password;
    }

    public static String getSerialNumberMAC(Context ctx){
        String serial = "unknown" ;
        InfoManager infoManager = new InfoManager() ;
        serial = infoManager.getSettings(ctx).getLogicNumber() ;

        return serial ;
    }

    public static String repetir(String s, int count){
        String r = "" ;
        for (int i = 0; i < count; i++) {
            r = r + s ;
        }
        return r ;
    }

    public static Date getData(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date() ;
        try {
            d = dateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d ;
    }

    public static void setLojaId(Context ctx, int l) {
        gravaParamString(ctx, "LOJA_ID", Integer.toString(l)) ;
    }
    public static int getLojaId(Context ctx) {
        return Integer.parseInt(lerParamString(ctx, "LOJA_ID")) ;
    }
    public static void setLojaNome(Context ctx, String nome) {
        gravaParamString(ctx, "LOJA_NOME", nome) ;
    }
    public static String getLojaNome(Context ctx) {
        return lerParamString(ctx, "LOJA_NOME") ;
    }
    public static void setLogin(Context ctx, String login) {
        gravaParamString(ctx, "LOGIN", login) ;
    }
    public static String getLogin(Context ctx) {
      return lerParamString(ctx, "LOGIN") ;
    }
    public static void setUsuarioId(Context ctx, int usuario_id) {
        gravaParamString(ctx, "USUARIO_ID", Integer.toString(usuario_id)) ;
    }
    public static int getUsuarioId(Context ctx) {
      if (lerParamString(ctx, "USUARIO_ID").equals(""))
            return 0 ; else
      return Integer.parseInt(lerParamString(ctx, "USUARIO_ID")) ;
    }
    public static void setUsuarioNome(Context ctx, String usuario_nome) {
        gravaParamString(ctx, "USUARIO_NOME", usuario_nome) ;
    }
    public static String getUsuarioNome(Context ctx) {
        return lerParamString(ctx, "USUARIO_NOME") ;
    }
    public static String md5(Context ctx, String st) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(st.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        } catch(UnsupportedEncodingException ex){
        }
        return null;
    }

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString() ;
        return uuid ;
    }
    public static void setServidorMaster(Context ctx, String url_mobile) {
        gravaParamString(ctx, "SERVIDOR_MASTER", url_mobile) ;
    }

    public static String getServidorMaster(Context ctx) {
        String s = lerParamString(ctx, "SERVIDOR_MASTER") ;
        if ((s.isEmpty()) || (s == null)){
            s = "https://gileade.com.br/anequimfood/rest.php" ;
        }
        return s ;
    }

    public static void setTerminalId(Context ctx, int id){
        gravaParamString(ctx, "TERMINAL_ID", Integer.toString(id)) ;
    }
    public static int getTerminalId(Context ctx){
        try {
            return Integer.valueOf(lerParamString(ctx, "TERMINAL_ID")) ;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0 ;
        }
    }
    public static String getDiaSemana(Date data){
        SimpleDateFormat dt = new SimpleDateFormat("EEE", new Locale("pt", "BR")) ;
        return dt.format(data) ;
        /*
        int dia = Integer.parseInt(dt.format(data)) ;
        switch (dia){
            case 1 : return "Dom" ;
            case 2 : return "Seg" ;
            case 3 : return "Ter" ;
            case 4 : return "Qua" ;
            case 5 : return "Qui" ;
            case 6 : return "Sex" ;
            case 7 : return "Sab" ;
            default: return dt.format(data) ;
        }*/
    }
    private static BigDecimal truncateDecimal(final double x, final int numberofDecimals) {
        BigDecimal v = new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_UP);
        //Log.i("BigDecimal", "Valor "+v) ;
        //Math.round(x) ;
        return v ;
    }
    public static Double truncate(double value) {
      //  return truncateDecimal(value, 2).doubleValue() ;
        DecimalFormat df = new DecimalFormat("00000000.00");
        String v = df.format(value).replace(",",".") ;
        Log.i("truncate", "Valor "+v) ;
        return Double.valueOf(v);
    }
    public static int diferencaData(Date d1, Date d2){
        long difference = d2.getTime() - d1.getTime();
        return (int) (difference / (1000 * 60 * 60 * 24));
    }

    public static Double transformarDouble(Double valor){
        String fr = new DecimalFormat("#0.000").format(valor) ;
        BigDecimal cm = new BigDecimal(fr.replace(",","."))
                .setScale(2, RoundingMode.DOWN) ;
        return cm.doubleValue() ;

    }
    public static String getVersao(Context ctx){
        String versao = "Erro" ;
        PackageInfo pInfo = null;
        try {
            pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            versao = pInfo.versionName ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return versao;
//        int verCode = pInfo.versionCode;
    }

    public static int getProximoNumero(Context ctx) {
        int i = Integer.parseInt(new SimpleDateFormat("ddHHmmSSS").format(new Date()));
/*
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        int i = preference.getInt("STONE_ID",0) ;
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("STONE_ID", ++i) ;
        editor.commit() ;
*/
        return i ;
    }
}
