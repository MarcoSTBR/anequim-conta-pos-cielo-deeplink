package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class Usuario implements Entidade{

    private int id ;
    private String LOGIN ;
    private String SENHA ;
    private String NOME ;
    private String AUTENTICACAO ;
    private int STATUS ;

    public Usuario(int id, String LOGIN, String SENHA, String NOME, String AUTENTICACAO, int STATUS) {
        this.id = id;
        this.LOGIN = LOGIN;
        this.SENHA = SENHA;
        this.NOME = NOME;
        this.AUTENTICACAO = AUTENTICACAO;
        this.STATUS = STATUS;
    }

    public Usuario(JSONObject j) throws JSONException {
        id = j.getInt("id") ;
        LOGIN = j.getString("LOGIN") ;
        SENHA = j.getString("SENHA") ;
        NOME  = j.getString("NOME") ;
        AUTENTICACAO = j.getString("AUTENTICACAO") ;
        STATUS = j.getInt("STATUS") ;
    }

    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject() ;
        j.put("id", id) ;
        j.put("LOGIN", LOGIN) ;
        j.put("SENHA", SENHA) ;
        j.put("NOME", NOME) ;
        j.put("AUTENTICACAO", AUTENTICACAO) ;
        j.put("STATUS", STATUS) ;
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLOGIN() {
        return LOGIN;
    }

    public void setLOGIN(String LOGIN) {
        this.LOGIN = LOGIN;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getSENHA() {
        return SENHA;
    }

    public void setSENHA(String SENHA) {
        this.SENHA = SENHA;
    }

    public String getAUTENTICACAO() {
        return AUTENTICACAO;
    }

    public void setAUTENTICACAO(String AUTENTICACAO) {
        this.AUTENTICACAO = AUTENTICACAO;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }
}
