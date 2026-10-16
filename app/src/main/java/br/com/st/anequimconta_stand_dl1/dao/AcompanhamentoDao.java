package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;
import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Acompanhamento;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AcompanhamentoDao extends Dao {

    private static AcompanhamentoDao acompanhamentoDao ;

    public static AcompanhamentoDao getAcompanhamentoDao(Context ctx) {
        if (acompanhamentoDao == null) {
            acompanhamentoDao = new AcompanhamentoDao(ctx) ;
            return acompanhamentoDao ;
        } else return acompanhamentoDao ;
    }

    public AcompanhamentoDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "ACOMP";
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
        l.add(new DaoCampos("DESCRICAO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("OBS", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("MIN", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("MAX", DaoCampoTipo.dbInt, false)) ;
        l.add(new DaoCampos("PRECO", DaoCampoTipo.dbDecimal, true)) ;
        return l ;
    }

    public List<Acompanhamento> getList(DaoFilters filters, String order) throws JSONException {
        List<Acompanhamento> l = new ArrayList<Acompanhamento>() ;
        JSONArray jaa = findAll(filters, order) ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new Acompanhamento(jaa.getJSONObject(i))) ;
        }
        return l ;
    }

    public List<Acompanhamento> getAcompID(int id){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("id", "=", String.valueOf(id)));
        try {
            return  getList(f, "") ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }

}
