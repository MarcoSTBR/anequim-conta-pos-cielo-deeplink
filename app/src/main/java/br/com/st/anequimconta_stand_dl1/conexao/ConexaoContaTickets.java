package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ConexaoContaTickets extends ConexaoServer{

    public ConexaoContaTickets(Activity ctx) throws MalformedURLException {
        super(ctx);
        method = "POST" ;
        msg = "" ;
        maps.put("class", "AfoodContaTicketPos") ;
        maps.put("method", "loadAll2") ;
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("STATUS", "=", "0"));
        f.add(new DaoFilter("TERMINAL_ID", "=", String.valueOf(UtilSet.getTerminalId(ctx))));
        maps.put("filters", f.getJSON()) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    public ConexaoContaTickets(Activity ctx, DaoFilters f, String order) throws MalformedURLException {
        super(ctx);
        method = "POST";
        msg = "";
        maps.put("class", "AfoodContaTicketPos");
        maps.put("method", "loadAll2");
        maps.put("filters", f.getJSON());
        if (order.length()>0)
            maps.put("order", order);
        url = new URL(UtilSet.getServidorMaster(ctx));
    }

    public void executar(){
        super.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("enviar_","ticket rec" +s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else ticketErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            ticketErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<PedidoTiket> l = new ArrayList<>() ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new PedidoTiket(jaa.getJSONObject(i))) ;
        }
        ticketOk(l);
    }

    public abstract void ticketOk(List<PedidoTiket> l) ;
    public abstract void ticketErro(String msg) ;

}
