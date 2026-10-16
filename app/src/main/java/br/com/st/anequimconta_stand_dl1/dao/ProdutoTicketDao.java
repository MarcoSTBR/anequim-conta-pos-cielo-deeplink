package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ProdutoTicket;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

public class ProdutoTicketDao extends Dao {

    private static ProdutoTicketDao produtoTicketDao;

    public static ProdutoTicketDao getProdutoTicket(Context ctx){
        if (produtoTicketDao == null) {
            produtoTicketDao = new ProdutoTicketDao(ctx);
            return produtoTicketDao;
        } else return produtoTicketDao;
    }

    public ProdutoTicketDao(Context ctx){
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "TICKET_ITEM";
    }

    @Override
    public DaoPrimaryKeyTipo getTipoPrimaryKey() {
        return DaoPrimaryKeyTipo.tpkNormal;
    }

    @Override
    public String getID() {
        return "ID";
    }

    @Override
    public List<DaoCampos> getCampos() {
        List<DaoCampos> l = new ArrayList<DaoCampos>() ;
        l.add(new DaoCampos("PRODUTO_ID", DaoCampoTipo.dbInt, true)) ;
        return l ;
    }

    public List<ProdutoTicket> getList(DaoFilters f, String order){
        List<ProdutoTicket> l = new ArrayList<ProdutoTicket>() ;
        try {
            JSONArray jaa = null;
            jaa = findAll(f, order);
            for (int i = 0 ; i < jaa.length() ; i++) {
                l.add(new ProdutoTicket(jaa.getJSONObject(i))) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l ;
    }

    public ProdutoTicket getProdutoTiket(int id){
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("PRODUTO_ID", "=", String.valueOf(id)));
        List<ProdutoTicket> l = getList(f, "") ;
        if (l.size() > 0) return l.get(0) ;
         else  return null ;
    }


}
