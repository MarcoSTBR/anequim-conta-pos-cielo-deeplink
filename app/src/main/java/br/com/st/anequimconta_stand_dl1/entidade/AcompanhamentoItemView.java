package br.com.st.anequimconta_stand_dl1.entidade;

public class AcompanhamentoItemView {

    private AcompanhamentoItem acompanhamentoItem ;
    private int quantidade ;

    public AcompanhamentoItemView(AcompanhamentoItem acompanhamentoItem, int quantidade) {
        this.acompanhamentoItem = acompanhamentoItem;
        this.quantidade = quantidade;
    }

    public AcompanhamentoItem getAcompanhamentoItem() {
        return acompanhamentoItem;
    }

    public void setAcompanhamentoItem(AcompanhamentoItem acompanhamentoItem) {
        this.acompanhamentoItem = acompanhamentoItem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
