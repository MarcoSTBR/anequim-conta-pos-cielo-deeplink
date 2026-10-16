package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class AcompanhamentoView {

    private Acompanhamento acompanhamento;
    private List<AcompanhamentoItemView> acompanhamentoItems ;

    public AcompanhamentoView(Acompanhamento acompanhamento, List<AcompanhamentoItemView> acompanhamentoItems) {
        this.acompanhamento = acompanhamento;
        this.acompanhamentoItems = acompanhamentoItems;
    }

    public Acompanhamento getAcompanhamento() {
        return acompanhamento;
    }

    public void setAcompanhamento(Acompanhamento acompanhamento) {
        this.acompanhamento = acompanhamento;
    }

    public List<AcompanhamentoItemView> getAcompanhamentoItems() {
        return acompanhamentoItems;
    }

    public void setAcompanhamentoItems(List<AcompanhamentoItemView> acompanhamentoItems) {
        this.acompanhamentoItems = acompanhamentoItems;
    }
}
