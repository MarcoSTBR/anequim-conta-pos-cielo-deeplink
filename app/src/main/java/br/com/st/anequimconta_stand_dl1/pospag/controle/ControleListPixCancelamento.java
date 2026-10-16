package br.com.st.anequimconta_stand_dl1.pospag.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPag;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPagEdit;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoPixPos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPosPix;
import br.com.st.anequimconta_stand_dl1.pospag.ControlePosPagamento;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamento;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPixView;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListPix;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public abstract class ControleListPixCancelamento {

    private Activity ctx ;
    private PosPagamentoPixView pag ;
    private ControlePosPagamento controlePosPagamento ;

    public ControleListPixCancelamento(FragmentListPix f) {
        this.ctx = f.getActivity();
        controlePosPagamento = new ControlePosPagamento(f) ;
    }

    public void executar(PosPagamentoPixView pag){
        this.pag = pag;
        verificaPermissao() ;
    }

    private void verificaPermissao(){
        startCancelamento() ;
    }

    private void startCancelamento(){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("PAGAMENTO_UUID", "=", pag.getTx_id()));

        try {
            new ConexaoRecebimentoPixPos(ctx, f, "") {
                @Override
                public void recebimentoPixPosOk(List<RecebimentoPosPix> l) {
                    if (l.size() > 0){
                        setContaPagamento(l.get(0)) ;
                    } else cancelamentoPixErro("Recebimento Não Encontrado!");
                }

                @Override
                public void recebimentoPixPosErro(String msg) {
                    cancelamentoPixErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancelamentoPixErro(e.getMessage());
        }
    }

    private void setContaPagamento(RecebimentoPosPix recebimentoPosPix){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CONTA_PAG_UUID", "=", recebimentoPosPix.getCONTA_PAG_UUID()));
        try {
            new ConexaoContaPag(ctx, f, "") {
                @Override
                public void conexaoContaPagOk(List<ContaPag> l) {
                    if (l.size() >0) {
                        controleCancelar(l.get(0)) ;
                    } else conexaoContaPagErro("Pagamento Não Encontrado!") ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    cancelamentoPixErro(msg);

                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancelamentoPixErro(e.getMessage());
        }
    }

    private void controleCancelar(ContaPag contaPag) {
        controlePosPagamento.cancelamento(contaPag , new OnPosCancelamento() {

            @Override
            public void onCancelamentoSucess(ContaPag contaPag) {
                cancelarWeb(contaPag) ;
            }

            @Override
            public void onCancelamentoErro(String msg) {
                cancelamentoPixErro(msg);
            }
        });

    }

    private void cancelarWeb(ContaPag contaPag) {
        contaPag.setSTATUS(0);
        try {
            new ConexaoContaPagEdit(ctx, contaPag) {
                @Override
                public void conexaoContaPagOk(ContaPag pag) {
                    cancelamentoPixOk();
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    cancelamentoPixErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancelamentoPixErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            cancelamentoPixErro(e.getMessage());
        }

    }

    public abstract void cancelamentoPixOk() ;
    public abstract void cancelamentoPixErro(String msg) ;
}
