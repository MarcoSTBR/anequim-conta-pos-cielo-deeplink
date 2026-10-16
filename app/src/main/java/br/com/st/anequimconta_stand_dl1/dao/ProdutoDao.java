package br.com.st.anequimconta_stand_dl1.dao;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampoTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoCampos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.tipos.DaoPrimaryKeyTipo;
import br.com.st.anequimconta_stand_dl1.daoclass.Dao;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDao extends Dao {

    private static ProdutoDao produtoDao ;

    public static ProdutoDao getProdutoDao(Context ctx) {
        if (produtoDao == null) {
            produtoDao = new ProdutoDao(ctx) ;
            return produtoDao ;
        } else return produtoDao ;
    }

    public ProdutoDao(Context ctx) {
        super(ctx);
    }

    @Override
    public String getTabela() {
        return "PRODUTO";
    }

    @Override
    public DaoPrimaryKeyTipo getTipoPrimaryKey() {
        return DaoPrimaryKeyTipo.tpkNormal;
    }

    @Override
    public String getID() {
        return "id";
    }

    @Override
    public List<DaoCampos> getCampos() {
        List<DaoCampos> l = new ArrayList<DaoCampos>() ;
        l.add(new DaoCampos("CODIGO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("UNIDADE", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("DESCRICAO", DaoCampoTipo.dbString, true)) ;
        l.add(new DaoCampos("STATUS", DaoCampoTipo.dbInt, true)) ;
        l.add(new DaoCampos("IMAGEM", DaoCampoTipo.dbString, false)) ;
        l.add(new DaoCampos("COMISSAO", DaoCampoTipo.dbDecimal, true)) ;
        l.add(new DaoCampos("PRECO", DaoCampoTipo.dbDecimal, true)) ;
        return l ;
    }

    public List<Produto> getList(DaoFilters filters, String order)  {
        List<Produto> l = new ArrayList<Produto>() ;
        try {
            JSONArray jaa = null;
            jaa = findAll(filters, order);
            for (int i = 0 ; i < jaa.length() ; i++) {
                l.add(new Produto(jaa.getJSONObject(i))) ;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l ;
    }

    public Produto getProduto(int id)  {
        List<Produto> l = new ArrayList<Produto>() ;
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("id", "=", String.valueOf(id)));
        l = getList(f, "");
        if (l.size()>0) return l.get(0) ;
         else return null ;
    }

    public Produto geCodProduto(String cod)  {
        List<Produto> l = new ArrayList<Produto>() ;
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("CODIGO", "=", cod));
        l = getList(f, "");
        if (l.size()>0) return l.get(0) ;
        else return null ;
    }

    public Produto geCodProdutoAtivo(String cod)  {
        List<Produto> l = new ArrayList<Produto>() ;
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("CODIGO", "=", cod));
        f.add(new DaoFilter("STATUS", "=", "1"));
        l = getList(f, "");
        if (l.size()>0) return l.get(0) ;
        else return null ;
    }
}
