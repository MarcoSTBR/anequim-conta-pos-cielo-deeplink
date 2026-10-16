package br.com.st.anequimconta_stand_dl1.entidade;

import androidx.annotation.NonNull;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaMultNFCe implements Entidade {

    private String UUID;
    private String conta_mult_uuid;
    private Date data;
    private String chave;
    private String protocolo;
    private String x_movito;
    private int stact;
    private int status_cancelamento;
    private int status_contingencia;
    private int status;
    private int tipo;
    private int IMPORT ;

    public ContaMultNFCe(String UUID, String conta_mult_uuid, Date data, String chave, String protocolo, String x_movito, int stact, int status_cancelamento, int status_contingencia, int status, int tipo) {
        this.UUID = UUID;
        this.conta_mult_uuid = conta_mult_uuid;
        this.data = data;
        this.chave = chave;
        this.protocolo = protocolo;
        this.x_movito = x_movito;
        this.stact = stact;
        this.status_cancelamento = status_cancelamento;
        this.status_contingencia = status_contingencia;
        this.status = status;
        this.tipo = tipo;
        IMPORT = 0 ;
    }

    public ContaMultNFCe(@NonNull JSONObject j) throws JSONException {
        UUID = j.getString("UUID");
        conta_mult_uuid = j.getString("CONTA_MULT_UUID");
        data = UtilSet.getData(j.getString("DATA"));
        chave = j.getString("CHAVE");
        protocolo = j.getString("PROTOCOLO");
        stact = j.getInt("STACT");
        status = j.getInt("STATUS");
        x_movito = j.getString("XMOTIVO");
        status_cancelamento = j.getInt("STATUS_CANCELAMENTO");
        status_contingencia = j.getInt("STATUS_CONTINGENCIA");
        tipo = j.getInt("TIPO");
        IMPORT = j.getInt("IMPORT");
    }

    @Override
    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("UUID", UUID);
        j.put("CONTA_MULT_UUID", conta_mult_uuid);
        j.put("DATA", df.format(data));
        j.put("CHAVE", chave);
        j.put("PROTOCOLO", protocolo);
        j.put("STACT", stact);
        j.put("STATUS", status);
        j.put("XMOTIVO", x_movito);
        j.put("STATUS_CANCELAMENTO", status_cancelamento);
        j.put("STATUS_CONTINGENCIA", status_contingencia);
        j.put("TIPO", tipo);
        j.put("IMPORT", IMPORT);
        return j;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getConta_mult_uuid() {
        return conta_mult_uuid;
    }

    public void setConta_mult_uuid(String conta_mult_uuid) {
        this.conta_mult_uuid = conta_mult_uuid;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getX_movito() {
        return x_movito;
    }

    public void setX_movito(String x_movito) {
        this.x_movito = x_movito;
    }

    public int getStact() {
        return stact;
    }

    public void setStact(int stact) {
        this.stact = stact;
    }

    public int getStatus_cancelamento() {
        return status_cancelamento;
    }

    public void setStatus_cancelamento(int status_cancelamento) {
        this.status_cancelamento = status_cancelamento;
    }

    public int getStatus_contingencia() {
        return status_contingencia;
    }

    public void setStatus_contingencia(int status_contingencia) {
        this.status_contingencia = status_contingencia;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
