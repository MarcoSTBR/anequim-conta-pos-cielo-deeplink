package br.com.st.anequimconta_stand_dl1.entidade;

public class PedidoItemAddTicketView {

    private Produto produto ;
    private ContaItem contaItemAdd ;
    private Double Quantidade ;

    public PedidoItemAddTicketView(Produto produto, ContaItem contaItemAdd, Double quantidade) {
        this.produto = produto;
        this.contaItemAdd = contaItemAdd;
        Quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public ContaItem getContaItemAdd() {
        return contaItemAdd;
    }

    public void setContaItemAdd(ContaItem contaItemAdd) {
        this.contaItemAdd = contaItemAdd;
    }

    public Double getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(Double quantidade) {
        Quantidade = quantidade;
    }
}
