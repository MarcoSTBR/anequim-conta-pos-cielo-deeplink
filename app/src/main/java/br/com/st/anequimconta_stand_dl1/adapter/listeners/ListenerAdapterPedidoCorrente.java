package br.com.st.anequimconta_stand_dl1.adapter.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

public interface ListenerAdapterPedidoCorrente {
    void pedidoSelecionado(PedidoView pedidoView);
    void apagarPedido(PedidoView pedidoView);
}
