package br.com.st.anequimconta_stand_dl1.entidade;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PedidoTiket {

    private int id ;
    private String UUID ;
    private Date DATA ;
    private String CONTA_ITEM_UUID ;
    private int STATUS ;
    private int USUARIO_ID ;
    private Date DATA_FECHAMENTO ;
    private int USUARIO_F_ID ;
    private int TERMINAL_ID ;



    public PedidoTiket(int id, String UUID, Date DATA, String CONTA_ITEM_UUID, int STATUS, int USUARIO_ID, Date DATA_FECHAMENTO, int USUARIO_F_ID, int TERMINAL_ID) {
        this.id = id;
        this.UUID = UUID;
        this.DATA = DATA;
        this.CONTA_ITEM_UUID = CONTA_ITEM_UUID;
        this.STATUS = STATUS;
        this.USUARIO_ID = USUARIO_ID;
        this.DATA_FECHAMENTO = DATA_FECHAMENTO;
        this.USUARIO_F_ID = USUARIO_F_ID;
        this.TERMINAL_ID = TERMINAL_ID;
    }

    public PedidoTiket(JSONObject j) throws JSONException {
      if (!j.isNull("id"))
        id = j.getInt("id") ;
       else id = 0 ;
      UUID                  = j.getString("UUID")  ;
      DATA                  = UtilSet.getData(j.getString("DATA")) ;
      CONTA_ITEM_UUID       = j.getString("CONTA_ITEM_UUID")  ;
      STATUS                = j.getInt("STATUS") ;
      USUARIO_ID            = j.getInt("USUARIO_ID") ;
      DATA_FECHAMENTO       = UtilSet.getData(j.getString("DATA_FECHAMENTO")) ;
      USUARIO_F_ID          = j.getInt("USUARIO_F_ID") ;
      TERMINAL_ID           = j.getInt("TERMINAL_ID") ;
    }

    public JSONObject getJSON() throws JSONException {
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("id", id) ;
        j.put("UUID", UUID);
        j.put("DATA", df.format(DATA));
        j.put("CONTA_ITEM_UUID", CONTA_ITEM_UUID);
        j.put("STATUS", STATUS);
        j.put("USUARIO_ID", USUARIO_ID);
        j.put("DATA_FECHAMENTO", df.format(DATA_FECHAMENTO));
        j.put("USUARIO_F_ID", USUARIO_F_ID);
        j.put("TERMINAL_ID", TERMINAL_ID);
        return j ;
    }

    public JSONObject getJSONInsert() throws JSONException {
        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        j.put("UUID", UUID);
        j.put("DATA", df.format(DATA));
        j.put("CONTA_ITEM_UUID", CONTA_ITEM_UUID);
        j.put("STATUS", STATUS);
        j.put("USUARIO_ID", USUARIO_ID);
        j.put("DATA_FECHAMENTO", df.format(DATA_FECHAMENTO));
        j.put("USUARIO_F_ID", USUARIO_F_ID);
        j.put("TERMINAL_ID", TERMINAL_ID);
        return j ;
    }

    public String toJSONString(){
        try {
            return getJSONInsert().toString() ;
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage() ;
        }
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

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public String getCONTA_ITEM_UUID() {
        return CONTA_ITEM_UUID;
    }

    public void setCONTA_ITEM_UUID(String CONTA_ITEM_UUID) {
        this.CONTA_ITEM_UUID = CONTA_ITEM_UUID;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public void setUSUARIO_ID(int USUARIO_ID) {
        this.USUARIO_ID = USUARIO_ID;
    }

    public Date getDATA_FECHAMENTO() {
        return DATA_FECHAMENTO;
    }

    public void setDATA_FECHAMENTO(Date DATA_FECHAMENTO) {
        this.DATA_FECHAMENTO = DATA_FECHAMENTO;
    }

    public int getUSUARIO_F_ID() {
        return USUARIO_F_ID;
    }

    public void setUSUARIO_F_ID(int USUARIO_F_ID) {
        this.USUARIO_F_ID = USUARIO_F_ID;
    }

    public int getTERMINAL_ID() {
        return TERMINAL_ID;
    }

    public void setTERMINAL_ID(int TERMINAL_ID) {
        this.TERMINAL_ID = TERMINAL_ID;
    }
}
