package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoItem;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AcompanhamentoItemDao extends Dao {

    private static AcompanhamentoItemDao acompanhamentoItemDao ;

    public static AcompanhamentoItemDao getAcompanhamentoItemDao(Context ctx) {
        if (acompanhamentoItemDao == null) {
            acompanhamentoItemDao = new AcompanhamentoItemDao(ctx) ;
            return acompanhamentoItemDao ;
        } else return acompanhamentoItemDao ;
    }

    public AcompanhamentoItemDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "ACOMP_ITEM";
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
        l.add(new DaoCampos("AFOOD_ACOMP_ID", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("AFOOD_PRODUTO_ID", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("OBS", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("MIN", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("MAX", DaoCampoTipo.dbInt, false)) ;
        l.add(new DaoCampos("PRECO", DaoCampoTipo.dbDecimal, true)) ;
        return l ;
    }

    public List<AcompanhamentoItem> getList(DaoFilters filters, String order) throws JSONException {
        List<AcompanhamentoItem> l = new ArrayList<AcompanhamentoItem>() ;
        JSONArray jaa = findAll(filters, order) ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new AcompanhamentoItem(jaa.getJSONObject(i))) ;
        }
        return l ;
    }

    public List<AcompanhamentoItem> getlistAcomItem(int acomp_id){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("AFOOD_ACOMP_ID", "=", String.valueOf(acomp_id)));
        try {
            return  getList(f, "") ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }

    }
}
