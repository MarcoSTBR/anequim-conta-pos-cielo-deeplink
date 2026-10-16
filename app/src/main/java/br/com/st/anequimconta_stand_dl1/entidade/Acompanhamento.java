package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class Acompanhamento implements Entidade{
   private int id ;
   private String descricao ;
   private String obs ;
   private int min ;
   private int max ;
   private Double preco ;

   public Acompanhamento(JSONObject j) throws JSONException {
       id = j.getInt("id") ;
       descricao = j.getString("DESCRICAO") ;
       obs = j.getString("OBS") ;
       min = j.getInt("MIN") ;
       max = j.getInt("MAX") ;
       preco = j.getDouble("PRECO") ;
   }

    public Acompanhamento(int id, String descricao, String obs, int min, int max, Double preco) {
        this.id = id;
        this.descricao = descricao;
        this.obs = obs;
        this.min = min;
        this.max = max;
        this.preco = preco;
    }


    public JSONObject geJSON() throws JSONException {
       JSONObject j = new JSONObject();
       j.put("id", id) ;
       j.put("DESCRICAO", descricao) ;
       j.put("OBS", obs) ;
       j.put("MIN", min) ;
       j.put("MAX", max) ;
       j.put("PRECO", preco) ;
       return j ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

}
