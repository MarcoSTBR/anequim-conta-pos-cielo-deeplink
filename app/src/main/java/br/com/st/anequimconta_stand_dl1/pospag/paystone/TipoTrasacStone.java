package br.com.st.anequimconta_stand_dl1.pospag.paystone;

public enum TipoTrasacStone {

    DEBIT("DEBIT"), CREDIT("CREDIT"), PIX("PIX"), VOUCHER("VOUCHER");

    public String valor ;

    TipoTrasacStone(String valor) {
        this.valor = valor;
    }
}
