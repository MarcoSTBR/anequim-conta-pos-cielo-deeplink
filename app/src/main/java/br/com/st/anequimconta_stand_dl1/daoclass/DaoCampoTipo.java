package br.com.st.anequimconta_stand_dl1.daoclass;

public enum DaoCampoTipo {
    dbString("String"), dbInt("Inteiro"), dbDateTime("Data Hora"),
    dbDecimal("Decimal") ;

    public String descricao ;
    DaoCampoTipo(String s){
        descricao = s ;
    }


}
