package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.util.TokenSet;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConexaoCloudNFCe extends AsyncTask<String, String, String> {

    public Activity ctx;
    public String msg;
    public int codInt = 0;
    public Map<String, Object> maps;
    public URL url;
    public String token;
    public String method;
    public ProgressDialog progressDialog;
    public String data = null ;

    public ConexaoCloudNFCe(Activity ctx) {
        this.ctx = ctx;
        //this.maps = new HashMap<String, Object>();
        maps = new LinkedHashMap<>();
        msg = "Aguarde...";
        token = TokenSet.getToken(this.ctx);
        method = "GET";
        data = null ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle(msg);
        progressDialog.setCancelable(true);
       // progressDialog.show();
    }



    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
       // RequestBody body = RequestBody.create(mediaType, "");
       // RequestBody body = null;
       // if (data != null)
       //    body = RequestBody.create("", data);
        Request request = null ;

        if (method.equals("GET")) {
            request = new Request.Builder()
                    // .url("https://hom-api.integranotas.com.br/v1/certificado")
                    .url(url.toString())
//                .method(method, body)
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type", "application/json")
                    .build();
        }
        if (method.equals("POST")) {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
            request = new Request.Builder()
                    .url(url.toString())
                    .post(body)
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type", "application/json")
                    .build();

        }
        try {
            Response response = client.newCall(request).execute();
            return response.body().string() ;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage() ;
        }
    }

/*

    @Override
    protected String doInBackground(String... strings) {
        String resposta = "";
        String urlget = "";
        try {
            if (url == null) throw new Exception("Link inválido!");
            StringBuilder postData = new StringBuilder();
            Log.i("maps", "size() " + maps.size());
            Log.i("maps", "toString() " + maps.toString());
            for (Map.Entry m : maps.entrySet()) {
                if (urlget.length() != 0) urlget = urlget + "&";
                urlget = urlget + m.getKey() + "=" + URLEncoder.encode(String.valueOf(m.getValue()), "UTF-8");
                if (postData.length() != 0) postData.append("&");
                postData.append(URLEncoder.encode(String.valueOf(m.getKey()), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(String.valueOf(m.getValue()), "UTF-8"));
                Log.i("parameters", m.getKey() + "=" + m.getValue());
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            Log.i("parameters", postData.toString());
            Log.i("url_u", url.toString());
            if (method.equals("GET")) {
                String nurl = null;
                if (urlget.length() > 0)
                    nurl = url.toString() + "?" + urlget;
                else nurl = url.toString();
                url = new URL(nurl);
            }
            Log.i("url_u", url.toString());
            //System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  //abre conexao
            connection.setRequestMethod(method);
            //connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.addRequestProperty("User-Agent", "Chrome/56.0.0.0 Mobile");

            //connection.setDefaultUseCaches(false);
            //connection.addRequestProperty("User-Agent", "Mozilla");
            //   connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");
            //   connection.setRequestProperty("http.agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");

            if (method.equals("GET")) // || (method.equals("POST")))
                connection.setRequestProperty("Content-Type", "application/json"); //fala o que vai mandar
            // connection.setRequestProperty("Accept-Encoding", "identity");
            if (!token.equals("")) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
                Log.i("token_", token);
            }

            if (method.equals("POST")) {
                connection.setDoInput(true);
                if (postData.toString().length() > 0) {

                    connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    //  if (method.equals("POST"))
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(postDataBytes);
                    //connection.getOutputStream().flush();
                }
            }
            Log.i("Urlmethod", method);
            Log.i("Url", url.toString());
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            codInt = connection.getResponseCode();
            Log.i("Url", "getHead "+connection.getHeaderFields().toString());

*/
/*
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream(), "UTF-8"));
*/
/*

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String decodedString = "";
            while ((decodedString = in.readLine()) != null) {
                resposta = resposta + decodedString;
            }
            in.close();
            // Log.i("decodedString",decodedString) ;
            connection.disconnect();

        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.i("ProtocolException", codInt + " " + e.getMessage());
            resposta = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("IOException", codInt + " " + e.getMessage());
            resposta = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Exception", codInt + " " + e.getMessage());
            resposta = e.getMessage();
        }
        Log.i("decodedString", codInt + " " + resposta);
        if (codInt == 401) resposta = "Token Expirado!";
        if (codInt == 0) resposta = "Sem conexão com Servidor " + resposta;

        return resposta;
    }
*/

    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
        Log.i("onPostExecute", msg + " cancelled ");
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        Log.i("onPostExecute", msg + " cod " + codInt + " " + s);
        if (codInt == 0) s = "Sem Conexão com o Servidor! \n"+s;
    }
}
