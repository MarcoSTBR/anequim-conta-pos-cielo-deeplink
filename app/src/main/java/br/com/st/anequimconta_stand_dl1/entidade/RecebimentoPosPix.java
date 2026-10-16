package br.com.st.anequimconta_stand_dl1.entidade;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecebimentoPosPix implements Entidade{

    private int id ;
    private String UUID ;
    private Date DATA ;
    private String PAGAMENTO_UUID;
    private String CONTA_PAG_UUID;
    private int STATUS ;
    private String RECIBO;

    public RecebimentoPosPix(String UUID, Date DATA, String PAGAMENTO_UUID, String CONTA_PAG_UUID, int STATUS, String RECIBO) {
        id = 0 ;
        this.UUID = UUID;
        this.DATA = DATA;
        this.PAGAMENTO_UUID = PAGAMENTO_UUID;
        this.CONTA_PAG_UUID = CONTA_PAG_UUID;
        this.STATUS = STATUS;
        this.RECIBO = RECIBO ;
    }

    public RecebimentoPosPix(JSONObject j) throws JSONException {
        this.id = j.getInt("id");
        this.UUID = j.getString("UUID");
        this.DATA = UtilSet.getData(j.getString("DATA"));
        this.PAGAMENTO_UUID = j.getString("PAGAMENTO_UUID") ;
        this.CONTA_PAG_UUID = j.getString("CONTA_PAG_UUID") ;
        this.STATUS = j.getInt("STATUS");
        this.RECIBO = j.getString("RECIBO");
    }


    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat dta = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        JSONObject j = new JSONObject() ;
        if (id > 0)
           j.put("id", id) ;
        j.put("UUID", UUID) ;
        j.put("DATA", dta.format(DATA)) ;
        j.put("PAGAMENTO_UUID", PAGAMENTO_UUID) ;
        j.put("CONTA_PAG_UUID", CONTA_PAG_UUID) ;
        j.put("STATUS", STATUS) ;
        j.put("RECIBO", RECIBO) ;
        return j;
    }

    public JSONObject geAltJSON() throws JSONException {
        SimpleDateFormat dta = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        JSONObject j = new JSONObject() ;
        j.put("id", id) ;
        j.put("UUID", UUID) ;
        j.put("DATA", dta.format(DATA)) ;
        j.put("PAGAMENTO_UUID", PAGAMENTO_UUID) ;
        j.put("CONTA_PAG_UUID", CONTA_PAG_UUID) ;
        j.put("STATUS", STATUS) ;
        j.put("RECIBO", RECIBO) ;
        return j;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public String getPAGAMENTO_UUID() {
        return PAGAMENTO_UUID;
    }

    public void setPAGAMENTO_UUID(String PAGAMENTO_UUID) {
        this.PAGAMENTO_UUID = PAGAMENTO_UUID;
    }

    public String getCONTA_PAG_UUID() {
        return CONTA_PAG_UUID;
    }

    public void setCONTA_PAG_UUID(String CONTA_PAG_UUID) {
        this.CONTA_PAG_UUID = CONTA_PAG_UUID;
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

    public String getRECIBO() {
        return RECIBO;
    }

    public void setRECIBO(String RECIBO) {
        this.RECIBO = RECIBO;
    }
}
