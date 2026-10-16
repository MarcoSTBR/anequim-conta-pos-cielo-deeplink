package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPag;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPags;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContas;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoMultPag;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaMultView;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPags;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class ControleContaMultView {

    private Activity ctx ;
    private OnControleContaMultView onControleContaMultView;
    private ContaMult contaMult ;
    private List<ContaPag> listPag ;

    public ControleContaMultView(Activity ctx, OnControleContaMultView onControleContaMultView) {
        this.ctx = ctx;
        this.onControleContaMultView = onControleContaMultView;
    }

    public void getConta(Conta conta){
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("CONTA_UUID", "=", conta.getUUID()));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoContaPags(ctx, f, "") {
                @Override
                public void conexaoContaPagsOk(List<ContaPags> l) {
                    if (l.size() > 0)
                        getMult(l.get(0).getCONTA_MULT_UUID());
                     else
                        onControleContaMultView.ControleContaMultViewErro("Pagamento Não Encontrado!");
                }

                @Override
                public void conexaoContaPagsErro(String msg) {
                    onControleContaMultView.ControleContaMultViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        }
    }

    public void getMult(String uuid){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", uuid));
        try {
            new ConexaoMultPag(ctx, f, "") {
                @Override
                public void conexaoMultPagOk(List<ContaMult> l) {
                    if (l.size() > 0) {
                        contaMult = l.get(0) ;
                        setPagamentos();
                    } else
                        onControleContaMultView.ControleContaMultViewErro("Indice de Multi Pagamento Não Encontrado!");
                }

                @Override
                public void conexaoMultPagErro(String msg) {
                    onControleContaMultView.ControleContaMultViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        }
    }

    public void getContaMult(ContaMult cm){
        contaMult = cm ;
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", cm.getUUID()));
        try {
            new ConexaoMultPag(ctx, f, "") {
                @Override
                public void conexaoMultPagOk(List<ContaMult> l) {
                    if (l.size() > 0)
                        contaMult = l.get(0) ;
                    setPagamentos();
                }

                @Override
                public void conexaoMultPagErro(String msg) {
                    onControleContaMultView.ControleContaMultViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        }
    }

    private void setPagamentos() {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("MULT_UUID", "=", contaMult.getUUID()));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoContaPag(ctx, f, "") {
                @Override
                public void conexaoContaPagOk(List<ContaPag> l) {
                    listPag = l ;
                    setContas();
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    onControleContaMultView.ControleContaMultViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        }
    }

    private void setContas() {
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("MULT_UUID", "=", contaMult.getUUID()));
        try {
            new ConexaoContaPags(ctx, f, "") {
                @Override
                public void conexaoContaPagsOk(List<ContaPags> l) {
                    setContasDeContasPags(l);
                }

                @Override
                public void conexaoContaPagsErro(String msg) {
                    onControleContaMultView.ControleContaMultViewErro(msg);
                }

            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        }
    }

    private void setContasDeContasPags(List<ContaPags> l) {
        try {
            new ConexaoContas(ctx, l) {
                @Override
                public void contasOk(List<Conta> l) {
                    Log.i("exc", " List<Conta> "+l.size()) ;
                    ContaMultView c = new ContaMultView(l, contaMult, listPag) ;
                    onControleContaMultView.ControleContaMultViewOk(c);
                }

                @Override
                public void contasErro(String msg) {
                    onControleContaMultView.ControleContaMultViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleContaMultView.ControleContaMultViewErro(e.getMessage());
        }
    }

}
