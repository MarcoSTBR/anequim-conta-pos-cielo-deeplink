package br.com.st.anequimconta_stand_dl1.pospag.nenhum;


import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;

import java.util.Date;

public class PosPagamentoViewNenhum implements PosPagamentoView {

    private ContaPag contaPag;
    private Modalidade modaldide ;

    public PosPagamentoViewNenhum(ContaPag contaPag, Modalidade modaldide) {
        this.contaPag = contaPag;
        this.modaldide = modaldide ;
    }

    @Override
    public String getId() {
        return contaPag.getUUID();
    }

    @Override
    public String getBandeiraNome() {
        return modaldide.getDescricao();
    }

    @Override
    public String getDescricaoStatus() {
        if (contaPag.getSTATUS() == 1)
           return "RECEBIDO";
        else return "CANCELADO";
    }

    @Override
    public String getLast4Digits() {
        return contaPag.getUUID().substring(1, 4) ;
    }

    @Override
    public Date getData() {
        return new Date();
    }

    @Override
    public Double getValor() {
        return contaPag.getVALOR();
    }
}
