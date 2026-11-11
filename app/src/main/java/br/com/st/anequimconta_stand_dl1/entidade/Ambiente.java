package br.com.st.anequimconta_stand_dl1.entidade;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Ambiente implements Entidade {

    private final String TAG = "Ambiente" ;
    private int id ;
    private String CODIGO ;
    private String DESCRICAO ;

    public Ambiente(JSONObject j) throws JSONException {
        this.id = j.getInt("id") ;
        this.CODIGO = j.getString("CODIGO") ;
        this.DESCRICAO = j.getString("DESCRICAO") ;
    }

    public Ambiente(int id, String CODIGO, String DESCRICAO) {
        this.id = id;
        this.CODIGO = CODIGO;
        this.DESCRICAO = DESCRICAO;
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        //JSONObject j = new JSONObject(new Gson().toJson(this).toString()) ;
        return new JSONObject(new Gson().toJson(this));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    public String getDESCRICAO() {
        return DESCRICAO;
    }

    public void setDESCRICAO(String DESCRICAO) {
        this.DESCRICAO = DESCRICAO;
    }
}
