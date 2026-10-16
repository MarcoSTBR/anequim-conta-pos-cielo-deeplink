package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.dao.GradeVendasDao;
import br.com.st.anequimconta_stand_dl1.dao.GradeVendasItemDao;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.GradeVendas;
import br.com.st.anequimconta_stand_dl1.entidade.GradeVendasItem;
import br.com.st.anequimconta_stand_dl1.entidade.GradeView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.util.ArrayList;
import java.util.List;

public class ControleGradeView {

    private static ControleGradeView controleGradeView;
    public static ControleGradeView getControleGradeView(Activity ctx) {
        if (controleGradeView == null) {
            controleGradeView = new ControleGradeView(ctx);
            return controleGradeView;
        } else
            return controleGradeView;
    }

    private Activity ctx;
    private static List<GradeView> listGradeView ;
    private static boolean fAtualizado = false;

    public ControleGradeView(Activity ctx) {
        this.ctx = ctx;
        listGradeView = new ArrayList<>() ;
    }

    public void atualizar() {
        listGradeView = new ArrayList<>() ;
        Log.i("atualizar_", "ControleGradeView");
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("STATUS", "=", "1"));
        List<GradeVendas> gradeVendas = GradeVendasDao.getGradeVendasDao(ctx).getList(f, "DESCRICAO");
        popularItens(gradeVendas);
        fAtualizado = true;
    }

    private void popularItens(List<GradeVendas> l){
        for (GradeVendas g : l) {
            Log.i("atualizar_", "GradeVendas "+g.getDescricao());
            GradeView gv = new GradeView(g, getListProdutos(g)) ;
            if (gv.getProdutoList().size() > 0)
                listGradeView.add(gv) ;
        }
        Log.i("atualizar_", "GradeVendas fim "+listGradeView.size());
    }

    private List<Produto> getListProdutos(GradeVendas g){
        List<Produto> l = new ArrayList<>();
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("GRADE_VENDAS_ID", "=",  String.valueOf(g.getId())));
        f.add(new DaoFilter("STATUS", "=",  "1"));
        try {
            List<GradeVendasItem> litensG = GradeVendasItemDao.getGradeVendasItensDao(ctx).getList(f, "");
            Log.i("atualizar_", "GradeVendasItens "+litensG.size());
            for (GradeVendasItem gvi : litensG){
                Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(gvi.getProduto_id()) ;
                if (p != null) {
                    if (p.getStatus() == 1)
                       l.add(p) ;
                }
            }
            Log.i("atualizar_", "GradeVendasItens L "+l.size());
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            return l ;
        }
    }

    public GradeView get(int id) throws IllegalAccessException {
        Log.i("atualizar_", "GradeVendasItens listGradeVie "+listGradeView.size());
        for (GradeView g : listGradeView) {
            if (g.getGrade().getId() == id) {
                return g ;
            }
        }
        throw new IllegalAccessException("Grade id == ["+id+"] não encotrada!") ;
    }

    public List<GradeView> getList() {
        Log.i("atualizar_", "getList() COUNT "+ listGradeView.size());
        if (!fAtualizado) {
            atualizar();
            return listGradeView;
        } else
            return listGradeView;
    }

    public boolean isfAtualizado() {
        return fAtualizado;
    }

    public void setfAtualizado(boolean fAtualizado) {
        fAtualizado = fAtualizado;
    }
}
