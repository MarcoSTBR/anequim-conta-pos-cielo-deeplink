package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

public class PedidoTicketDao extends Dao {

    private static PedidoTicketDao pedidoTicketDao ;

    public static PedidoTicketDao getPedidoTicketDao(Context ctx) {
        if (pedidoTicketDao == null) {
            pedidoTicketDao = new PedidoTicketDao(ctx) ;
            return pedidoTicketDao ;
        } else return pedidoTicketDao ;
    }

    public PedidoTicketDao(Context ctx){
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "PEDIDO_TICKET";
    }

    @Override
    public DaoPrimaryKeyTipo getTipoPrimaryKey() {
        return DaoPrimaryKeyTipo.tpkNormal;
    }

    @Override
    public String getID() {
        return "UUID";
    }

    @Override
    public List<DaoCampos> getCampos() {
        List<DaoCampos> l = new ArrayList<DaoCampos>() ;
        l.add(new DaoCampos("CONTA_ITEM_UUID", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("TERMINAL_ID", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("DATA", DaoCampoTipo.dbDateTime, true)) ;
        l.add(new DaoCampos("USUARIO_ID", DaoCampoTipo.dbInt, false)) ;
        l.add(new DaoCampos("USUARIO_F_ID", DaoCampoTipo.dbInt, false)) ;
        l.add(new DaoCampos("DATA_FECHAMENTO", DaoCampoTipo.dbDateTime, true)) ;
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true)) ;
        return l ;
    }

    public List<PedidoTiket> getList(DaoFilters f, String order){
        List<PedidoTiket> l = new ArrayList<PedidoTiket>() ;
        try {
            JSONArray jaa = null;
            jaa = findAll(f, order);
            for (int i = 0 ; i < jaa.length() ; i++) {
                Log.i("pedtickt", jaa.getString(i).toString()) ;
                l.add(new PedidoTiket(jaa.getJSONObject(i))) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l ;
    }

    public List<PedidoTiket> getPedidoTicketStatus(int s){
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("STATUS", "=", String.valueOf(s)));
        return getList(f, "") ;
    }
}
