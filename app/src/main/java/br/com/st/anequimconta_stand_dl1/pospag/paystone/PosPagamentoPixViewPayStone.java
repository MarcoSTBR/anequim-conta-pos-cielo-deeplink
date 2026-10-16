package br.com.st.anequimconta_stand_dl1.pospag.paystone;


import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPosPix;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPixView;

public class PosPagamentoPixViewPayStone extends PosPagamentoPixView {

    private String tx_id;
    private Double cob_value;
    private String status;
    private String pix_client_id;
    private Modalidade modalidade ;
    private ContaPag contaPag;
    private RecebimentoPosPix recebimentoPosPix ;

    public PosPagamentoPixViewPayStone(Modalidade modalidade, RecebimentoPosPix recebimentoPosPix, ContaPag contaPag) {
        this.modalidade = modalidade;
        this.recebimentoPosPix = recebimentoPosPix;
        this.contaPag = contaPag;
        this.tx_id = recebimentoPosPix.getUUID();
        this.cob_value = contaPag.getVALOR();
        this.status = contaPag.getSTATUS() == 1 ? "RECCEBIDO" : "CANCELADO";
        this.pix_client_id = recebimentoPosPix.getPAGAMENTO_UUID();
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