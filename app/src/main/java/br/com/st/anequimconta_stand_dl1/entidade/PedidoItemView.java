package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class PedidoItemView {
    private PedidoItem pedidoItem ;
    private List<PedidoItemAdd> pedidoItemAdd ;

    public PedidoItemView(PedidoItem pedidoItem, List<PedidoItemAdd> pedidoItemAdd) {
        this.pedidoItem = pedidoItem;
        this.pedidoItemAdd = pedidoItemAdd;
    }

    public PedidoItem getPedidoItem() {
        return pedidoItem;
    }

    public void setPedidoItem(PedidoItem pedidoItem) {
        this.pedidoItem = pedidoItem;
    }

    public List<PedidoItemAdd> getPedidoItemAdd() {
        return pedidoItemAdd;
    }

    public void setPedidoItemAdd(List<PedidoItemAdd> pedidoItemAdd) {
        this.pedidoItemAdd = pedidoItemAdd;
    }
}
