package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.dao.AcompanhamentoDao;
import br.com.st.anequimconta_stand_dl1.dao.AcompanhamentoItemDao;
import br.com.st.anequimconta_stand_dl1.dao.AcompanhamentoProdutoDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Acompanhamento;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoItem;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoProduto;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ControleAcompanhamento {
    private static ControleAcompanhamento controleAcompanhamento;

    public static ControleAcompanhamento getControleAcompanhamento(Activity ctx) {
        if (controleAcompanhamento == null) {
            controleAcompanhamento = new ControleAcompanhamento(ctx);
            return controleAcompanhamento;
        } else {
            controleAcompanhamento.setCtx(ctx) ;
            return controleAcompanhamento;
        }
    }

    private Activity ctx;
    private static List<AcompanhamentoView> list;

    public void setCtx(Activity ctx) {
        this.ctx = ctx;
    }

    public ControleAcompanhamento(Activity ctx) {
        this.ctx = ctx;
    }

    public List<AcompanhamentoView> getList() {
        return list;
    }

    public List<AcompanhamentoView> list(Produto p) {
        list = new ArrayList<AcompanhamentoView>();
        List<Acompanhamento> lac = getAcompanhamentos(p.getId());
        for (Acompanhamento ac : lac) {
            list.add(new AcompanhamentoView(ac, getAcompItens(ac)));
        }
        return list;
    }

    private List<Acompanhamento> getAcompanhamentos(int produto_id) {
        List<Acompanhamento> l = new ArrayList<Acompanhamento>();
        try {
            DaoFilters f = new DaoFilters();
            f.add(new DaoFilter("AFOOD_PRODUTO_ID", "=", String.valueOf(produto_id)));
            List<AcompanhamentoProduto> lprd = AcompanhamentoProdutoDao.getAcompanhamentoProdutoDao(ctx).getList(f, "id");
            for (AcompanhamentoProduto ap : lprd) {
                Log.i("ap_", "getAcomp_id() " + ap.getAcomp_id());
                l.add(new Acompanhamento(AcompanhamentoDao.getAcompanhamentoDao(ctx).get(String.valueOf(ap.getAcomp_id()))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l;
    }

    private List<AcompanhamentoItemView> getAcompItens(Acompanhamento a) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("AFOOD_ACOMP_ID", "=", String.valueOf(a.getId())));
        try {
            return getItenView(AcompanhamentoItemDao.getAcompanhamentoItemDao(ctx).getList(f, "id"));
        } catch (JSONException e) {
            e.printStackTrace();
            // throw new RuntimeException(e);
            return new ArrayList<>();
        }
    }

    private List<AcompanhamentoItemView> getItenView(List<AcompanhamentoItem> its) {
        List<AcompanhamentoItemView> itensv = new ArrayList<AcompanhamentoItemView>();
        for (AcompanhamentoItem i : its) {
            itensv.add(new AcompanhamentoItemView(i, 0));
        }
        return itensv;
    }

}
