package br.com.st.anequimconta_stand_dl1.entidade;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaItem {

    private int id ;
    private String UUID ;
    private String UUID_ACOMP ;
    private int PRODUTO_ID ;
    private String CONTA_UUID ;
    private Date DATA ;
    private int USUARIO_ID ;
    private int TERMINAL_ID ;
    private Double QUANTIDADE ;
    private Double PRECO ;
    private Double VALOR ;
    private Double COMISSAO ;
    private Double DESCONTO ;
    private String OBS ;
    private int STATUS ;

    public ContaItem(JSONObject j) throws JSONException {
     id             = j.getInt("id");
     UUID           = j.getString("UUID");
     UUID_ACOMP     = j.getString("UUID_ACOMP");
     PRODUTO_ID     = j.getInt("PRODUTO_ID");
     CONTA_UUID     = j.getString("CONTA_UUID");
     DATA           = UtilSet.getData(j.getString("DATA"));
     USUARIO_ID     = j.getInt("USUARIO_ID");
     TERMINAL_ID    = j.getInt("TERMINAL_ID");
     QUANTIDADE     = j.getDouble("QUANTIDADE");
     PRECO          = j.getDouble("PRECO");
     VALOR          = j.getDouble("VALOR");
     COMISSAO       = j.getDouble("COMISSAO");
     DESCONTO       = j.getDouble("DESCONTO");
     OBS            = j.getString("OBS");
     STATUS         = j.getInt("STATUS");
    }

    public JSONObject getJSON() throws JSONException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("id", id);
        j.put("UUID", UUID);
        j.put("UUID_ACOMP", UUID_ACOMP);
        j.put("PRODUTO_ID", PRODUTO_ID);
        j.put("CONTA_UUID", CONTA_UUID);
        j.put("DATA", dt.format(DATA));
        j.put("USUARIO_ID", USUARIO_ID);
        j.put("TERMINAL_ID", TERMINAL_ID);
        j.put("QUANTIDADE", QUANTIDADE);
        j.put("PRECO", PRECO);
        j.put("VALOR", VALOR);
        j.put("COMISSAO", COMISSAO);
        j.put("DESCONTO", DESCONTO);
        j.put("OBS", OBS);
        j.put("STATUS", STATUS);
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

    public String getUUID_ACOMP() {
        return UUID_ACOMP;
    }

    public void setUUID_ACOMP(String UUID_ACOMP) {
        this.UUID_ACOMP = UUID_ACOMP;
    }

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public void setPRODUTO_ID(int PRODUTO_ID) {
        this.PRODUTO_ID = PRODUTO_ID;
    }

    public String getCONTA_UUID() {
        return CONTA_UUID;
    }

    public void setCONTA_UUID(String CONTA_UUID) {
        this.CONTA_UUID = CONTA_UUID;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public void setUSUARIO_ID(int USUARIO_ID) {
        this.USUARIO_ID = USUARIO_ID;
    }

    public int getTERMINAL_ID() {
        return TERMINAL_ID;
    }

    public void setTERMINAL_ID(int TERMINAL_ID) {
        this.TERMINAL_ID = TERMINAL_ID;
    }

    public Double getQUANTIDADE() {
        return QUANTIDADE;
    }

    public void setQUANTIDADE(Double QUANTIDADE) {
        this.QUANTIDADE = QUANTIDADE;
    }

    public Double getPRECO() {
        return PRECO;
    }

    public void setPRECO(Double PRECO) {
        this.PRECO = PRECO;
    }

    public Double getVALOR() {
        return VALOR;
    }

    public void setVALOR(Double VALOR) {
        this.VALOR = VALOR;
    }

    public Double getCOMISSAO() {
        return COMISSAO;
    }

    public void setCOMISSAO(Double COMISSAO) {
        this.COMISSAO = COMISSAO;
    }

    public Double getDESCONTO() {
        return DESCONTO;
    }

    public void setDESCONTO(Double DESCONTO) {
        this.DESCONTO = DESCONTO;
    }

    public String getOBS() {
        return OBS;
    }

    public void setOBS(String OBS) {
        this.OBS = OBS;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }
}
