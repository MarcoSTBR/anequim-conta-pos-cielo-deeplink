package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.dao.PedidoItemAddDao;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemDao;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAdd;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemView;

import org.json.JSONException;

public abstract class ControleVendaViewEdit {
    private Activity ctx ;

    public ControleVendaViewEdit(Activity ctx) {
        this.ctx = ctx;
    }

    public void alterarQuantidade(PedidoItemView it) {
        try {
            PedidoItemDao.getPedidoItemDao(ctx).update(it.getPedidoItem().geJSON());
            ok() ;
        } catch (JSONException e) {
            e.printStackTrace();
            erro(e.getMessage());
        }
    }

    public void cancelar(PedidoItemView it) {
         for (PedidoItemAdd itemAdd : it.getPedidoItemAdd()){
             PedidoItemAddDao.getPedidoItemAddDao(ctx).delete(itemAdd.getUUID());
            }
         PedidoItemDao.getPedidoItemDao(ctx).delete(it.getPedidoItem().getUUID());
         ok() ;
    }

    public abstract void ok() ;
    public abstract void erro(String msg) ;
}
