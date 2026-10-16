package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.dao.ProdutoTicketDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ProdutoTicket;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public abstract class ConexaoProdutoTicke extends ConexaoServer{


    public ConexaoProdutoTicke(Activity ctx) throws MalformedURLException {
        super(ctx);
        method = "POST";
        msg = "";
        maps.put("class", "AfoodContaTicketProdutoPos");
        maps.put("method", "loadAll2");
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CONF_TERMINAL_ID", "=", String.valueOf(UtilSet.getTerminalId(ctx))));
        f.add(new DaoFilter("STATUS", "=", "1"));
        String order = "ID" ;
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
        Log.i("prod_ticket","ticket rec" +s) ;
        super.onPostExecute(s);
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                setJSON(j.getJSONArray("data"));
            } else ticketProdutoErro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            ticketProdutoErro(e.getMessage());
        }
    }

    private void setJSON(JSONArray jaa) throws JSONException {
        List<ProdutoTicket> l = new ArrayList<>() ;
        ProdutoTicketDao.getProdutoTicket(ctx).deleteAll();
        for (int i = 0 ; i < jaa.length() ; i++) {
            ProdutoTicket pt = new ProdutoTicket(jaa.getJSONObject(i)) ;
            l.add(pt) ;
            ProdutoTicketDao.getProdutoTicket(ctx).insert(pt.geJSON());
            Log.i("prod_ticket",l.get(l.size()-1).geJSON().toString()) ;
        }
        ticketProdutoOk(l);
    }

    public abstract void ticketProdutoOk(List<ProdutoTicket> l) ;
    public abstract void ticketProdutoErro(String msg) ;
}
