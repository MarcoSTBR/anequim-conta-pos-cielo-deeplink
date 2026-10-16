package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;


import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleGradeProduto;
import br.com.st.anequimconta_stand_dl1.dao.PedidoDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemDao;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.Pedido;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

public class ControleGradeProduto {
    private Activity ctx;
    private PedidoView pedidoView;
    private Pedido pedido;

    private ListenerControleGradeProduto listenerControleGradeProduto;

    public ControleGradeProduto(Activity ctx) {
        this.ctx = ctx;
    }

    public boolean caixaAberto() {
        return true ;//caixa == null ? false : true;
    }

    public void refresh() {
        vendaAtual();
    }

    public void vendaAtual() {
            pedido = PedidoDao.getPedidoDao(ctx).getPedidoCorrente();
            if (pedido == null) {
                pedido = null;
            } else {
                pedidoView = new ControlePedidoView(ctx).getPedido(pedido);
            }
    }

    public Double getQuantidade(Produto p) {
        Double q = 0.0;
        if (pedidoView != null) {
            for (PedidoItemView it : pedidoView.getPedidoItemView()) {
                if (p.getId() == it.getPedidoItem().getPRODUTO_ID()) {
                    q += it.getPedidoItem().getQUANTIDADE();
                }
            }
        }
        return q;
    }

    public void addProduto(Produto p, Double q, ListenerControleGradeProduto listenerControleGradeProduto) {
        this.listenerControleGradeProduto = listenerControleGradeProduto;
        if (pedido != null) {
            addProdutoAberto(p, q);
        } else {
            listenerControleGradeProduto.OnErro("Nenhum Pedido Aberto!");
        }
    }

    private void addProdutoAberto(Produto p, Double q) {
        PedidoItem it = null;
        if (!(p.getUnidade().substring(0,1).equals("K") || p.getUnidade().substring(0,1).equals("L"))) {
            for (PedidoItemView itv : pedidoView.getPedidoItemView()) {
                if (p.getId() == itv.getPedidoItem().getPRODUTO_ID()) {
                    it = itv.getPedidoItem();
                }
            }
        }
        try {
            if (it == null) {
                it = new PedidoItem(UtilSet.getUUID(), pedido.getUUID(),
                        p.getId(), q, p.getPreco(), 0, 0, q * p.getPreco(), 1, "", 0);
                PedidoItemDao.getPedidoItemDao(ctx).insert(it);
            } else {
                Produto prd = ProdutoDao.getProdutoDao(ctx).getProduto(it.getPRODUTO_ID()) ;
                Double quant = it.getQUANTIDADE() + q;
                Double comissao = (quant * p.getPreco()) * (prd.getComissao() / 100);
                it.setQUANTIDADE(quant);
                it.setVALOR(quant * p.getPreco());
                it.setCOMISSAO(comissao);
                PedidoItemDao.getPedidoItemDao(ctx).update(it.geJSON());
            }
            listenerControleGradeProduto.OnProdutoAdd(p);
            refresh();
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControleGradeProduto.OnErro(e.getMessage());
        }
    }


}
