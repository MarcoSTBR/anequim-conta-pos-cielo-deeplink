package br.com.st.anequimconta_stand_dl1.pospag;

public enum TipoTrasacCielo {

    DEBIT("DEBIT"), CREDIT("CREDIT"), PIX("PIX"), VOUCHER("VOUCHER");

    public String valor ;

    TipoTrasacCielo(String valor) {
        this.valor = valor;
    }
}
