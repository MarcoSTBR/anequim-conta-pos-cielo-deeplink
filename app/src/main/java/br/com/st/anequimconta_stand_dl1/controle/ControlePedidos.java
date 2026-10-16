package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControlePedidoApagar;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControlePedidoCorrente;
import br.com.st.anequimconta_stand_dl1.dao.PedidoDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Pedido;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControlePedidoAdd;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

import java.util.List;

public class ControlePedidos {

    private Activity ctx ;

    public ControlePedidos(Activity ctx) {
        this.ctx = ctx;
    }

    public void addPedido(String num_pedido,String referencia, ListenerControlePedidoAdd listenerControlePedidoAdd) {
        try {
            Pedido pedido = PedidoDao.getPedidoDao(ctx).getPedidoNumero(num_pedido);
            if (pedido == null) {
                pedido = new Pedido(UtilSet.getUUID(), num_pedido, referencia, 1, 1) ;
                PedidoDao.getPedidoDao(ctx).insert(pedido.geJSON());
                PedidoDao.getPedidoDao(ctx).setPedidoCorrente(pedido.getNUMERO());
            } else {
                pedido.setCORRENTE(1);
                PedidoDao.getPedidoDao(ctx).setPedidoCorrente(pedido.getNUMERO());
            }
            listenerControlePedidoAdd.pedidoOk(pedido);
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControlePedidoAdd.pedidoErro(e.getMessage());
        }
    }

    public void setCorrente(Pedido pedido, ListenerControlePedidoCorrente listenerControlePedidoCorrente) {
        PedidoDao.getPedidoDao(ctx).setPedidoCorrente(pedido.getNUMERO());
        listenerControlePedidoCorrente.pedidoCorrenteOk(pedido);
    }

    public void apagarPedido(String uuid, ListenerControlePedidoApagar listenerControlePedidoApagar) {
        int q = 0 ;
        Pedido pedido = PedidoDao.getPedidoDao(ctx).getPedidoID(uuid);
        if (pedido == null) {
            listenerControlePedidoApagar.pedidoApagarErro("Pedido Não Encontrado!");
        } else {
            boolean corrente = (pedido.getCORRENTE() == 1) ? true : false ;
            PedidoDao.getPedidoDao(ctx).delete(uuid);
            List<Pedido> l = PedidoDao.getPedidoDao(ctx).getList(new DaoFilters(), "") ;
            q = l.size() ;
            if (l.size() > 0) {
                if (corrente) {
                    PedidoDao.getPedidoDao(ctx).setPedidoCorrente(l.get(0).getNUMERO());
                }
            }
            listenerControlePedidoApagar.pedidoApagado(q);
        }
    }

}
