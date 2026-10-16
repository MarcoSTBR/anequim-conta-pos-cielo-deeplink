package br.com.st.anequimconta_stand_dl1.entidade;

public class ProdutoSelectAcompanhamentoItem {

    private AcompanhamentoItem acompanhamentoItem ;
    private PedidoItemAdd pedidoItemAdd ;

    public ProdutoSelectAcompanhamentoItem(AcompanhamentoItem acompanhamentoItem, PedidoItemAdd pedidoItemAdd) {
        this.acompanhamentoItem = acompanhamentoItem;
        this.pedidoItemAdd = pedidoItemAdd;
    }

    public AcompanhamentoItem getAcompanhamentoItem() {
        return acompanhamentoItem;
    }

    public void setAcompanhamentoItem(AcompanhamentoItem acompanhamentoItem) {
        this.acompanhamentoItem = acompanhamentoItem;
    }

    public PedidoItemAdd getPedidoItemAdd() {
        return pedidoItemAdd;
    }

    public void setPedidoItemAdd(PedidoItemAdd pedidoItemAdd) {
        this.pedidoItemAdd = pedidoItemAdd;
    }
}
