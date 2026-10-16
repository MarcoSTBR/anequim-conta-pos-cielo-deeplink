package br.com.st.anequimconta_stand_dl1.daoclass;

import org.json.JSONArray;

public class DaoFilter {
   private String campo ;
   private  String Operador ;
   private  String valor ;

    public DaoFilter(String campo, String operador, String valor) {
        this.campo = campo;
        this.Operador = operador;
        this.valor = valor;
    }

    public JSONArray getJSON(){
        JSONArray j = new JSONArray() ;
        j.put(campo) ;
        j.put(Operador) ;
        j.put(valor) ;
        return j ;
    }

    public String getJSONCx(){
        return "["+'"'+campo+'"'+','+'"'+Operador+'"'+','+'"'+valor+'"'+']' ;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getOperador() {
        return Operador;
    }

    public void setOperador(String operador) {
        Operador = operador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
