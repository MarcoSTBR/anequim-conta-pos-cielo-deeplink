package br.com.st.anequimconta_stand_dl1.pospag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosFilter {
    private Date dtaInicial ;
    private Date dtaFinal ;
    private String PaymentId ;
    private List<TipoRecebimentoPos> tipoRecebimentoPos;

    public PosFilter() {
        PaymentId = null ;
        dtaInicial = null ;
        tipoRecebimentoPos = new ArrayList<>();
    }

    public Date getDtaInicial() {
        return dtaInicial;
    }

    public void setDtaInicial(Date dtaInicial) {
        this.dtaInicial = dtaInicial;
    }

    public Date getDtaFinal() {
        return dtaFinal;
    }

    public void setDtaFinal(Date dtaFinal) {
        this.dtaFinal = dtaFinal;
    }

    public String getPaymentId() {
        return PaymentId ;
    }

    public void setPaymentId(String paymentId) {
        PaymentId = paymentId;
    }

    public List<TipoRecebimentoPos> getTipoRecebimentoPos() {
        return tipoRecebimentoPos;
    }

    public void setTipoRecebimentoPos(List<TipoRecebimentoPos> tipoRecebimentoPos) {
        this.tipoRecebimentoPos = tipoRecebimentoPos;
    }
}
