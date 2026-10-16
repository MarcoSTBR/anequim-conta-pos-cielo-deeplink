package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class PedidoView {

    private Pedido pedido ;
    private List<PedidoItemView> pedidoItemView ;

    public PedidoView(Pedido pedido, List<PedidoItemView> pedidoItemView) {
        this.pedido = pedido;
        this.pedidoItemView = pedidoItemView;
    }

    public Double getQuantidades(){
        Double q = 0.0 ;
        for (PedidoItemView it : pedidoItemView) {
           q += it.getPedidoItem().getQUANTIDADE() ;
           for (PedidoItemAdd itemAdd : it.getPedidoItemAdd())
               q += it.getPedidoItem().getQUANTIDADE() * itemAdd.getQUANTIDADE() ;
        }
        return q;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<PedidoItemView> getPedidoItemView() {
        return pedidoItemView;
    }

    public void setPedidoItemView(List<PedidoItemView> pedidoItemView) {
        this.pedidoItemView = pedidoItemView;
    }
}
