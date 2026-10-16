package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public class PedidoItemAdd implements Entidade {

    private String UUID ;
    private String PEDIDO_ITEM_UUID;
    private int PRODUTO_ID ;
    private double QUANTIDADE ;
    private double PRECO ;
    private double DESCONTO ;
    private double COMISSAO ;
    private double VALOR ;
    private int STATUS ;
    private String OBS ;
    private int INDICE ;

    public PedidoItemAdd(String UUID, String PEDIDO_ITEM_UUID, int PRODUTO_ID, double QUANTIDADE, double PRECO, double DESCONTO, double COMISSAO, double VALOR, int STATUS, String OBS, int INDICE) {
        this.UUID = UUID;
        this.PEDIDO_ITEM_UUID = PEDIDO_ITEM_UUID;
        this.PRODUTO_ID = PRODUTO_ID;
        this.QUANTIDADE = QUANTIDADE;
        this.PRECO = PRECO;
        this.DESCONTO = DESCONTO;
        this.COMISSAO = COMISSAO;
        this.VALOR = VALOR;
        this.STATUS = STATUS;
        this.OBS = OBS;
        this.INDICE = INDICE;
    }


    public PedidoItemAdd(JSONObject j) throws JSONException {
        UUID = j.getString("UUID");
        PEDIDO_ITEM_UUID = j.getString("PEDIDO_ITEM_UUID");
        PRODUTO_ID = j.getInt("PRODUTO_ID");
        QUANTIDADE = j.getDouble("QUANTIDADE");
        PRECO = j.getDouble("PRECO");
        DESCONTO = j.getDouble("DESCONTO");
        COMISSAO = j.getDouble("COMISSAO");
        VALOR = j.getDouble("VALOR");
        STATUS = j.getInt("STATUS");
        OBS = j.getString("OBS");
        INDICE = j.getInt("INDICE");
    }



    public JSONObject geJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("UUID", UUID) ;
        j.put("PEDIDO_ITEM_UUID", PEDIDO_ITEM_UUID) ;
        j.put("PRODUTO_ID", PRODUTO_ID) ;
        j.put("QUANTIDADE", QUANTIDADE) ;
        j.put("PRECO", PRECO) ;
        j.put("DESCONTO", DESCONTO) ;
        j.put("COMISSAO", COMISSAO) ;
        j.put("VALOR", VALOR) ;
        j.put("STATUS", STATUS) ;
        j.put("OBS", OBS) ;
        j.put("INDICE",INDICE) ;
        return j;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getPEDIDO_ITEM_UUID() {
        return PEDIDO_ITEM_UUID;
    }

    public void setPEDIDO_ITEM_UUID(String PEDIDO_ITEM_UUID) {
        this.PEDIDO_ITEM_UUID = PEDIDO_ITEM_UUID;
    }

    public int getPRODUTO_ID() {
        return PRODUTO_ID;
    }

    public void setPRODUTO_ID(int PRODUTO_ID) {
        this.PRODUTO_ID = PRODUTO_ID;
    }

    public double getQUANTIDADE() {
        return QUANTIDADE;
    }

    public void setQUANTIDADE(double QUANTIDADE) {
        this.QUANTIDADE = QUANTIDADE;
    }

    public double getPRECO() {
        return PRECO;
    }

    public void setPRECO(double PRECO) {
        this.PRECO = PRECO;
    }

    public double getDESCONTO() {
        return DESCONTO;
    }

    public void setDESCONTO(double DESCONTO) {
        this.DESCONTO = DESCONTO;
    }

    public double getCOMISSAO() {
        return COMISSAO;
    }

    public void setCOMISSAO(double COMISSAO) {
        this.COMISSAO = COMISSAO;
    }

    public double getVALOR() {
        return VALOR;
    }

    public void setVALOR(double VALOR) {
        this.VALOR = VALOR;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public String getOBS() {
        return OBS;
    }

    public void setOBS(String OBS) {
        this.OBS = OBS;
    }

    public int getINDICE() {
        return INDICE;
    }

    public void setINDICE(int INDICE) {
        this.INDICE = INDICE;
    }
}
