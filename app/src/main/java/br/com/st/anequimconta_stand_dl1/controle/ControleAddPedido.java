package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaEdit;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleAddPedido;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

public class ControleAddPedido {
    private Activity ctx ;
    private OnControleAddPedido onControleAddPedido ;

    public ControleAddPedido(Activity ctx, OnControleAddPedido onControleAddPedido) {
        this.ctx = ctx;
        this.onControleAddPedido = onControleAddPedido;
    }

    public void addUUID(String uuid) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", uuid));
        try {
            new ConexaoContas(ctx, f, "") {
                @Override
                public void contasOk(List<Conta> l) {
                    if (l.size()>0){
                        if (l.get(0).getTRAVA() == 1)
                            onControleAddPedido.numeroPedidoTravado(l.get(0));
                        else onControleAddPedido.numeroPedidoLiberado(l.get(0).getNUMERO());
                    } else onControleAddPedido.numeroPedidoErro("Conta Não Encontrada!");
                }

                @Override
                public void contasErro(String msg) {
                    onControleAddPedido.numeroPedidoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleAddPedido.numeroPedidoErro(e.getMessage());
        }
    }

    public void addNumero(long numero){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("NUMERO", "=", String.valueOf(numero)));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoContas(ctx, f, "") {
                @Override
                public void contasOk(List<Conta> l) {
                    if (l.size()>0){
                        if (l.get(0).getTRAVA() == 1)
                            onControleAddPedido.numeroPedidoTravado(l.get(0));
                        else onControleAddPedido.numeroPedidoLiberado(String.valueOf(numero));
                    } else onControleAddPedido.numeroPedidoLiberado(String.valueOf(numero));
                }

                @Override
                public void contasErro(String msg) {
                    onControleAddPedido.numeroPedidoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleAddPedido.numeroPedidoErro(e.getMessage());
        }
    }

    public void liberar(Conta c){
        try {
            c.setTRAVA(0);
            new ConexaoContaEdit(ctx, c) {
                @Override
                public void contasOk(Conta c) {
                    onControleAddPedido.numeroPedidoLiberado(c.getNUMERO());
                }

                @Override
                public void contasErro(String msg) {
                    onControleAddPedido.numeroPedidoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleAddPedido.numeroPedidoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleAddPedido.numeroPedidoErro(e.getMessage());
        }


    }
}
