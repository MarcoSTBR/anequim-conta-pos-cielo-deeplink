package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class ContaMultDest implements Entidade{
    private int id ;
    private String UUID ;
    private String contaMultDest;
    private String cpfcnpj ;
    private String nome ;
    private int status ;

    public ContaMultDest(int id, String UUID, String contaMultDest, String cpfcnpj, String nome, int status) {
        this.id = id;
        this.UUID = UUID;
        this.contaMultDest = contaMultDest;
        this.cpfcnpj = cpfcnpj;
        this.nome = nome;
        this.status = status;
    }

    public ContaMultDest(JSONObject j) throws JSONException {
        id             = j.getInt("ID") ;
        UUID           = j.getString("UUID");
        contaMultDest = j.getString("CONTA_MULT_UUID");
        cpfcnpj        = j.getString("CPFCNPJ"); ;
        nome           = j.getString("NOME"); ;
        status         = j.getInt("STATUS") ;
    }

    public JSONObject getExportacaoJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("UUID", UUID) ;
        j.put("CONTA_MULT_UUID", contaMultDest) ;
        j.put("CPFCNPJ", cpfcnpj) ;
        j.put("NOME", nome) ;
        j.put("STATUS", status) ;
        return j;

    }

    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("ID", id) ;
        j.put("UUID", UUID) ;
        j.put("CONTA_MULT_UUID", contaMultDest) ;
        j.put("CPFCNPJ", cpfcnpj) ;
        j.put("NOME", nome) ;
        j.put("STATUS", status) ;
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

    public String getContaMultDest() {
        return contaMultDest;
    }

    public void setContaMultDest(String contaMultDest) {
        this.contaMultDest = contaMultDest;
    }

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
