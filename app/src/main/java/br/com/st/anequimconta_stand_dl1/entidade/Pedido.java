package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class Pedido implements Entidade{

    private String UUID ;
    private String NUMERO;
    private String REFERENCIA ;
    private int STATUS ;
    private int CORRENTE ;

    public Pedido(String UUID, String NUMERO, String REFERENCIA, int STATUS, int CORRENTE) {
        this.UUID = UUID;
        this.NUMERO = NUMERO;
        this.REFERENCIA = REFERENCIA;
        this.STATUS = STATUS;
        this.CORRENTE = CORRENTE;
    }

    public Pedido(JSONObject j) throws JSONException {
        this.UUID = j.getString("UUID");
        this.REFERENCIA = j.getString("REFERENCIA");
        this.NUMERO = j.getString("NUMERO");
        this.STATUS = j.getInt("STATUS");
        this.CORRENTE = j.getInt("CORRENTE");
    }


    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("UUID", UUID) ;
        j.put("NUMERO", NUMERO) ;
        j.put("REFERENCIA", REFERENCIA) ;
        j.put("STATUS", STATUS) ;
        j.put("CORRENTE", CORRENTE) ;
        return j;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getREFERENCIA() {
        return REFERENCIA;
    }

    public void setREFERENCIA(String REFERENCIA) {
        this.REFERENCIA = REFERENCIA;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public int getCORRENTE() {
        return CORRENTE;
    }

    public void setCORRENTE(int CORRENTE) {
        this.CORRENTE = CORRENTE;
    }

    public String getNUMERO() {
        return NUMERO;
    }

    public void setNUMERO(String NUMERO) {
        this.NUMERO = NUMERO;
    }
}
