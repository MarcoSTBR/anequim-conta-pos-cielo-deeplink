package br.com.st.anequimconta_stand_dl1.entidade;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class CartaoRFid implements Entidade {

    private int id ;
    private String RFID ;
    private int NUMERO ;
    private String DESCRICAO ;
    private int TIPO ;
    private String MOTIVO ;
    private Date DATA_UP ;
    private int STATUS ;



    public CartaoRFid(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.RFID = j.getString("RFID");
        this.NUMERO = j.getInt("NUMERO");
        this.DESCRICAO = j.getString("DESCRICAO");
        this.TIPO = j.getInt("TIPO");
        this.MOTIVO = j.getString("MOTIVO");
        this.DATA_UP = UtilSet.getData(j.getString("DATA_UP"));
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

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public int getNUMERO() {
        return NUMERO;
    }

    public void setNUMERO(int NUMERO) {
        this.NUMERO = NUMERO;
    }

    public String getDESCRICAO() {
        return DESCRICAO;
    }

    public void setDESCRICAO(String DESCRICAO) {
        this.DESCRICAO = DESCRICAO;
    }

    public int getTIPO() {
        return TIPO;
    }

    public void setTIPO(int TIPO) {
        this.TIPO = TIPO;
    }

    public String getMOTIVO() {
        return MOTIVO;
    }

    public void setMOTIVO(String MOTIVO) {
        this.MOTIVO = MOTIVO;
    }

    public Date getDATA_UP() {
        return DATA_UP;
    }

    public void setDATA_UP(Date DATA_UP) {
        this.DATA_UP = DATA_UP;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }
}
