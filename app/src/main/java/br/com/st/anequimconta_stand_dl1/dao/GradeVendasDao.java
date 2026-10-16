package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;
import android.widget.Toast;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.GradeVendas;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GradeVendasDao extends Dao {

    private static GradeVendasDao gradeVendasDao;

    public static GradeVendasDao getGradeVendasDao(Context ctx) {
        if (gradeVendasDao == null) {
            gradeVendasDao = new GradeVendasDao(ctx);
            return gradeVendasDao;
        } else return gradeVendasDao;
    }

    public GradeVendasDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "GRADE_VENDAS";
    }

    @Override
    public DaoPrimaryKeyTipo getTipoPrimaryKey() {
        return DaoPrimaryKeyTipo.tpkNormal;
    }

    @Override
    public String getID() {
        return "id";
    }

    @Override
    public List<DaoCampos> getCampos() {
        List<DaoCampos> l = new ArrayList<DaoCampos>();
        l.add(new DaoCampos("DESCRICAO", DaoCampoTipo.dbString, true));
        l.add(new DaoCampos("IMAGEM", DaoCampoTipo.dbString, true));
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true));
        return l;
    }

    public List<GradeVendas> getList(DaoFilters filters, String order) {
        List<GradeVendas> l = new ArrayList<GradeVendas>();
        try {
            JSONArray jaa = findAll(filters, order);
            for (int i = 0; i < jaa.length(); i++) {
                l.add(new GradeVendas(jaa.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return l;
    }

    public GradeVendas GradeID(int id) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("id", "=", String.valueOf(id)));
        List<GradeVendas> l = null;
        l = getList(f, "");
        if (l.size() > 0) {
            return l.get(0);
        } else return null;
    }
}
