package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.dao.PedidoDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemAddDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Pedido;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAdd;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

import java.util.ArrayList;
import java.util.List;

public class ControlePedidoView {

    private Activity ctx;

    public ControlePedidoView(Activity ctx) {
        this.ctx = ctx;
    }

    public PedidoView getPedido(Pedido pedido){
       return new PedidoView(pedido, getListItems(pedido)) ;
    }

    public PedidoView getPedido(String uuid){
        Pedido pedido = PedidoDao.getPedidoDao(ctx).getPedidoID(uuid) ;
        if (pedido != null) {
            return new PedidoView(pedido, getListItems(pedido));
        }
        else return null ;
    }

    public List<PedidoView> getPedidos(){
        List<PedidoView> list = new ArrayList<>() ;
        DaoFilters f = new DaoFilters() ;
        List<Pedido> l = PedidoDao.getPedidoDao(ctx).getList(f, "") ;
        for (Pedido pedido : l)
            list.add(new PedidoView(pedido, getListItems(pedido))) ;
        return list ;
    }


    private List<PedidoItemView> getListItems(Pedido pedido){
        List<PedidoItemView> list = new ArrayList<PedidoItemView>() ;
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("PEDIDO_UUID", "=", pedido.getUUID()));
        List<PedidoItem> l = PedidoItemDao.getPedidoItemDao(ctx).getList(f, "") ;
        for (PedidoItem it : l){
            list.add(new PedidoItemView(it, getPedidoItemAdd(it))) ;
        }
        return list ;
    }

    private List<PedidoItemAdd> getPedidoItemAdd(PedidoItem pedidoItem){
      DaoFilters f = new DaoFilters() ;
      f.add(new DaoFilter("PEDIDO_ITEM_UUID", "=", pedidoItem.getUUID()));
      return PedidoItemAddDao.getPedidoItemAddDao(ctx).getList(f, "") ;
    }

    public PedidoView getCorrente() {
        PedidoView pedidoView = null ;
        Pedido pedido = PedidoDao.getPedidoDao(ctx).getPedidoCorrente() ;
        if (pedido != null) pedidoView = new PedidoView(pedido, getListItems(pedido));
        return pedidoView ;
    }
}
