package br.com.st.anequimconta_stand_dl1.pospag;

public enum PosPagType {

    posNenhum("Nenhum"), posCielo("PayCielo") ;

    public String valor;

    public static PosPagType getValor(String s){
        for (PosPagType p : PosPagType.values()){
            if (p.valor.equals(s))
            return p ;
        }
        throw new IllegalArgumentException() ;
    }
    PosPagType(String valor) {
        this.valor = valor;
    }
}
