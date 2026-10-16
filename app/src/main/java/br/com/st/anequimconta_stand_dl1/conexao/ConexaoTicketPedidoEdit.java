package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoTicketPedidoEdit extends ConexaoServer{

    public ConexaoTicketPedidoEdit(Activity ctx, JSONObject j) throws MalformedURLException, JSONException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaTicketPos") ;
        maps.put("method", "store2") ;
        maps.put("data", j) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_ticket","ticket "+s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                conexaoPedidoTicketEditOk(new PedidoTiket(j.getJSONObject("data")));
            } else conexaoPedidoTicketEditErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            conexaoPedidoTicketEditErro(e.getMessage());
        }
    }

    public abstract void conexaoPedidoTicketEditOk(PedidoTiket p) ;
    public abstract void conexaoPedidoTicketEditErro(String msg) ;

}
