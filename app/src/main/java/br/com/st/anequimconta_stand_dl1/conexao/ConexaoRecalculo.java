package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import br.com.st.anequimconta_stand_dl1.dao.AmbienteDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Ambiente;
import br.com.st.anequimconta_stand_dl1.entidade.CartaoRFid;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public abstract class ConexaoRecalculo extends ConexaoServer{

    private final String TAG = "recalculo" ;
    private Activity ctx ;

    public ConexaoRecalculo(Activity ctx, int rfid_id, String conta_uuid) throws MalformedURLException {
        super(ctx);
        msg = "Consultando Ambiente" ;
        method = "POST" ;
        this.ctx = ctx;
        maps.put("class","AfoodRecalculoPos") ;
        maps.put("method","recalculo") ;
        maps.put("cartao_rfid_id",rfid_id);
        maps.put("conta_uuid_id", conta_uuid) ;
        url = new URL(UtilSet.getServidorMaster(ctx)) ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i(TAG, codInt+" "+s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                recalculoOk() ;                //setJSON(j.getJSONArray("data")) ;
            } else recalculoerro(j.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            recalculoerro("COD "+codInt+" "+s);
        }
    }

    private void  setJSON(JSONArray j) throws JSONException {
        //List<Acompanhamento> l = new ArrayList<Acompanhamento>();
        AmbienteDao.getAmbienteDao(ctx).deleteAll();
        for (int i = 0 ; i < j.length() ; i++) {
            Ambiente a = new Ambiente(j.getJSONObject(i)) ;
            AmbienteDao.getAmbienteDao(ctx).insert(a) ;
            Log.i(TAG, codInt+" "+a.geJSON().toString()) ;
        }
    }

    public abstract void recalculoOk() ;
    public abstract void recalculoerro(String msg) ;
}
