package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;
import android.widget.Toast;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Pedido;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PedidoDao extends Dao {

    private static PedidoDao pedidoDao;

    public static PedidoDao getPedidoDao(Context ctx) {
        if (pedidoDao == null) {
            pedidoDao = new PedidoDao(ctx);
            return pedidoDao;
        } else return pedidoDao;
    }

    public PedidoDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "PEDIDO";
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
        List<DaoCampos> l = new ArrayList<DaoCampos>();
        l.add(new DaoCampos("NUMERO", DaoCampoTipo.dbString, true));
        l.add(new DaoCampos("REFERENCIA", DaoCampoTipo.dbString, true));
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true));
        l.add(new DaoCampos("CORRENTE", DaoCampoTipo.dbInt, true));
        return l;
    }

    public List<Pedido> getList(DaoFilters Filter, String order) {
        List<Pedido> l = new ArrayList<Pedido>();
        try {
            JSONArray jaa = null;
            jaa = findAll(Filter, order);
            for (int i = 0; i < jaa.length(); i++) {
                l.add(new Pedido(jaa.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return l;
    }

    public Pedido getPedidoCorrente() {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CORRENTE", "=", "1"));
        List<Pedido> l = getList(f, "");
        if (l.size() > 0) return l.get(0);
        else return null;
    }

    public Pedido getPedidoID(String uuid) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", uuid));
        List<Pedido> l = getList(f, "");
        if (l.size() > 0) return l.get(0);
        else return null;
    }

    public Pedido getPedidoNumero(String num) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("NUMERO", "=", String.valueOf(num)));
        List<Pedido> l = getList(f, "");
        if (l.size() > 0) return l.get(0);
        else return null;
    }

    public void setPedidoCorrente(String num) {
        try {
            DaoFilters f = new DaoFilters();
            List<Pedido> l = getList(f, "");
            for (Pedido p : l) {
                if (p.getNUMERO().equals(num)) {
                  if (p.getCORRENTE() != 1) {
                     p.setCORRENTE(1);
                     update(p.geJSON());
                  }
                } else {
                    if (p.getCORRENTE() != 0) {
                        p.setCORRENTE(0);
                        update(p.geJSON());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
