package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class ProdutoTicket implements Entidade {

    private int ID ;
    private int PRODUTO_ID ;

    public ProdutoTicket(JSONObject j) throws JSONException {
        ID = j.getInt("ID") ;
        PRODUTO_ID = j.getInt("PRODUTO_ID") ;
    }

    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID", ID) ;
        j.put("PRODUTO_ID", PRODUTO_ID) ;
        return j;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public void setPRODUTO_ID(int PRODUTO_ID) {
        this.PRODUTO_ID = PRODUTO_ID;
    }
}
