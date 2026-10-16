package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContas;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ControlePedidoConta {

    private Activity ctx ;
    private int ponteiro ;
    private List<PedidoView> lip ;
    private List<Conta> list ;

    public ControlePedidoConta(Activity ctx) {
        this.ctx = ctx;
    }

    public void getContas(List<PedidoView> l){
        lip = l ;
        list = new ArrayList<Conta>() ;
        ponteiro = 0 ;
        if (lip.size()>0)
            getConta(lip.get(ponteiro));
         else contas(list);

    }

    private void getConta(PedidoView p){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("NUMERO", "=", p.getPedido().getNUMERO()));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoContas(ctx, f, "") {
                @Override
                public void contasOk(List<Conta> l) {
                    if (l.size() >0) {
                        list.add(l.get(0));
                        if (++ponteiro < lip.size())
                            getConta(lip.get(ponteiro));
                        else contas(list);
                    } else {
                        if (++ponteiro < lip.size())
                            getConta(lip.get(ponteiro));
                        else contas(list);
                    }

                }

                @Override
                public void contasErro(String msg) {

                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public abstract void contas(List<Conta> l) ;
}
