package br.com.st.anequimconta_stand_dl1.pospag.nenhum;

import android.content.Context;

import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.pospag.OnListPagamentos;
import br.com.st.anequimconta_stand_dl1.pospag.OnListPixPagamentos;
import br.com.st.anequimconta_stand_dl1.pospag.PosApi;
import br.com.st.anequimconta_stand_dl1.pospag.PosFilter;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;

import java.util.ArrayList;
import java.util.List;

public class PosApiNenhum implements PosApi {

    private Context ctx ;

    public PosApiNenhum(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void getList(PosFilter filter, OnListPagamentos listener) {
        listener.OnSucess(getPosPgs());
    }

    @Override
    public void getPixList(PosFilter filter, OnListPixPagamentos listener) {
        listener.OnPixSucess(new ArrayList<>());
    }

    private List<PosPagamentoView> getPosPgs(){
        List<PosPagamentoView> l = new ArrayList<PosPagamentoView>();
/*
        Caixa caixa = CaixaDao.getCaixaDao(ctx).getCaixaAberto(UtilSet.getUsuarioId(ctx)) ;
        if (caixa != null) {
            for (VendaVistaPag pg : getVendaItens(caixa)) {
                Modalidade modalidade = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(pg.getMODALIDADE_ID())) ;
                l.add(new PosPagamentoViewNenhum(pg, modalidade));
            }
        }
*/
        return l ;
    }

    private List<ContaPag> getVendaItens(Caixa caixa){
        List<ContaPag> l = new ArrayList<>() ;
/*
        DaoFilters f  = new DaoFilters();
        f.add(new DaoFilter("CAIXA_UUID", "=", caixa.getUUID()));
        f.add(new DaoFilter("STATUS", "=", "2"));
        for (VendaVista vv: VendaVistaDao.getVendaVistaDao(ctx).getList(f, "DATA DESC")){
            DaoFilters f2 = new DaoFilters();
            f2.add(new DaoFilter("VENDA_VISTA_UUID", "=", vv.getUUID()));
            f2.add(new DaoFilter("STATUS", "=", "1"));
            l.addAll(VendaVistaPagDao.getVendaVistaPagDao(ctx).getList(f2, "")) ;
        }
*/
        return l ;
    }

}
