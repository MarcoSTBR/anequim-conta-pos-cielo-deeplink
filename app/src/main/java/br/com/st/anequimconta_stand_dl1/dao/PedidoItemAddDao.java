package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAdd;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PedidoItemAddDao extends Dao {

    private static PedidoItemAddDao pedidoItemAddDao;

    public static PedidoItemAddDao getPedidoItemAddDao(Context ctx) {
        if (pedidoItemAddDao == null) {
            pedidoItemAddDao = new PedidoItemAddDao(ctx) ;
            return pedidoItemAddDao;
        } else return pedidoItemAddDao;
    }

    public PedidoItemAddDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "PEDIDO_ITEM_ADD";
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
        l.add(new DaoCampos("PEDIDO_ITEM_UUID", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("PRODUTO_ID", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("QUANTIDADE", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("PRECO", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("DESCONTO", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("COMISSAO", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("VALOR", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("OBS", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("INDICE", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true)) ;
        return l;
    }

    public List<PedidoItemAdd> getList(DaoFilters Filter, String order) {
        List<PedidoItemAdd> l = new ArrayList<PedidoItemAdd>() ;
        try {
            JSONArray jaa = null;
            jaa = findAll(Filter, order);
            for (int i = 0 ; i < jaa.length() ; i++) {
                l.add(new PedidoItemAdd(jaa.getJSONObject(i))) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l ;
    }
}
