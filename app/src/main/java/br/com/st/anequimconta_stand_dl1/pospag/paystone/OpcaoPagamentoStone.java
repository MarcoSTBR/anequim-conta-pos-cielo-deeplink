package br.com.st.anequimconta_stand_dl1.pospag.paystone;

public class OpcaoPagamentoStone {


    private TipoInstallmentType tipoInstallmentType ;
    private TipoTrasacStone tipoTrasacStone ;
    private String descricao ;

    public OpcaoPagamentoStone(TipoInstallmentType tipoInstallmentType, TipoTrasacStone tipoTrasacStone, String descricao) {
        this.tipoInstallmentType = tipoInstallmentType;
        this.tipoTrasacStone = tipoTrasacStone;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoInstallmentType getTipoInstallmentType() {
        return tipoInstallmentType;
    }

    public void setTipoInstallmentType(TipoInstallmentType tipoInstallmentType) {
        this.tipoInstallmentType = tipoInstallmentType;
    }

    public TipoTrasacStone getTipoTrasacStone() {
        return tipoTrasacStone;
    }

    public void setTipoTrasacStone(TipoTrasacStone tipoTrasacStone) {
        this.tipoTrasacStone = tipoTrasacStone;
    }
}
