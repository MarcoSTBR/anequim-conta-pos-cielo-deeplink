package br.com.st.anequimconta_stand_dl1.daoclass;

public enum DaoPrimaryKeyTipo {
    tpkNormal("normal"), tpkMax("max"), tpkSerial("serial"), tpkUUID("uuid");

    public String descricao;

    DaoPrimaryKeyTipo(String d) {
        descricao = d ;
    }

}
