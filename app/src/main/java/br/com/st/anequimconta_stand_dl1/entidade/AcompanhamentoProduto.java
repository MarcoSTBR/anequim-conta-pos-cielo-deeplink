package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class AcompanhamentoProduto implements Entidade{

    private int id ;
    private int produto_id ;
    private int acomp_id ;


    public AcompanhamentoProduto(JSONObject j) throws JSONException {
       id = j.getInt("id") ;
       produto_id = j.getInt("AFOOD_PRODUTO_ID") ;
       acomp_id = j.getInt("AFOOD_ACOMP_ID") ;
    }

    public AcompanhamentoProduto(int id, int produto_id, int acomp_id) {
        this.id = id;
        this.produto_id = produto_id;
        this.acomp_id = acomp_id;
    }


    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("id", id) ;
        j.put("AFOOD_PRODUTO_ID", produto_id) ;
        j.put("AFOOD_ACOMP_ID", acomp_id) ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getAcomp_id() {
        return acomp_id;
    }

    public void setAcomp_id(int acomp_id) {
        this.acomp_id = acomp_id;
    }

}
