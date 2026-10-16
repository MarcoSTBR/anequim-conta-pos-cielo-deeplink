package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.dao.PedidoDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemAddDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemDao;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoView;
import br.com.st.anequimconta_stand_dl1.entidade.Pedido;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAdd;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

import java.util.List;

public abstract class ControleGravarVendaItem {

    private Activity ctx;
    private Pedido pedido ;

    public ControleGravarVendaItem(Activity ctx) {
        this.ctx = ctx;
    }

    public void gravarVendaItem(PedidoItem it, List<AcompanhamentoView> acs) {
        try {
            pedido = PedidoDao.getPedidoDao(ctx).getPedidoCorrente() ;// vendaVista = getVendaVista();
            if (pedido.getUUID().equals(it.getPEDIDO_UUID())) {
                gravar(it, acs);
                setVendaItemOK() ;
            } else throw new Exception("Venda Diferente da referência do item!");
        } catch (Exception e) {
            e.printStackTrace();
            erroNoItem(e.getMessage());
        }
    }

    private void gravar(PedidoItem it, List<AcompanhamentoView> acs) throws JSONException {
        //vendaVista.setDATA(new Date());
       // PedidoDao.getPedidoDao(ctx).update(it.geJSON());
        PedidoItemDao.getPedidoItemDao(ctx).insert(it);
        int count = 0;
        for (AcompanhamentoView a : acs) {
            for (AcompanhamentoItemView acvi : a.getAcompanhamentoItems()) {
                if (acvi.getQuantidade() > 0) {
                    Double comisao = 0.0 ;
                    PedidoItemAdd itemAdd = new PedidoItemAdd(UtilSet.getUUID(), it.getUUID(), acvi.getAcompanhamentoItem().getProduto_id(),
                            acvi.getQuantidade(), acvi.getAcompanhamentoItem().getPreco(),
                            0, comisao, acvi.getQuantidade() * acvi.getAcompanhamentoItem().getPreco(),
                            1, "", ++count);
                    PedidoItemAddDao.getPedidoItemAddDao(ctx).insert(itemAdd);
                }
            }
        }

    }

    public abstract void setVendaItemOK();

    public abstract void erroNoItem(String msg);
}
