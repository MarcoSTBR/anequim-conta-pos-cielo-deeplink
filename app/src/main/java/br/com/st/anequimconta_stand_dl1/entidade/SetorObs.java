package br.com.st.anequimconta_stand_dl1.entidade;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SetorObs implements Entidade{
    private int id ;
    private int SETOR_ID ;
    private String OBSERVACAO ;
    private int STATUS ;

    public SetorObs(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.SETOR_ID = j.getInt("SETOR_IMP_ID");
        this.OBSERVACAO = j.getString("OBSERVACAO");
        this.STATUS = j.getInt("STATUS");
    }


    @Override
    public JSONObject geJSON() throws JSONException {
        return new JSONObject(new Gson().toJson(this));
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSETOR_ID() {
        return SETOR_ID;
    }

    public void setSETOR_ID(int SETOR_ID) {
        this.SETOR_ID = SETOR_ID;
    }

    public String getOBSERVACAO() {
        return OBSERVACAO;
    }

    public void setOBSERVACAO(String OBSERVACAO) {
        this.OBSERVACAO = OBSERVACAO;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }
}
