package br.com.st.anequimconta_stand_dl1.entidade;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Conta {

    private int id ;
    private String UUID ;
    private String NUMERO ;
    private String REFERENCIA ;
    private Date DATA ;
    private Double DESCONTO ;
    private int NUM_IMPRESSAO ;
    private int TRAVA ;
    private Date DATA_FECHAMENTO ;
    private int NUM_PESSOAS ;
    private int USUARIO_ID ;
    private int TERMINAL_ID ;
    private int STATUS_COMISSAO ;
    private int STATUS ;
    private int IMPORT ;

    public Conta(JSONObject j) throws JSONException {
        id               = j.getInt("id") ;
        UUID             = j.getString("UUID") ;
        NUMERO           = j.getString("NUMERO") ;
        REFERENCIA       = j.getString("REFERENCIA") ;
        DATA             = UtilSet.getData(j.getString("DATA")) ;
        DESCONTO         = j.getDouble("DESCONTO") ;
        NUM_IMPRESSAO    = j.getInt("NUM_IMPRESSAO") ;
        TRAVA            = j.getInt("TRAVA") ;
        DATA_FECHAMENTO  = UtilSet.getData(j.getString("DATA_FECHAMENTO")) ;
        NUM_PESSOAS      = j.getInt("NUM_PESSOAS") ;
        USUARIO_ID       = j.getInt("USUARIO_ID") ;
        TERMINAL_ID      = j.getInt("TERMINAL_ID") ;
        STATUS_COMISSAO  = j.getInt("STATUS_COMISSAO") ;
        STATUS           = j.getInt("STATUS") ;
        IMPORT           = j.getInt("IMPORT") ;
    }

    public JSONObject getJSON() throws JSONException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject j = new JSONObject();
        j.put("id", id) ;
        j.put("UUID", UUID) ;
        j.put("NUMERO", NUMERO) ;
        j.put("REFERENCIA", REFERENCIA) ;
        j.put("DATA", df.format(DATA)) ;
        j.put("DESCONTO", DESCONTO) ;
        j.put("NUM_IMPRESSAO", NUM_IMPRESSAO) ;
        j.put("TRAVA", TRAVA) ;
        j.put("DATA_FECHAMENTO", df.format(DATA_FECHAMENTO)) ;
        j.put("NUM_PESSOAS", NUM_PESSOAS) ;
        j.put("USUARIO_ID", USUARIO_ID) ;
        j.put("TERMINAL_ID", TERMINAL_ID) ;
        j.put("STATUS_COMISSAO", STATUS_COMISSAO) ;
        j.put("STATUS", STATUS) ;
        j.put("IMPORT", IMPORT) ;
        return j;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
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

    public String getNUMERO() {
        return NUMERO;
    }

    public void setNUMERO(String NUMERO) {
        this.NUMERO = NUMERO;
    }

    public Date getDATA() {
        return DATA;
    }

    public void setDATA(Date DATA) {
        this.DATA = DATA;
    }

    public Double getDESCONTO() {
        return DESCONTO;
    }

    public void setDESCONTO(Double DESCONTO) {
        this.DESCONTO = DESCONTO;
    }

    public int getNUM_IMPRESSAO() {
        return NUM_IMPRESSAO;
    }

    public void setNUM_IMPRESSAO(int NUM_IMPRESSAO) {
        this.NUM_IMPRESSAO = NUM_IMPRESSAO;
    }

    public int getTRAVA() {
        return TRAVA;
    }

    public void setTRAVA(int TRAVA) {
        this.TRAVA = TRAVA;
    }

    public Date getDATA_FECHAMENTO() {
        return DATA_FECHAMENTO;
    }

    public void setDATA_FECHAMENTO(Date DATA_FECHAMENTO) {
        this.DATA_FECHAMENTO = DATA_FECHAMENTO;
    }

    public int getNUM_PESSOAS() {
        return NUM_PESSOAS;
    }

    public void setNUM_PESSOAS(int NUM_PESSOAS) {
        this.NUM_PESSOAS = NUM_PESSOAS;
    }

    public int getUSUARIO_ID() {
        return USUARIO_ID;
    }

    public void setUSUARIO_ID(int USUARIO_ID) {
        this.USUARIO_ID = USUARIO_ID;
    }

    public int getTERMINAL_ID() {
        return TERMINAL_ID;
    }

    public void setTERMINAL_ID(int TERMINAL_ID) {
        this.TERMINAL_ID = TERMINAL_ID;
    }

    public int getSTATUS_COMISSAO() {
        return STATUS_COMISSAO;
    }

    public void setSTATUS_COMISSAO(int STATUS_COMISSAO) {
        this.STATUS_COMISSAO = STATUS_COMISSAO;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public String getREFERENCIA() {
        return REFERENCIA;
    }

    public void setREFERENCIA(String REFERENCIA) {
        this.REFERENCIA = REFERENCIA;
    }


    public static class NumeroComparador implements Comparator<Conta> {

        @Override
        public int compare(Conta conta1, Conta conta2) {
            int c1 = Integer.parseInt(conta1.getNUMERO()) ;
            int c2 = Integer.parseInt(conta2.getNUMERO()) ;
            return Integer.compare(c1, c2);
        }
    }

}
