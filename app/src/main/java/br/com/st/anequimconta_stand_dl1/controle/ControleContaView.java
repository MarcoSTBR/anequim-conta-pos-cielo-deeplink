package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaView;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.net.MalformedURLException;
import java.util.List;

public class ControleContaView {

    private Activity ctx ;
    private OnControleContaView onControleContaView ;

    public ControleContaView(Activity ctx, OnControleContaView onControleContaView) {
        this.ctx = ctx;
        this.onControleContaView = onControleContaView;
    }

    public void getContaView(String uuid) {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", uuid));
        try {
            new ConexaoContaView(ctx, f, "") {
                @Override
                public void contaViewOk(List<ContaView> l) {
                    if (l.size() > 0)
                        onControleContaView.controleContaViewOk(l.get(0));
                    else onControleContaView.controleContaViewErro("Conta Não Encontrada!");
                }

                @Override
                public void contaViewErro(String msg) {
                    onControleContaView.controleContaViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaView.controleContaViewErro(e.getMessage());
        }
    }



}
