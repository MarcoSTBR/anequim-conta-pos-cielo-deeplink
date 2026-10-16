package br.com.st.anequimconta_stand_dl1.entidade;

import br.com.st.anequimconta_stand_dl1.pospag.TipoRecebimentoPos;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecebimentoPos implements Entidade {
    private int id ;
    private String uuid;
    private int pagamento_id;
    private String pagamento_uuid ;
    private Date data ;
    private String conta_pag_uuid;
    private TipoRecebimentoPos status_rec;
    private int status;
    private String nsu ;
    private String recibo ;

    public RecebimentoPos(int id, String uuid, int pagamento_id, String pagamento_uuid, Date data, String conta_pag_uuid, TipoRecebimentoPos status_rec, int status, String nsu, String recibo) {
        this.id = id;
        this.uuid = uuid;
        this.pagamento_id = pagamento_id;
        this.pagamento_uuid = pagamento_uuid;
        this.data = data;
        this.conta_pag_uuid = conta_pag_uuid;
        this.status_rec = status_rec;
        this.status = status;
        this.nsu = nsu;
        this.recibo = recibo;
    }

    public RecebimentoPos(JSONObject j) throws JSONException {
        id = j.getInt("id") ;
        uuid = j.getString("UUID") ;
        pagamento_id = j.getInt("PAGAMENTO_ID") ;
        conta_pag_uuid = j.getString("CONTA_PAG_UUID") ;
        pagamento_uuid = j.getString("PAGAMENTO_UUID") ;
        status = j.getInt("STATUS") ;
        status_rec =  TipoRecebimentoPos.getRecebimentoPos(j.getString("STATUS_REC")) ;
        data = UtilSet.getData(j.getString("DATA")) ;
        pagamento_uuid = j.getString("PAGAMENTO_UUID") ;
        if (!j.isNull("NSU"))
           nsu = j.getString("NSU") ;
        recibo = j.getString("RECIBO") ;
    }


    public JSONObject geJSON() throws JSONException {
        SimpleDateFormat dta = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        JSONObject j = new JSONObject();
        if (id > 0)
          j.put("id", id);
        j.put("UUID", uuid);
        j.put("PAGAMENTO_ID", pagamento_id);
        j.put("CONTA_PAG_UUID", conta_pag_uuid);
        j.put("PAGAMENTO_UUID", pagamento_uuid);
        j.put("STATUS", status);
        j.put("STATUS_REC", status_rec.valor);
        j.put("DATA", dta.format(data));
        j.put("NSU", nsu);
        j.put("RECIBO", recibo);
        return j;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getPagamento_id() {
        return pagamento_id;
    }

    public void setPagamento_id(int pagamento_id) {
        this.pagamento_id = pagamento_id;
    }

    public String getConta_pag_uuid() {
        return conta_pag_uuid;
    }

    public void setConta_pag_uuid(String conta_pag_uuid) {
        this.conta_pag_uuid = conta_pag_uuid;
    }

    public TipoRecebimentoPos getStatus_rec() {
        return status_rec;
    }

    public void setStatus_rec(TipoRecebimentoPos status_rec) {
        this.status_rec = status_rec;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getPagamento_uuid() {
        return pagamento_uuid;
    }

    public void setPagamento_uuid(String pagamento_uuid) {
        this.pagamento_uuid = pagamento_uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNsu() {
        return nsu;
    }

    public void setNsu(String nsu) {
        this.nsu = nsu;
    }

    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }
}
