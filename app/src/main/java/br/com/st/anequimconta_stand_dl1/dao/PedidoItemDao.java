package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;
import android.widget.Toast;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PedidoItemDao extends Dao {

    private static PedidoItemDao pedidoItemDao;

    public static PedidoItemDao getPedidoItemDao(Context ctx) {
        if (pedidoItemDao == null) {
            pedidoItemDao = new PedidoItemDao(ctx) ;
            return pedidoItemDao;
        } else return pedidoItemDao;
    }

    public PedidoItemDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "PEDIDO_ITEM";
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
        l.add(new DaoCampos("PEDIDO_UUID", DaoCampoTipo.dbString, true)) ;
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

    public List<PedidoItem> getList(DaoFilters Filter, String order) {
        List<PedidoItem> l = new ArrayList<PedidoItem>() ;
        try {
            JSONArray jaa = null;
            jaa = findAll(Filter, order);
            for (int i = 0 ; i < jaa.length() ; i++) {
                l.add(new PedidoItem(jaa.getJSONObject(i))) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return l ;
    }

    public PedidoItem getPedidoItemID(String uuid){
        try {
            return new PedidoItem(get(uuid)) ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }

}
