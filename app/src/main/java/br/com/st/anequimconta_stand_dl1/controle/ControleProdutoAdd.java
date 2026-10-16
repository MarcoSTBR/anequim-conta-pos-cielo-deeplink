package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenrControleVenda;
import br.com.st.anequimconta_stand_dl1.dao.PedidoDao;
import br.com.st.anequimconta_stand_dl1.entidade.Pedido;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

public class ControleProdutoAdd {

    private Activity ctx;
    private ListenrControleVenda listenerControleVenda;
    private PedidoItem pedidoItem;
    private Pedido pedido;

    public ControleProdutoAdd(Activity ctx, ListenrControleVenda listenerControleVenda) {
        this.ctx = ctx;
        this.listenerControleVenda = listenerControleVenda;
    }

    public void addProduto(Produto p, Double quantidade) {
        try {
            if (quantidade == 0) throw new Exception("Quantidade Inválida!") ;
            Double q = quantidade;
            Double preco = p.getPreco();
            pedido = PedidoDao.getPedidoDao(ctx).getPedidoCorrente();
            if (pedido == null)
                throw new Exception("Nenhum Pedido Aberto!") ;
            else {

                Double comissao = (q * preco) * (p.getComissao() / 100) ;
                PedidoItem it = new PedidoItem(UtilSet.getUUID(), pedido.getUUID(),
                        p.getId(), q, preco, 0, comissao, q * preco, 1, "", getIndice());
                listenerControleVenda.vendaOk(it);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControleVenda.vendaErro(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            listenerControleVenda.vendaErro(e.getMessage());
        }
    }

    private int getIndice(){
        return 0 ;
    }

}
