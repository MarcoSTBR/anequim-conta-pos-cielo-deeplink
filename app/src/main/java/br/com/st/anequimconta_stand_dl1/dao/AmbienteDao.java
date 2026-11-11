package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.daoclass.Dao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.daoclass.ListAll;
import br.com.st.anequimconta_stand_dl1.entidade.Ambiente;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoPrimaryKeyTipo;

public class AmbienteDao extends Dao implements ListAll.Listener<Ambiente>  {

    private static AmbienteDao ambienteDao;
    private ListAll listAll = null ;

    public static AmbienteDao getAmbienteDao(Context ctx) {
        if (ambienteDao == null) {
/*
            SQLiteDatabase db = DBHelper.getDB(ctx).getWritableDatabase() ;
            db.execSQL("DROP TABLE IF EXISTS AMBIENTE") ;
            db.execSQL("CREATE TABLE IF NOT EXISTS AMBIENTE ( "
                    + "id INTEGER PRIMARY KEY, "
                    + "CODIGO TEXT, "
                    + "DESCRICAO TEXT)");
*/

            ambienteDao = new AmbienteDao(ctx) ;
            return ambienteDao;
        } else return ambienteDao;
    }


    public AmbienteDao(Context ctx) {
        super(ctx);
        listAll = new ListAll<Ambiente>(this, this) ;
    }

    @Override
    public Ambiente getItem(JSONObject j) {
        Ambiente ambiente = null ;
        try {
            ambiente =new Ambiente(j) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ambiente;
    }


    public ListAll getListAll(){
        return listAll ;
    }


    @Override
    public String getTabela() {
        return "AMBIENTE";
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
        l.add(new DaoCampos("CODIGO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("DESCRICAO", DaoCampoTipo.dbString, true)) ;
        return l ;
    }

    public List<Ambiente> getList(DaoFilters filters, String order) throws JSONException {
        List<Ambiente> l = new ArrayList<Ambiente>() ;
        JSONArray jaa = findAll(filters, order) ;
        for (int i = 0 ; i < jaa.length() ; i++) {
            l.add(new Ambiente(jaa.getJSONObject(i))) ;
        }
        return l ;
    }

    public Ambiente getAmienteID(int id){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("id", "=", String.valueOf(id)));
        try {
            List<Ambiente> l = getList(f, "") ;
            return  l.get(0) ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }

}
