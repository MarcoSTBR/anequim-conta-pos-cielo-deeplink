package br.com.st.anequimconta_stand_dl1.impressao.tipos;

public enum TipoImpressaoPos {

    GENERICO("GENERICO"), A910("A910"), L300("L300"), L400("L400"), TIANYU("TIANYU"),
    SUNMIP2("SUNMIP2"), DX8000("DX8000") ;

    public String descricao;

    public static TipoImpressaoPos getTipoImpressaoPos(String s){
        for (TipoImpressaoPos tp : TipoImpressaoPos.values()){
            if (tp.descricao.equals(s))
                return tp ;
        }
        return TipoImpressaoPos.GENERICO ;
    }

    TipoImpressaoPos(String d) {
        descricao = d ;
    }
}
