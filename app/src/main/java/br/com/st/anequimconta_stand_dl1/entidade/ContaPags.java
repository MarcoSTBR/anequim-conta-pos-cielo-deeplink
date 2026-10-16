package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class ContaPags implements Entidade{

    private int id ;
    private String CONTA_UUID ;
    private String CONTA_MULT_UUID ;

    public ContaPags(int id, String CONTA_UUID, String CONTA_MULT_UUID) {
        this.id = id;
        this.CONTA_UUID = CONTA_UUID;
        this.CONTA_MULT_UUID = CONTA_MULT_UUID;
    }

    public ContaPags(JSONObject j) throws JSONException {
        id = j.getInt("id") ;
        CONTA_UUID      = j.getString("CONTA_UUID") ;
        CONTA_MULT_UUID = j.getString("MULT_UUID") ;
    }


    @Override
    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("id", id) ;
        j.put("CONTA_UUID", CONTA_UUID) ;
        j.put("MULT_UUID", CONTA_MULT_UUID) ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCONTA_UUID() {
        return CONTA_UUID;
    }

    public void setCONTA_UUID(String CONTA_UUID) {
        this.CONTA_UUID = CONTA_UUID;
    }

    public String getCONTA_MULT_UUID() {
        return CONTA_MULT_UUID;
    }

    public void setCONTA_MULT_UUID(String CONTA_MULT_UUID) {
        this.CONTA_MULT_UUID = CONTA_MULT_UUID;
    }
}
