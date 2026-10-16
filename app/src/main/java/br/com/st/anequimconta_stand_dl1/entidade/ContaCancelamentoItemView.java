package br.com.st.anequimconta_stand_dl1.entidade;

public class ContaCancelamentoItemView {
    private ContaItemView contaItemView ;
    private Double quantidade ;

    public ContaCancelamentoItemView(ContaItemView contaItemView, Double quantidade) {
        this.contaItemView = contaItemView;
        this.quantidade = quantidade;
    }

    public ContaItemView getContaItemView() {
        return contaItemView;
    }

    public void setContaItemView(ContaItemView contaItemView) {
        this.contaItemView = contaItemView;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
