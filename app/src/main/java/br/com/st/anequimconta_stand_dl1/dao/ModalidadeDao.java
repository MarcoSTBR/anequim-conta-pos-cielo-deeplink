package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ModalidadeDao extends Dao {

    private static ModalidadeDao modalidadeDao ;

    public static ModalidadeDao getModalidadeDao(Context ctx) {
        if (modalidadeDao == null) {
            modalidadeDao = new ModalidadeDao(ctx) ;
            return modalidadeDao ;
        } else return modalidadeDao ;
    }

    public ModalidadeDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "MODALIDADE";
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
        List<DaoCampos> l = new ArrayList<>() ;
        l.add(new DaoCampos("CODIGO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("DESCRICAO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("TIPOMODALIDADE", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("COD_RECEBIMENTO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("FOTO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true)) ;
        return l;
    }

    public List<Modalidade> getList(DaoFilters filters, String order) throws JSONException {
        List<Modalidade> l = new ArrayList<Modalidade>() ;
        JSONArray jaa = findAll(filters, order) ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new Modalidade(jaa.getJSONObject(i))) ;
        }
        return l ;
    }

    public List<Modalidade> getGradeList() {
        try {
            DaoFilters f = new DaoFilters();
            f.add(new DaoFilter("TIPOMODALIDADE", "<>", "pgTroco"));
            f.add(new DaoFilter("STATUS", "=", "1"));
            return getList(f, "id") ;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<Modalidade>() ;
        }

    }

    public Modalidade getModalidade(String id){
        try {
            Log.i("dao", "getModalidade "+id) ;
            DaoFilters f = new DaoFilters() ;
            f.add(new DaoFilter("id", "=", id));
            List<Modalidade> l = getList(f, "") ;
            if (l.size()>0)
                return l.get(0);
              else return null ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }

    }

    public Modalidade getTroco() {
        try {
            DaoFilters f = new DaoFilters() ;
            f.add(new DaoFilter("TIPOMODALIDADE", "=", "pgTroco"));
            List<Modalidade> l = getList(f, "") ;
            if (l.size()>0)
                return l.get(0);
            else return null ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }

    public Modalidade getModalidadeDinheiro() {
        try {
            DaoFilters f = new DaoFilters() ;
            f.add(new DaoFilter("TIPOMODALIDADE", "=", "pgDinheiro"));
            List<Modalidade> l = getList(f, "") ;
            if (l.size()>0)
                return l.get(0);
            else return null ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }

    public Modalidade getModalidadeDesconto() {
        try {
            DaoFilters f = new DaoFilters() ;
            f.add(new DaoFilter("TIPOMODALIDADE", "=", "pgDesconto"));
            List<Modalidade> l = getList(f, "") ;
            if (l.size()>0)
                return l.get(0);
            else return null ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }
}
