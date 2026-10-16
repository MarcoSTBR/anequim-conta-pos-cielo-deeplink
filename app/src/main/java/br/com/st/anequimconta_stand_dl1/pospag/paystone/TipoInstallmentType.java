package br.com.st.anequimconta_stand_dl1.pospag.paystone;

public enum TipoInstallmentType {

    MERCHANT("MERCHANT"), ISSUER("ISSUER"), NONE("NONE");

    public String valor ;

    TipoInstallmentType(String valor) {
        this.valor = valor ;
    }
}
