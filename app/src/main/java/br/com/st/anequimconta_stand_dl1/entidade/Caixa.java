package br.com.st.anequimconta_stand_dl1.entidade;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Caixa implements Entidade{

    private int id ;
    private String UUID ;
    private int LOJA_ID ;
    private int USUARIO_ID ;
    private int MODALIDADE_ID ;
    private int CONF_TERMINAL_ID ;
    private Date DATA ;
    private Date DATA_FECHAMENTO ;
    private double VALOR ;
    private int STATUS ;

    public Caixa(int id, String UUID, int LOJA_ID, int USUARIO_ID, int MODALIDADE_ID, int CONF_TERMINAL_ID, Date DATA, Date DATA_FECHAMENTO, double VALOR, int STATUS) {
        this.id = id ;
        this.UUID = UUID;
        this.LOJA_ID = LOJA_ID;
        this.USUARIO_ID = USUARIO_ID;
        this.MODALIDADE_ID = MODALIDADE_ID;
        this.CONF_TERMINAL_ID = CONF_TERMINAL_ID;
        this.DATA = DATA;
        this.DATA_FECHAMENTO = DATA_FECHAMENTO;
        this.VALOR = VALOR;
        this.STATUS = STATUS;
    }

    public Caixa(JSONObject j) throws JSONException {
        this.id =  j.getInt("id");
        this.UUID =  j.getString("UUID");
        this.LOJA_ID =  j.getInt("LOJA_ID");
        this.USUARIO_ID = j.getInt("USUARIO_ID");
        this.MODALIDADE_ID = j.getInt("MODALIDADE_ID");
        this.CONF_TERMINAL_ID = j.getInt("CONF_TERMINAL_ID");
        this.DATA = UtilSet.getData(j.getString("DATA"));
        this.DATA_FECHAMENTO = UtilSet.getData(j.getString("DATA_FECHAMENTO"));
        this.VALOR = j.getDouble("VALOR");
        this.STATUS = j.getInt("STATUS");
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        if (id != 0)
            j.put("id", id) ;
        j.put("UUID", UUID) ;
        j.put("LOJA_ID", LOJA_ID) ;
        j.put("USUARIO_ID", USUARIO_ID) ;
        j.put("MODALIDADE_ID", MODALIDADE_ID) ;
        j.put("CONF_TERMINAL_ID", CONF_TERMINAL_ID) ;
        j.put("DATA", fdate.format(DATA)) ;
        j.put("DATA_FECHAMENTO", fdate.format(DATA_FECHAMENTO)) ;
        j.put("VALOR", VALOR) ;
        j.put("STATUS", STATUS) ;
        return j;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getLOJA_ID() {
        return LOJA_ID;
    }

    public void setLOJA_ID(int LOJA_ID) {
        this.LOJA_ID = LOJA_ID;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public void setUSUARIO_ID(int USUARIO_ID) {
        this.USUARIO_ID = USUARIO_ID;
    }

    public int getMODALIDADE_ID() {
        return MODALIDADE_ID;
    }

    public void setMODALIDADE_ID(int MODALIDADE_ID) {
        this.MODALIDADE_ID = MODALIDADE_ID;
    }

    public int getCONF_TERMINAL_ID() {
        return CONF_TERMINAL_ID;
    }

    public void setCONF_TERMINAL_ID(int CONF_TERMINAL_ID) {
        this.CONF_TERMINAL_ID = CONF_TERMINAL_ID;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public Date getDATA_FECHAMENTO() {
        return DATA_FECHAMENTO;
    }

    public void setDATA_FECHAMENTO(Date DATA_FECHAMENTO) {
        this.DATA_FECHAMENTO = DATA_FECHAMENTO;
    }

    public double getVALOR() {
        return VALOR;
    }

    public void setVALOR(double VALOR) {
        this.VALOR = VALOR;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
