package br.com.st.anequimconta_stand_dl1.daoclass;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListAll<T>  {

    private final String TAG = "listall" ;
    private List<T> list ;
    private Dao dao ;
    private Listener<T> listener;

    public ListAll(Dao dao, Listener<T> listener) {
        this.dao = dao ;
        this.listener = listener ;
    }

    public List<T> getAll(DaoFilters f, String order){
        list = new ArrayList<>() ;
        JSONArray raa = null;
        try {
            raa = dao.findAll(f, order);
            for (int i = 0 ; i < raa.length() ; i++) {
                Log.i(TAG, raa.getJSONObject(i).toString()) ;
                list.add(listener.getItem(raa.getJSONObject(i))) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list ;
    }

    public interface Listener<T>{
        T getItem(JSONObject j) ;
    }
   // public abstract T getItem(JSONObject j) ;

}
