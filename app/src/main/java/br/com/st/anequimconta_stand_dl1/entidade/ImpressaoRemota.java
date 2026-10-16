package br.com.st.anequimconta_stand_dl1.entidade;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImpressaoRemota {

    private String UUID ;
    private String PEDIDO ;
    private String REFERENCIA ;
    private Date DATA ;
    private List<ImpressaoRemotaItem> ITENS ;

    public ImpressaoRemota(JSONObject j) throws JSONException {
        UUID         = j.getString("UUID") ;
        PEDIDO       = j.getString("PEDIDO") ;
        REFERENCIA   = j.getString("REFERENCIA") ;
        DATA         = UtilSet.getData(j.getString("DATA")) ;
        ITENS        = new ArrayList<>();
        for (int i = 0 ; i < j.getJSONArray("ITENS").length() ; i++) {
            ITENS.add(new ImpressaoRemotaItem(j.getJSONArray("ITENS").getJSONObject(i))) ;
        }
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getPEDIDO() {
        return PEDIDO;
    }

    public void setPEDIDO(String PEDIDO) {
        this.PEDIDO = PEDIDO;
    }

    public String getREFERENCIA() {
        return REFERENCIA;
    }

    public void setREFERENCIA(String REFERENCIA) {
        this.REFERENCIA = REFERENCIA;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public List<ImpressaoRemotaItem> getITENS() {
        return ITENS;
    }

    public void setITENS(List<ImpressaoRemotaItem> ITENS) {
        this.ITENS = ITENS;
    }
}
