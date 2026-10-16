package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class ProdutoAcompanhamento {

    private Acompanhamento acompanhamento ;
    private List<ProdutoSelectAcompanhamentoItem> listAcompanhamentoItem ;

    public ProdutoAcompanhamento(Acompanhamento acompanhamento, List<ProdutoSelectAcompanhamentoItem> listAcompanhamentoItem) {
        this.acompanhamento = acompanhamento;
        this.listAcompanhamentoItem = listAcompanhamentoItem;
    }

    public Acompanhamento getAcompanhamento() {
        return acompanhamento;
    }

    public void setAcompanhamento(Acompanhamento acompanhamento) {
        this.acompanhamento = acompanhamento;
    }

    public List<ProdutoSelectAcompanhamentoItem> getListAcompanhamentoItem() {
        return listAcompanhamentoItem;
    }

    public void setListAcompanhamentoItem(List<ProdutoSelectAcompanhamentoItem> listAcompanhamentoItem) {
        this.listAcompanhamentoItem = listAcompanhamentoItem;
    }
}
