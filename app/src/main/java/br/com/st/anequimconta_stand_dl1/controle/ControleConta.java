package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaEdit;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleConta;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ControleConta {
    private Activity ctx ;
    private OnControleConta onControleConta ;

    public ControleConta(Activity ctx, OnControleConta onControleConta) {
        this.ctx = ctx;
        this.onControleConta = onControleConta;
    }

    public void getContasAbertas(){
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("LOJA_ID", "=", String.valueOf(UtilSet.getLojaId(ctx))));
        f.add(new DaoFilter("STATUS", "=", "1"));
        getContas(f, "NUMERO");
    }

    private void getContas(DaoFilters f, String order){
        try {
            new ConexaoContas(ctx, f, order) {
                @Override
                public void contasOk(List<Conta> l) {
                    l.indexOf(new ContaViewPagView.DataDesc()) ;
                    onControleConta.contasOk(l);
                }

                @Override
                public void contasErro(String msg) {
                    onControleConta.contaErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleConta.contaErro(e.getMessage());
        }
    }

    public void cancelarConta(Conta c){
        c.setSTATUS(0);
        try {
            new ConexaoContaEdit(ctx, c) {
                @Override
                public void contasOk(Conta conta) {
                    onControleConta.contasOk(new ArrayList<>());
                }

                @Override
                public void contasErro(String msg) {
                    onControleConta.contaErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleConta.contaErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleConta.contaErro(e.getMessage());
        }
    }
}
