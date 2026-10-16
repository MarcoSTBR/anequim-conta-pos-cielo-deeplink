package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class PedidoTicketView {

    private Produto produto ;
    private PedidoTiket pedidoTiket ;
    private List<PedidoItemAddTicketView> pedidoItemAddTicketViews;

    public PedidoTicketView(Produto produto, PedidoTiket pedidoTiket, List<PedidoItemAddTicketView> pedidoItemAddTicketViews) {
        this.produto = produto;
        this.pedidoTiket = pedidoTiket;
        this.pedidoItemAddTicketViews = pedidoItemAddTicketViews;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<PedidoItemAddTicketView> getPedidoItemAddTicketViews() {
        return pedidoItemAddTicketViews;
    }

    public void setPedidoItemAddTicketViews(List<PedidoItemAddTicketView> pedidoItemAddTicketViews) {
        this.pedidoItemAddTicketViews = pedidoItemAddTicketViews;
    }

    public PedidoTiket getPedidoTiket() {
        return pedidoTiket;
    }

    public void setPedidoTiket(PedidoTiket pedidoTiket) {
        this.pedidoTiket = pedidoTiket;
    }
}
