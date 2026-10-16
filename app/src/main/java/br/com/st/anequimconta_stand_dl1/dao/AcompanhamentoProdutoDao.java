package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoProduto;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AcompanhamentoProdutoDao extends Dao {

    private static AcompanhamentoProdutoDao acompanhamentoProdutoDao ;

    public static AcompanhamentoProdutoDao getAcompanhamentoProdutoDao(Context ctx) {
        if (acompanhamentoProdutoDao == null) {
            acompanhamentoProdutoDao = new AcompanhamentoProdutoDao(ctx) ;
            return acompanhamentoProdutoDao ;
        } else return acompanhamentoProdutoDao ;
    }

    public AcompanhamentoProdutoDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "ACOMP_PROD";
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
        l.add(new DaoCampos("AFOOD_PRODUTO_ID", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("AFOOD_ACOMP_ID", DaoCampoTipo.dbInt, true)) ;
        return l ;
    }

    public List<AcompanhamentoProduto> getList(DaoFilters filters, String order) throws JSONException {
        List<AcompanhamentoProduto> l = new ArrayList<AcompanhamentoProduto>() ;
        JSONArray jaa = findAll(filters, order) ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new AcompanhamentoProduto(jaa.getJSONObject(i))) ;
        }
        return l ;
    }

    public List<AcompanhamentoProduto> getProdutoAcomp(int produto_id){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("AFOOD_PRODUTO_ID", "=", String.valueOf(produto_id)));
        try {
            return getList(f, "AFOOD_ACOMP_ID") ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }
}
