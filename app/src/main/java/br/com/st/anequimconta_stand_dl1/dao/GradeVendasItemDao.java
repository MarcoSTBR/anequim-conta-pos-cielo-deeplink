package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.GradeVendasItem;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class GradeVendasItemDao extends Dao {

    private static GradeVendasItemDao gradeVendasItemDao ;

    public static GradeVendasItemDao getGradeVendasItensDao(Context ctx) {
        if (gradeVendasItemDao == null) {
            gradeVendasItemDao = new GradeVendasItemDao(ctx) ;
            return gradeVendasItemDao ;
        } else return gradeVendasItemDao ;
    }
    public GradeVendasItemDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "GRADE_VENDAS_ITEM";
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
        List<DaoCampos> l = new ArrayList<DaoCampos>() ;
        l.add(new DaoCampos("GRADE_VENDAS_ID", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("PRODUTO_ID", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("PRECO", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("COMISSAO", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true)) ;
        return l;
    }

    public List<GradeVendasItem> getList(DaoFilters filters, String order) throws JSONException {
        List<GradeVendasItem> l = new ArrayList<GradeVendasItem>() ;
        JSONArray jaa = findAll(filters, order) ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new GradeVendasItem(jaa.getJSONObject(i))) ;
        }
        return l ;
    }


}
