package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPag;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaViewPagView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContasPagas;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControleContasPagas {

    private Activity ctx ;
    private OnControleContasPagas onControleContasPagas;
    private List<ContaPag> contaPagList ;
    private List<ContaMult> contaMultList ;

    public ControleContasPagas(Activity ctx, OnControleContasPagas onControleContasPagas) {
        this.ctx = ctx;
        this.onControleContasPagas = onControleContasPagas;
    }

    public void filtro(){
        new ControleCaixa(ctx) {
            @Override
            public void caixaAberto(Caixa c) {
                filtroData(c);
            }

            @Override
            public void caixaFechado() {
                onControleContasPagas.onControleContasPagasErro("Caixa Fechado!") ;
            }

            @Override
            public void caixaErro(String msg) {
                onControleContasPagas.onControleContasPagasErro(msg) ;
            }
        }.getCaixaAberto(UtilSet.getUsuarioId(ctx));
    }


    public void filtroData(Caixa caixa){
        try {
            new ConexaoContaViewPagView(ctx, caixa) {
                @Override
                public void contaViewPagViewOk(List<ContaViewPagView> l) {
                    onControleContasPagas.onControleContasPagasOk(l);
                }

                @Override
                public void contaViewPagViewErro(String msg) {
                    onControleContasPagas.onControleContasPagasErro(msg);

                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContasPagas.onControleContasPagasErro(e.getMessage());
        }

    }

    public void filtroData(Date inicial){
        DaoFilters f = new DaoFilters() ;
        try {
            new ConexaoContaPag(ctx, f, "") {
                @Override
                public void conexaoContaPagOk(List<ContaPag> l) {
                    contaPagList = l ;
                    setContaMults() ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    onControleContasPagas.onControleContasPagasErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContasPagas.onControleContasPagasErro(e.getMessage());
        }
    }

    private void setContaMults(){
        List<String> listMult = new ArrayList<>() ;
        for (ContaPag pag: contaPagList){
            if (listMult.indexOf(pag.getMULT_UUID()) == -1)
                listMult.add(pag.getMULT_UUID()) ;
        }
        try {
            new ConexaoContaViewPagView(ctx, listMult) {
                @Override
                public void contaViewPagViewOk(List<ContaViewPagView> l) {
                    onControleContasPagas.onControleContasPagasOk(l);
                }

                @Override
                public void contaViewPagViewErro(String msg) {
                    onControleContasPagas.onControleContasPagasErro(msg);

                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContasPagas.onControleContasPagasErro(e.getMessage());
        }
    }

}
