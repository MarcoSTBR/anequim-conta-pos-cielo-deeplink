package br.com.st.anequimconta_stand_dl1.pospag.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPag;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPagEdit;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoPos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPos;
import br.com.st.anequimconta_stand_dl1.pospag.ControlePosPagamento;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamento;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public abstract class ControleListCancelamento {

    private Activity ctx ;
    private PosPagamentoView pag ;
    private ControlePosPagamento controlePosPagamento;

    public ControleListCancelamento(FragmentListCartao f) {
        this.ctx = f.getActivity();
        controlePosPagamento = new ControlePosPagamento(f) ;
    }

    public void executar(PosPagamentoView pag){
        this.pag = pag;
        startCancelamento() ;
    }

    private void startCancelamento(){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CONTA_PAG_UUID", "=", pag.getId().trim()));
      //  f.add(new DaoFilter("DATA", "=", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pag.getData())));
        try {
            new ConexaoRecebimentoPos(ctx, f, "") {
                @Override
                public void recebimentoPosOk(List<RecebimentoPos> l) {
                    if (l.size() > 0){
                        setContaPagamento(l.get(0)) ;
                    } else cancelamentoErro("Pagamento Não Encontrado:\n"+pag.getId());
                }

                @Override
                public void recebimentoPosErro(String msg) {
                    cancelamentoErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancelamentoErro(e.getMessage());
        }
    }

    private void setContaPagamento(RecebimentoPos recebimentoPos ){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", recebimentoPos.getConta_pag_uuid()));
        try {
            new ConexaoContaPag(ctx, f, "") {
                @Override
                public void conexaoContaPagOk(List<ContaPag> l) {
                    if (l.size() >0) {
                        controleCancelar(l.get(0)) ;
                    } else conexaoContaPagErro("Recebimento Não Encontrado:\n"+recebimentoPos.getConta_pag_uuid()) ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    cancelamentoErro(msg);

                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancelamentoErro(e.getMessage());
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
                cancelamentoErro(msg);
            }
        });

    }

    private void cancelarWeb(ContaPag contaPag) {
        contaPag.setSTATUS(0);
        try {
            new ConexaoContaPagEdit(ctx, contaPag) {
                @Override
                public void conexaoContaPagOk(ContaPag pag) {
                    cancelamentoOk() ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    cancelamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancelamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            cancelamentoErro(e.getMessage());
        }
    }

    public abstract void cancelamentoOk() ;
    public abstract void cancelamentoErro(String msg) ;
}
