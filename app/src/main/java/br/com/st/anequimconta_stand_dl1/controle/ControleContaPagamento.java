package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaMultEdit;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPagEdit;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPags;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPagsAtualiza;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaMultView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContasView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleFechamentoContas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControlePagamento;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPags;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultView;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControleTotaisContaView;
import br.com.st.anequimconta_stand_dl1.pospag.ControlePosPagamento;
import br.com.st.anequimconta_stand_dl1.pospag.ControlePosPagamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamento;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagamento;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagamentoPix;
import br.com.st.anequimconta_stand_dl1.tipos.TipoModalidade;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControleContaPagamento implements OnPosPagamento, OnPosPagamentoPix , OnPosCancelamento, OnPosCancelamentoPix {

    private Activity ctx ;
    private OnControlePagamento onControlePagamento ;
    private Caixa caixa ;
    private ContaPag contaPagCorrente ;
    private ContaMultView contaMultView ;
    private int ponteiro ;
    private ControlePosPagamento controlePosPagamento ;
    private ControlePosPagamentoPix controlePosPagamentoPix ;

    public ControleContaPagamento(FragmentPagamento f, OnControlePagamento onControlePagamento) {
        this.ctx = f.getActivity();
        this.onControlePagamento = onControlePagamento ;
        controlePosPagamento = new ControlePosPagamento(f) ;
        controlePosPagamentoPix = new ControlePosPagamentoPix(f) ;
       // Log.i("pagamentos_", "criou ContaMult "+contaMult.getUUID()) ;
    }

    public ControleContaPagamento(FragmentPagamento f, Caixa caixa, OnControlePagamento onControlePagamento) {
        this.ctx = f.getActivity();
        this.caixa = caixa ;
        this.onControlePagamento = onControlePagamento ;
        ContaMult contaMult = new ContaMult(0, UtilSet.getLojaId(ctx), new Date(), UtilSet.getUUID(), caixa.getUUID(), 0, 0) ;
        controlePosPagamento = new ControlePosPagamento(f) ;
        controlePosPagamentoPix = new ControlePosPagamentoPix(f) ;
        //contaMultView = new ContaMultView(new ArrayList<>(), contaMult, new ArrayList<>()) ;
        Log.i("pagamentos_", "criou ContaMult "+contaMult.getUUID()) ;
    }

    public void setCaixa(Caixa c){
        caixa = c ;
        ContaMult contaMult = new ContaMult(0, UtilSet.getLojaId(ctx), new Date(), UtilSet.getUUID(), caixa.getUUID(), 0, 0) ;
        contaMultView = new ContaMultView(new ArrayList<>(), contaMult, new ArrayList<>()) ;

    }

    public void setListView(List<PedidoView> pvl){
        ContaMult contaMult = new ContaMult(0, UtilSet.getLojaId(ctx), new Date(), UtilSet.getUUID(), caixa.getUUID(), 0, 0) ;
        contaMultView = new ContaMultView(new ArrayList<>(), contaMult, new ArrayList<>()) ;
        Log.i("pagamentos_", "criou ContaMult "+contaMult.getUUID()) ;
        contasEnviadas(pvl) ;
    }


    private void getContaMult(ContaMult contaMult){
        new ControleContaMultView(ctx, new OnControleContaMultView() {
            @Override
            public void ControleContaMultViewOk(ContaMultView cv) {
                contaMultView = cv ;
                Log.i("pagamentos_", " getContaList() "+contaMultView.getContaList().size()) ;
                Log.i("pagamentos_", " getContaPagList() "+contaMultView.getContaPagList().size()) ;
                display();
            }

            @Override
            public void ControleContaMultViewErro(String msg) {
                onControlePagamento.pagamentoErro(msg);

            }
        }).getContaMult(contaMult);
    }

    private void contasEnviadas(List<PedidoView> pvl) {
        new ControlePedidoConta(ctx) {
            @Override
            public void contas(List<Conta> l) {
                if (l.size() > 0)
                  addContas(l);
                else {
                    display();
                    onControlePagamento.pagamentoErro("Nenhuma conta aberta encontrada!") ;
                }
            }
        }.getContas(pvl);
    }

    private void addContas(List<Conta> l){
        contaMultView.setContaList(l);
        ponteiro = 0 ;
        naoPodeTerRecebimento(contaMultView.getContaList().get(ponteiro)) ;
    }

    private void naoPodeTerRecebimento(Conta c){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CONTA_UUID", "=", c.getUUID()));
        try {
            new ConexaoContaPags(ctx, f, "") {
                @Override
                public void conexaoContaPagsOk(List<ContaPags> l) {
                    Log.i("naoPodeTer", "List<ContaPags> "+l.size()) ;
                    if (l.size()>0) {
                        Log.i("naoPodeTer", "pagamento encontrado "+l.get(0).getCONTA_MULT_UUID()) ;
                        encontrouPagamentoDaContaInserida(l.get(0).getCONTA_MULT_UUID()) ;
                    } else {
                        if (++ponteiro < contaMultView.getContaList().size())
                            contaMultView.getContaList() ;
                        else display();
                    }
                }

                @Override
                public void conexaoContaPagsErro(String msg) {
                    onControlePagamento.pagamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        }
    }

    public void addContas(String uuid){
        if (contaMultView.getContaPagList().size() == 0) {
            DaoFilters f = new DaoFilters();
            f.add(new DaoFilter("UUID", "=", uuid));
            try {
                new ConexaoContas(ctx, f, "") {
                    @Override
                    public void contasOk(List<Conta> l) {
                        if (l.size() > 0)
                            verificar(l.get(0));
                        else onControlePagamento.pagamentoErro("Conta não encontrada!");
                    }

                    @Override
                    public void contasErro(String msg) {
                        onControlePagamento.pagamentoErro(msg);
                    }
                }.executar();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                onControlePagamento.pagamentoErro(e.getMessage());
            }
        } else onControlePagamento.pagamentoErro("Selecione as contas antes de fazer o pagamento!");
    }

    private boolean seFoiSelecionado(String uuid){
        boolean f = false ;
        for (Conta c : contaMultView.getContaList()){
            String u = c.getUUID() ;
            Log.i("pagamentos_", " c uuid "+c.getUUID()+" uuid "+uuid) ;
            if (u.equals(uuid))
                f = true ;
        }
        return f ;
    }

    private void verificar(Conta c){
        Log.i("pagamentos_", " getContaList() "+contaMultView.getContaList().size()) ;
        Log.i("pagamentos_", " getContaPagList() "+contaMultView.getContaPagList().size()) ;

        if (seFoiSelecionado(c.getUUID())) {
            Log.i("pagamentos_", "sim contains") ;
            onControlePagamento.pagamentoErro("Conta Já foi selecionada!");
        } else {
            Log.i("pagamentos_", "não contains") ;
            naoPodeTerPagamentoVinculado(c) ;
        }
    }

    private void naoPodeTerPagamentoVinculado(Conta c){
        Log.i("naoPodeTer", "Conta c "+c.getUUID()) ;
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CONTA_UUID", "=", c.getUUID()));
        try {
            new ConexaoContaPags(ctx, f, "") {
                @Override
                public void conexaoContaPagsOk(List<ContaPags> l) {
                    Log.i("naoPodeTer", "List<ContaPags> "+l.size()) ;
                    if (l.size()>0) {
                        Log.i("naoPodeTer", "pagamento encontrado "+l.get(0).getCONTA_MULT_UUID()) ;
                        encontrouPagamentoDaContaInserida(l.get(0).getCONTA_MULT_UUID()) ;
                    } else {
                        contaMultView.getContaList().add(c) ;
                        display();
                    }
                }

                @Override
                public void conexaoContaPagsErro(String msg) {
                    onControlePagamento.pagamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        }
    }

    private void encontrouPagamentoDaContaInserida(String mult_uuid){
        new ControleContaMultView(ctx, new OnControleContaMultView() {
            @Override
            public void ControleContaMultViewOk(ContaMultView vc) {
                contaMultView = vc ;
                display();
            }

            @Override
            public void ControleContaMultViewErro(String msg) {
                onControlePagamento.pagamentoErro(msg);
            }
        }).getMult(mult_uuid);
    }

    public void display(){
        onControlePagamento.contasList(contaMultView.getContaList());
        onControlePagamento.pagamentoList(contaMultView.getContaPagList());
        new ControleContasView(ctx, new OnControleContasView() {
            @Override
            public void controleContaViewOk(List<ContaView> contaViews) {
                Double total = 0.00 ;
                Double pago = 0.00 ;
                for (ContaView c : contaViews) {
                    total+=new ControleTotaisContaView(ctx, c).getTotal() ;
                }
                for (ContaPag contaPag : contaMultView.getContaPagList()){
                    if (contaPag.getSTATUS() == 1)
                      pago += contaPag.getVALOR() ;
                }
                Double apagar = total - pago ;
                onControlePagamento.total(total, apagar, pago);
            }

            @Override
            public void controleContaViewErro(String msg) {
                onControlePagamento.pagamentoErro("display "+msg);
            }
        }).getContasView(contaMultView.getContaList());
    }

    public void addPagamento(Modalidade modalidade, Double valor){
        if (valor <= 0.00)
            onError("Valor Inválido!");
        else if (contaMultView.getContaList().size() == 0)
            onError("Nenhuma Conta selecionada!");
        else
            if (contaMultView.getContaList().size() > 0) {
                contaPagCorrente = new ContaPag(0, UtilSet.getUUID(), contaMultView.getContaMult().getUUID(), new Date(),  caixa.getUUID(), UtilSet.getTerminalId(ctx), modalidade.getId(), valor, 0.0, 1);
                if ((modalidade.getTipoModalidade() == TipoModalidade.pgCartao) ||
                        (modalidade.getTipoModalidade() == TipoModalidade.pgDebito) ||
                        (modalidade.getTipoModalidade() == TipoModalidade.pgCredito))
                    controlePosPagamento.pagamento(contaPagCorrente, this);
                else if (modalidade.getTipoModalidade() == TipoModalidade.pgPIX)
                    controlePosPagamento.pagamento(contaPagCorrente, this);
                else onSucess(contaPagCorrente);
          } else onError("Opção Inválida!");
    }

    public void addPagamento(Modalidade modDinheiro, Double valor, Double troco){
        if ((valor > 0) && (troco > 0)){
            Modalidade modTroco = ModalidadeDao.getModalidadeDao(ctx).getTroco() ;
            if (modTroco != null){
                Double v = valor - troco ;
                contaPagCorrente = new ContaPag(0, UtilSet.getUUID(), contaMultView.getContaMult().getUUID(), new Date(),  caixa.getUUID(), UtilSet.getTerminalId(ctx), modDinheiro.getId(), v, troco, 1);
                onSucess(contaPagCorrente);
            } else onError("Troco não Permitido!");
        } else onError("Valor Inválido!");
    }

    public void cancelar(ContaPag pag){
        Modalidade modalidade = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(pag.getMODALIDADE_ID())) ;
        if ((modalidade.getTipoModalidade() == TipoModalidade.pgCartao) ||
                (modalidade.getTipoModalidade() == TipoModalidade.pgDebito) ||
                (modalidade.getTipoModalidade() == TipoModalidade.pgCredito))
            controlePosPagamento.cancelamento(pag, this);
        else if (modalidade.getTipoModalidade() == TipoModalidade.pgPIX)
            controlePosPagamento.cancelamento(pag, this);
        else onCancelamentoSucess(pag);
    }

    @Override
    public void onSucess(ContaPag it) {
        input(it) ;
    }

    private void input(ContaPag pag){
        contaPagCorrente = pag ;
        try {
            new ConexaoContaPagsAtualiza(ctx, contaMultView.getContaMult(), contaMultView.getContaList()) {
                @Override
                public void contaPagsAtualizaOk(List<ContaPags> l) {
                    atualizaMultPag() ;
                }

                @Override
                public void contaPagsAtualizaErro(String msg) {
                    onControlePagamento.pagamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        }
    }

    private void outPut(ContaPag pag){
        contaPagCorrente = pag ;
        contaPagCorrente.setSTATUS(0);
        try {
            new ConexaoContaPagEdit(ctx, contaPagCorrente) {
                @Override
                public void conexaoContaPagOk(ContaPag pag) {
                    getContaMult(contaMultView.getContaMult()) ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    onControlePagamento.pagamentoErro(msg);
                }

            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        }
    }

    private void atualizaMultPag(){
        try {
            new ConexaoContaMultEdit(ctx, contaMultView.getContaMult()) {
                @Override
                public void conexaoContaMultEditOk(ContaMult contaMult) {
                    gravarItemPag() ;
                }

                @Override
                public void conexaoContaMultEditErro(String msg) {
                    onControlePagamento.pagamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        }
    }

    private void gravarItemPag(){
        try {
            new ConexaoContaPagEdit(ctx, contaPagCorrente) {
                @Override
                public void conexaoContaPagOk(ContaPag pag) {
                    getContaMult(contaMultView.getContaMult()) ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    onControlePagamento.pagamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControlePagamento.pagamentoErro(e.getMessage());
        }
    }

    @Override
    public void onError(String msg) {
        onControlePagamento.pagamentoErro(msg);
    }

    @Override
    public void onPixSucess(ContaPag it) {
        input(it);
    }

    @Override
    public void onPixError(String msg) {
        onControlePagamento.pagamentoErro(msg);
    }

    @Override
    public void onCancelamentoSucess(ContaPag contaPag) {
        outPut(contaPag) ;
    }

    @Override
    public void onCancelamentoErro(String msg) {
        onControlePagamento.pagamentoErro(msg);
    }

    @Override
    public void onCancelamentoPixSucess(ContaPag it) {
        outPut(it) ;
    }

    public void onCancelamentoPixError(String msg) {
        onControlePagamento.pagamentoErro(msg);
    }

    public void fechar(OnControleFechamentoContas onControleFechamentoContas) {
        //onControlePagamento.pagamentoErro("Fechar") ;
        new ControleFechamentoContas(ctx, caixa, contaMultView.getContaMult(), onControleFechamentoContas).fechar();
    }

}
