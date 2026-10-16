package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContasView;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ControleContasView {

    private Activity ctx ;
    private List<Conta> list ;
    private List<ContaView> listView ;
    private OnControleContasView onControleContasView;
    private int ponteiro ;

    public ControleContasView(Activity ctx, OnControleContasView onControleContaView) {
        this.ctx = ctx;
        this.onControleContasView = onControleContaView;
    }

    public void getContasView(List<Conta> list) {
        this.list = list ;
        listView = new ArrayList<ContaView>() ;
        ponteiro = 0 ;
        Log.i("ControleContasView", " ponteiro "+ ponteiro+" list "+ list.size()) ;
        if (list.size() > 0)
            getContaView(list.get(ponteiro)) ;
    }

    private void getContaView(Conta conta){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", conta.getUUID()));
        try {
            new ConexaoContaView(ctx, f, "") {
                @Override
                public void contaViewOk(List<ContaView> l) {
                    if (l.size() > 0) {
                        listView.add(l.get(0)) ;
                        if (++ponteiro < list.size()){
                            Log.i("ControleContasView", " getContaView ponteiro "+ ponteiro+" list "+ list.get(ponteiro).getNUMERO()) ;
                            getContaView(list.get(ponteiro)) ;
                        } else {
                            Log.i("ControleContasView", " Ok ponteiro "+ ponteiro+" listView "+ listView.size()) ;

                            onControleContasView.controleContaViewOk(listView);
                        }
                    } else onControleContasView.controleContaViewErro("Conta Não Encontrada!");
                }

                @Override
                public void contaViewErro(String msg) {
                    onControleContasView.controleContaViewErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContasView.controleContaViewErro(e.getMessage());
        }
    }

}
