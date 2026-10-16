package br.com.st.anequimconta_stand_dl1.pospag;

public enum TipoRecebimentoPos {
    tr_pendete("Pendente"),
    tr_confirmado("Confirmado"),
    tr_canelado("Cancelado"),
    tr_revertido("Revertido"),
    tr_negado("Negado"),
    tr_inacessivel("inacessível"),
    tr_validando("Validando"),
    tr_capturando("Capiturando"),
    tr_processando("Processando"),
    tr_reembolsado("Reembolsado") ;

    public String valor;

    public static TipoRecebimentoPos getRecebimentoPos(String s){
        for (TipoRecebimentoPos tp : values()){
            if (s.equals(tp.valor)){
                return tp ;
            }
        }
        return tr_pendete ;
      //  throw new IllegalArgumentException("Tipo "+s+" Não Encontrado") ;
    }

    TipoRecebimentoPos(String valor) {
        this.valor = valor;
    }
}

