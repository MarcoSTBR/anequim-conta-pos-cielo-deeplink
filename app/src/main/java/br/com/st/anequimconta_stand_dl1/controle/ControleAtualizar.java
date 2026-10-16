package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.app.ProgressDialog;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoAcompanhamento;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoAcompanhamentoItem;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoAcompanhamentoProduto;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoGradeVendas;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoModalidade;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoProdutos;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoProdutoTicke;
import br.com.st.anequimconta_stand_dl1.entidade.ProdutoTicket;

import java.net.MalformedURLException;
import java.util.List;

public abstract class ControleAtualizar {
    private Activity ctx;
    private ProgressDialog progressDialog = null;

    public ControleAtualizar(Activity ctx) {
        this.ctx = ctx;
    }

    public void executar() {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Produtos....");
        progressDialog.setCancelable(true);
        progressDialog.show();

        new ConexaoProdutos(ctx) {
            @Override
            public void ok() {
                setAcompanhamento();
            }

            @Override
            public void erro(String msg) {
                sairErro(msg);
            }
        }.executar();
    }

    private void setAcompanhamento () {
        try {
            progressDialog.setTitle("Acompanhamentos....");
            new ConexaoAcompanhamento(ctx) {
                @Override
                public void ok() {
                    setAcompanhamentoItem();
                }

                @Override
                public void erro(String msg) {
                    sairErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            sairErro(e.getMessage());
        }
    }

    private void setAcompanhamentoItem () {
        try {
            progressDialog.setTitle("Acompanhamentos Itens....");
            new ConexaoAcompanhamentoItem(ctx) {
                @Override
                public void ok() {
                    setAcompanhamentoProd();
                }

                @Override
                public void erro(String msg) {
                    sairErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            sairErro(e.getMessage());
        }
    }

    private void setAcompanhamentoProd () {
        try {
            progressDialog.setTitle("Acompanhamentos Produtos....");
            new ConexaoAcompanhamentoProduto(ctx) {
                @Override
                public void ok() {
                    setGradeVendas();
                }

                @Override
                public void erro(String msg) {
                    sairErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            sairErro(e.getMessage());
        }
    }


    private void setGradeVendas () {
        try {
            progressDialog.setTitle("Grade Vendas....");
            new ConexaoGradeVendas(ctx) {
                @Override
                public void ok() {
                    //atualizacaoOk();
                    setModalidade();
                }

                @Override
                public void erro(String msg) {
                    sairErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            sairErro(e.getMessage());
        }
    }


    private void setModalidade(){
        progressDialog.setTitle("Modalidades...");
        new ConexaoModalidade(ctx) {
            @Override
            public void ok() {

                setTickets() ;
            }

            @Override
            public void erro(String msg) {
                sairErro(msg) ;
            }
        }.execute() ;
    }


    private void setTickets(){
        try {
            new ConexaoProdutoTicke(ctx) {
                @Override
                public void ticketProdutoOk(List<ProdutoTicket> l) {
                    atualOk();
                }

                @Override
                public void ticketProdutoErro(String msg) {
                    sairErro(msg) ;
                }
            }.executar();
        } catch (MalformedURLException e) {
           e.printStackTrace();
            atualizacaoErro(e.getMessage()) ;
        }
    }

    private void atualOk(){
        progressDialog.dismiss();
        atualizacaoOk() ;
    }
    private void sairErro(String msg){
        progressDialog.dismiss();
        atualizacaoErro(msg) ;
    }
/*
    private void setTickets(){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CONF_TERMINAL_ID", "=", String.valueOf(UtilSet.getTerminalId(ctx))));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoTicketItem(ctx, f, "ID") {
                @Override
                public void ok() {
                    atualizacaoOk() ;
                    //    acessos() ;
                }

                @Override
                public void erro(String msg) {
                    atualizacaoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            atualizacaoErro(e.getMessage());
        }
    }
*/
    public abstract void atualizacaoOk();

    public abstract void atualizacaoErro(String msg);
}
