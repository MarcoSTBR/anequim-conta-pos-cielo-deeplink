package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaItem;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoTicketPedidoEdit;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControlePedidoTicketView;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItem;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAddTicketView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTicketView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTiket;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ControlePedidoTicketView {

    private Activity ctx ;
    private OnControlePedidoTicketView onControlePedidoTicketView;
    private int ponteiro ;
    private List<PedidoTicketView> list ;
    private List<PedidoTiket> listTickets ;

    public ControlePedidoTicketView(Activity ctx, OnControlePedidoTicketView onControlePedidoTicketView) {
        this.ctx = ctx;
        this.onControlePedidoTicketView = onControlePedidoTicketView;
    }

    public void setTicketsViews(List<PedidoTiket> l){
        listTickets = l ;
        list = new ArrayList<>() ;
        ponteiro = 0 ;
        if (listTickets.size() > 0){
            getTicket(listTickets.get(ponteiro)) ;
        } else onControlePedidoTicketView.onControleContaTicketViewOk(list);
    }

    private void getTicket(PedidoTiket pedidoTiket) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", pedidoTiket.getCONTA_ITEM_UUID()));
        try {
            new ConexaoContaItem(ctx, f, "") {
                @Override
                public void contaItemOk(List<ContaItem> l) {
                    if (l.size() > 0) {
                        addContaAdd(pedidoTiket, l.get(0)) ;
                    } else onControlePedidoTicketView.onControleContaTicketViewErro("Ticket Não Encontrado! "+pedidoTiket.getCONTA_ITEM_UUID());
                }

                @Override
                public void contaItemErro(String msg) {
                    onControlePedidoTicketView.onControleContaTicketViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePedidoTicketView.onControleContaTicketViewErro(e.getMessage());
        }
    }

    private void addContaAdd(PedidoTiket pedidoTiket, ContaItem contaItem) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID_ACOMP", "=", pedidoTiket.getCONTA_ITEM_UUID()));
        f.add(new DaoFilter("UUID", "<>", pedidoTiket.getCONTA_ITEM_UUID()));
        try {
            new ConexaoContaItem(ctx, f, "") {
                @Override
                public void contaItemOk(List<ContaItem> l) {
                        addContaTicket(pedidoTiket, contaItem, l) ;
                }

                @Override
                public void contaItemErro(String msg) {
                    onControlePedidoTicketView.onControleContaTicketViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePedidoTicketView.onControleContaTicketViewErro(e.getMessage());
        }
    }

    private void addContaTicket(PedidoTiket pedidoTiket, ContaItem contaItem, List<ContaItem> ladd) {
        Produto prd = ProdutoDao.getProdutoDao(ctx).getProduto(contaItem.getPRODUTO_ID()) ;
        List<PedidoItemAddTicketView> ldd = new ArrayList<>() ;
        for (ContaItem add : ladd) {
            Produto prdadd = ProdutoDao.getProdutoDao(ctx).getProduto(add.getPRODUTO_ID()) ;
            ldd.add(new PedidoItemAddTicketView(prdadd, add, add.getQUANTIDADE() / contaItem.getQUANTIDADE())) ;
        }
        list.add(new PedidoTicketView(prd, pedidoTiket, ldd)) ;
        next() ;
    }

    private void next(){
        Log.i("ticket", "ponteiro "+ponteiro+" de "+listTickets.size()) ;
        if (++ponteiro < listTickets.size())
            getTicket(listTickets.get(ponteiro)) ;
        else mudaStatus()  ;
    }

    private void mudaStatus(){
        ponteiro = 0 ;
        mudaStatus(listTickets.get(ponteiro));
    }

    private void mudaStatus(PedidoTiket pedidoTiket){
        pedidoTiket.setSTATUS(1);
        try {
            new ConexaoTicketPedidoEdit(ctx, pedidoTiket.getJSON()) {
                @Override
                public void conexaoPedidoTicketEditOk(PedidoTiket p) {
                    if (++ponteiro < listTickets.size())
                        mudaStatus(listTickets.get(ponteiro));
                      else onControlePedidoTicketView.onControleContaTicketViewOk(list);
                }

                @Override
                public void conexaoPedidoTicketEditErro(String msg) {
                    onControlePedidoTicketView.onControleContaTicketViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePedidoTicketView.onControleContaTicketViewErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControlePedidoTicketView.onControleContaTicketViewErro(e.getMessage());
        }
    }
}

