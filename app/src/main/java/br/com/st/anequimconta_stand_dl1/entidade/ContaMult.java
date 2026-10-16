package br.com.st.anequimconta_stand_dl1.entidade;

import android.util.Log;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ContaMult implements Entidade{

    private int id ;
    private String UUID ;
    private int LOJA_ID ;
    private String CAIXA_UUID;
    private Date DATA ;
    private int STATUS;
    private int IMPORT ;


    public ContaMult(int id, int LOJA_ID, Date DATA, String UUID, String CAIXA_UUID, int STATUS, int IMPORT) {
        this.id = id;
        this.LOJA_ID = LOJA_ID ;
        this.DATA = DATA ;
        this.UUID = UUID;
        this.CAIXA_UUID = CAIXA_UUID;
        this.STATUS = STATUS;
        this.IMPORT = IMPORT;
    }

    public ContaMult(JSONObject j) throws JSONException {
        id          = j.getInt("id") ;
        LOJA_ID     = j.getInt("LOJA_ID") ;
        UUID        = j.getString("UUID") ;
        DATA        = UtilSet.getData(j.getString("DATA")) ;
        CAIXA_UUID  = j.getString("CAIXA_UUID") ;
        STATUS      = j.getInt("STATUS") ;
        IMPORT      = j.getInt("IMPORT") ;
    }


    @Override
    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject() ;
        if (id != 0)
          j.put("id", id) ;
        j.put("LOJA_ID", LOJA_ID) ;
        j.put("UUID", UUID) ;
        j.put("DATA", df.format(DATA)) ;
        j.put("CAIXA_UUID", CAIXA_UUID) ;
        j.put("STATUS", STATUS) ;
        j.put("IMPORT", IMPORT) ;
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

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public int getIMPORT() {
        return IMPORT;
    }

    public void setIMPORT(int IMPORT) {
        this.IMPORT = IMPORT;
    }

    public String getCAIXA_UUID() {
        return CAIXA_UUID;
    }

    public void setCAIXA_UUID(String CAIXA_UUID) {
        this.CAIXA_UUID = CAIXA_UUID;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public static class DataDesc implements Comparator<ContaMult> {

        @Override
        public int compare(ContaMult conta1, ContaMult conta2) {
            int r = (conta1.getDATA().compareTo(conta2.getDATA())) ;
            Log.i("compare", "r "+r+" conta1 "+conta1.getDATA()+" conta2 "+conta2.getDATA()) ;
            return r ;
        }
    }

    public int getLOJA_ID() {
        return LOJA_ID;
    }

    public void setLOJA_ID(int LOJA_ID) {
        this.LOJA_ID = LOJA_ID;
    }
}
