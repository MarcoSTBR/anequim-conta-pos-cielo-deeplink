package br.com.st.anequimconta_stand_dl1.entidade;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaPag implements Entidade{

    private int id ;
    private String UUID ;
    private String MULT_UUID ;
    private Date DATA ;
    private String CAIXA_UUID ;
    private int TERMINAL_ID ;
    private int MODALIDADE_ID ;
    private Double VALOR ;
    private Double TROCO ;
    private int STATUS ;

    public ContaPag(int id, String UUID, String MULT_UUID, Date DATA, String CAIXA_UUID, int TERMINAL_ID, int MODALIDADE_ID, Double VALOR, Double TROCO, int STATUS) {
        this.id = id;
        this.UUID = UUID;
        this.MULT_UUID = MULT_UUID;
        this.DATA = DATA;
        this.CAIXA_UUID = CAIXA_UUID;
        this.TERMINAL_ID = TERMINAL_ID;
        this.MODALIDADE_ID = MODALIDADE_ID;
        this.VALOR = VALOR;
        this.TROCO = TROCO;
        this.STATUS = STATUS;
    }

    public ContaPag(JSONObject j) throws JSONException {
        id            = j.getInt("id") ;
        UUID          = j.getString("UUID") ;
        MULT_UUID     = j.getString("MULT_UUID") ;
        DATA          = UtilSet.getData(j.getString("DATA")) ;
        CAIXA_UUID    = j.getString("CAIXA_UUID") ;
        TERMINAL_ID   = j.getInt("TERMINAL_ID") ;
        MODALIDADE_ID = j.getInt("MODALIDADE_ID") ;
        TROCO         = j.getDouble("TROCO") ;
        VALOR         = j.getDouble("VALOR") ;
        STATUS        = j.getInt("STATUS") ;
    }

    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        if (id > 0)
            j.put("id", 0) ;
        j.put("UUID", UUID) ;
        j.put("MULT_UUID", MULT_UUID) ;
        j.put("DATA", df.format(DATA)) ;
        j.put("CAIXA_UUID", CAIXA_UUID) ;
        j.put("TERMINAL_ID", TERMINAL_ID) ;
        j.put("MODALIDADE_ID", MODALIDADE_ID) ;
        j.put("VALOR", VALOR) ;
        j.put("TROCO", TROCO) ;
        j.put("STATUS", STATUS) ;
        return j;
    }

    public JSONObject geJSONData() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("id", id) ;
        j.put("UUID", UUID) ;
        j.put("MULT_UUID", MULT_UUID) ;
        j.put("DATA", df.format(DATA)) ;
        j.put("CAIXA_UUID", CAIXA_UUID) ;
        j.put("TERMINAL_ID", TERMINAL_ID) ;
        j.put("MODALIDADE_ID", MODALIDADE_ID) ;
        j.put("VALOR", VALOR) ;
        j.put("TROCO", TROCO) ;
        j.put("STATUS", STATUS) ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMULT_UUID() {
        return MULT_UUID;
    }

    public void setMULT_UUID(String MULT_UUID) {
        this.MULT_UUID = MULT_UUID;
    }

    public String getCAIXA_UUID() {
        return CAIXA_UUID;
    }

    public void setCAIXA_UUID(String CAIXA_UUID) {
        this.CAIXA_UUID = CAIXA_UUID;
    }

    public int getTERMINAL_ID() {
        return TERMINAL_ID;
    }

    public void setTERMINAL_ID(int TERMINAL_ID) {
        this.TERMINAL_ID = TERMINAL_ID;
    }

    public int getMODALIDADE_ID() {
        return MODALIDADE_ID;
    }

    public void setMODALIDADE_ID(int MODALIDADE_ID) {
        this.MODALIDADE_ID = MODALIDADE_ID;
    }

    public Double getVALOR() {
        return VALOR;
    }

    public void setVALOR(Double VALOR) {
        this.VALOR = VALOR;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public Double getTROCO() {
        return TROCO;
    }

    public void setTROCO(Double TROCO) {
        this.TROCO = TROCO;
    }
}
