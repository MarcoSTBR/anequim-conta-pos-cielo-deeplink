package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPag;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoMultPag;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaMultPagList;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.pospag.OnListPixPagamentos;
import br.com.st.anequimconta_stand_dl1.pospag.paystone.PosPagamentoPixPayStone;

public class ControleContaMultPagList {

    private Activity ctx ;
    private OnControleContaMultPagList onControleContaMultPagList ;
    private List<ContaMult> contaMults ;
    private List<PagamentoView> pagamentoViews ;
    private int ponteiro ;

    public ControleContaMultPagList(Activity ctx, OnControleContaMultPagList onControleContaMultPagList) {
        this.ctx = ctx;
        this.onControleContaMultPagList = onControleContaMultPagList ;
        pagamentoViews = new ArrayList<>() ;
    }

    public void getCaixa(Caixa caixa){
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("CAIXA_UUID", "=", caixa.getUUID()));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoMultPag(ctx, f, "id DESC") {
                @Override
                public void conexaoMultPagOk(List<ContaMult> l) {
                    if (l.size() > 0) {
                        ponteiro = 0 ;
                        contaMults = l;
                        getPagamentos(contaMults.get(ponteiro));
                    } else
                        onControleContaMultPagList.erro("Pagamentos Não Encontrados!");

                }

                @Override
                public void conexaoMultPagErro(String msg) {
                    onControleContaMultPagList.erro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultPagList.erro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleContaMultPagList.erro(e.getMessage());
        }
    }

    public void getPagamentos(ContaMult cm){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("MULT_UUID", "=", cm.getUUID()));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoContaPag(ctx, f, "") {
                @Override
                public void conexaoContaPagOk(List<ContaPag> l) {
                    if (l.size() > 0){
                        for (ContaPag pg : l){
                            Modalidade modalidade = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(pg.getMODALIDADE_ID())) ;
                            if (modalidade != null) {
                                pagamentoViews.add(new PagamentoView(modalidade, pg)) ;
                            }
                        }
                    }
                    proximoContaM() ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    onControleContaMultPagList.erro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultPagList.erro(e.getMessage());
        }
    }

    private void proximoContaM() {
        ponteiro++ ;
        if (ponteiro < contaMults.size())
            getPagamentos(contaMults.get(ponteiro)) ;
        else onControleContaMultPagList.ok(pagamentoViews);
    }

    public class PagamentoView{
        private Modalidade modalidade ;
        private ContaPag contaPag ;

        public PagamentoView(Modalidade modalidade, ContaPag contaPag) {
            this.modalidade = modalidade;
            this.contaPag = contaPag;
        }

        public Modalidade getModalidade() {
            return modalidade;
        }

        public void setModalidade(Modalidade modalidade) {
            this.modalidade = modalidade;
        }

        public ContaPag getContaPag() {
            return contaPag;
        }

        public void setContaPag(ContaPag contaPag) {
            this.contaPag = contaPag;
        }
    }

}
