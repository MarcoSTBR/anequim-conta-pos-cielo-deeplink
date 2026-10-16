package br.com.st.anequimconta_stand_dl1.pospag.paystone;

import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.controle.ControleCaixa;
import br.com.st.anequimconta_stand_dl1.controle.ControleConsultaPIX;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaMultPagList;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnConsultaPIX;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaMultPagList;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.pospag.OnListPagamentos;
import br.com.st.anequimconta_stand_dl1.pospag.OnListPixPagamentos;
import br.com.st.anequimconta_stand_dl1.pospag.PosApi;
import br.com.st.anequimconta_stand_dl1.pospag.PosFilter;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPixView;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;
import br.com.st.anequimconta_stand_dl1.pospag.nenhum.PosPagamentoViewNenhum;
import br.com.st.anequimconta_stand_dl1.tipos.TipoModalidade;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListPix;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class PosApiPayStone implements PosApi {

    private FragmentListCartao ctx ;
    private FragmentListPix ctxPix ;
    private OnListPixPagamentos listenerPix ;

    public PosApiPayStone(FragmentListCartao ctx) {
        this.ctx = ctx;
    }

    public PosApiPayStone(FragmentListPix ctxPix) {
        this.ctxPix = ctxPix;
    }

    @Override
    public void getList(PosFilter filter, OnListPagamentos listener) {
        new ControleCaixa(ctx.getActivity()) {
            @Override
            public void caixaAberto(Caixa c) {
                new ControleContaMultPagList(ctx.getActivity(), new OnControleContaMultPagList() {
                    @Override
                    public void ok(List<ControleContaMultPagList.PagamentoView> ls) {
                        List<PosPagamentoView> l = new ArrayList<PosPagamentoView>();
                        for (ControleContaMultPagList.PagamentoView p : ls){
                            l.add(new PosPagamentoViewNenhum(p.getContaPag(), p.getModalidade())) ;
                        }
                        listener.OnSucess(l);
                    }

                    @Override
                    public void erro(String msg) {
                        listener.OnErro(msg);
                    }
                }).getCaixa(c);
            }

            @Override
            public void caixaFechado() {
                listenerPix.OnPixError("Caixa Fechado!");
            }

            @Override
            public void caixaErro(String msg) {
                listenerPix.OnPixError(msg);
            }
        }.getCaixaAberto(UtilSet.getUsuarioId(ctx.getActivity()));
        // listenerPix.OnPixSucess();
    }

    @Override
    public void getPixList(PosFilter filter, OnListPixPagamentos listener) {
        listenerPix = listener ;
        new ControleCaixa(ctxPix.getActivity()) {
            @Override
            public void caixaAberto(Caixa c) {
                new ControleContaMultPagList(ctxPix.getActivity(), new OnControleContaMultPagList() {
                    @Override
                    public void ok(List<ControleContaMultPagList.PagamentoView> ls) {
                        List<ControleContaMultPagList.PagamentoView> somentePix = new ArrayList<>() ;
                        for (ControleContaMultPagList.PagamentoView p : ls){
                            if (p.getModalidade().getTipoModalidade() == TipoModalidade.pgPIX)
                               somentePix.add(p) ;

                        }
                        setpagamentoPix(somentePix) ;
                    }

                    @Override
                    public void erro(String msg) {
                        listener.OnPixError(msg);
                    }
                }).getCaixa(c);
            }

            @Override
            public void caixaFechado() {
                listenerPix.OnPixError("Caixa Fechado!");
            }

            @Override
            public void caixaErro(String msg) {
                listenerPix.OnPixError(msg);
            }
        }.getCaixaAberto(UtilSet.getUsuarioId(ctx.getActivity()));
       // listenerPix.OnPixSucess();
    }

    private void setpagamentoPix(List<ControleContaMultPagList.PagamentoView> ls) {
        new ControleConsultaPIX(ctx.getActivity(), ls, new OnConsultaPIX() {
            @Override
            public void ConsultaPix(List<PosPagamentoPixView> l) {
                listenerPix.OnPixSucess(l);

            }

            @Override
            public void ConsultaPixErro(String msg) {
                listenerPix.OnPixError(msg);

            }
        }).executar();
    }






}
