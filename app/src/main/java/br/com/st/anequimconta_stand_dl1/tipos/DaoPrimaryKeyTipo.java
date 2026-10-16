package br.com.st.anequimconta_stand_dl1.tipos;

public enum DaoPrimaryKeyTipo {
    tpkNormal("normal"), tpkMax("max"), tpkSerial("serial"), tpkUUID("uuid");

    public String descricao;

    DaoPrimaryKeyTipo(String d) {
        descricao = d ;
    }

}
