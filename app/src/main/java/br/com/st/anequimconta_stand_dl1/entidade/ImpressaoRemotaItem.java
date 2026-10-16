package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class ImpressaoRemotaItem {

    private String UUID ;
    private String UUID_ACOMP ;
    private int PRODUTO_ID ;
    private Double QUANTIDADE ;
    private String OBS ;

    public ImpressaoRemotaItem(JSONObject j) throws JSONException {
        UUID = j.getString("UUID") ;
        UUID_ACOMP = j.getString("UUID_ACOMP") ;
        PRODUTO_ID = j.getInt("PRODUTO_ID") ;
        QUANTIDADE = j.getDouble("QUANTIDADE") ;
        OBS = j.getString("OBS") ;
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

    public Double getQUANTIDADE() {
        return QUANTIDADE;
    }

    public void setQUANTIDADE(Double QUANTIDADE) {
        this.QUANTIDADE = QUANTIDADE;
    }

    public String getOBS() {
        return OBS;
    }

    public void setOBS(String OBS) {
        this.OBS = OBS;
    }
}
