package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class GradeVendasItem implements Entidade{
    private int id ;
    private int gradeVendasId ;
    private int produto_id ;
    private double preco ;
    private double comissao ;
    private int status ;

    public GradeVendasItem(JSONObject j) throws JSONException {
        id  = j.getInt("id") ;
        gradeVendasId = j.getInt("GRADE_VENDAS_ID") ;
        produto_id = j.getInt("PRODUTO_ID");
        if (j.isNull("PRECO"))
            preco = 0 ;
         else  preco = j.getDouble("PRECO") ;
        if (j.isNull("COMISSAO"))
            comissao = 0 ;
        else  comissao = j.getDouble("COMISSAO") ;
        status = j.getInt("STATUS") ;
    }


    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("id", id) ;
        j.put("GRADE_VENDAS_ID", gradeVendasId) ;
        j.put("PRODUTO_ID", produto_id) ;
        j.put("PRECO", preco) ;
        j.put("COMISSAO", comissao) ;
        j.put("STATUS", status) ;
        return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGradeVendasId() {
        return gradeVendasId;
    }

    public void setGradeVendasId(int gradeVendasId) {
        this.gradeVendasId = gradeVendasId;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }
}
