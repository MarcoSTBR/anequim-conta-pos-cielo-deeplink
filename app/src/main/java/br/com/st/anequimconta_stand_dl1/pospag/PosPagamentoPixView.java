package br.com.st.anequimconta_stand_dl1.pospag;

import org.json.JSONException;
import org.json.JSONObject;

public class PosPagamentoPixView {

    private String tx_id;
    private Double cob_value;
    private String status;
    private String pix_client_id;

    public PosPagamentoPixView(String tx_id, Double cob_value, String status, String pix_client_id) {
        this.tx_id = tx_id;
        this.cob_value = cob_value;
        this.status = status;
        this.pix_client_id = pix_client_id;
    }

    public PosPagamentoPixView() {
    }
     //{"code":"0","amount":"100","success":"true","itk":"PB0921B979955-3.9.6-SVJQLC-0004","type":"PIX","installment_count":"0","brand":"PIX","entry_mode":"QRC","atk":"C22056f5GQ8Gi3GkFbDb9LrEFJS","pan":"**************","cardholder_name":"Maria%20Paula%20Silva%20de%20Souza","order_id":"301639657","authorization_date_time":"30%2F04%2F2025%2019%3A39%3A37","account_id":"c2cce9c5-a86b-49d4-9df8-ba30959d945d","customer_wallet_provider_id":"0000000-0000-0000-0000-000000000000","instant_payment_id":"E10573521202101240332SWzE4lfRHDc"}
    public PosPagamentoPixView(JSONObject j) throws JSONException {
        tx_id = j.getString("tx_id");
        cob_value = j.getDouble("cob_value");
        status = j.getString("status");
        pix_client_id = j.getString("pix_client_id");
    }

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
    }

    public Double getCob_value() {
        return cob_value;
    }

    public void setCob_value(Double cob_value) {
        this.cob_value = cob_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPix_client_id() {
        return pix_client_id;
    }

    public void setPix_client_id(String pix_client_id) {
        this.pix_client_id = pix_client_id;
    }
}