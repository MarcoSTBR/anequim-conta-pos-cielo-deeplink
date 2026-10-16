package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContasAbertas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContasView;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ControleContasAbertas {

    private Activity ctx ;
    private OnControleContasAbertas onControleContasAbertas ;
    private static List<ContaView> list ;

    public ControleContasAbertas(Activity ctx, OnControleContasAbertas onControleContasAbertas) {
        this.ctx = ctx;
        this.onControleContasAbertas = onControleContasAbertas;
        list = new ArrayList<>() ;
    }

    public void refress(){
        List<Conta> listContas ;
        if (list.size() > 0){
            listContas = new ArrayList<>() ;
            for (ContaView cv : list)
                listContas.add(cv.getConta()) ;
            new ControleContasView(ctx, new OnControleContasView() {
                @Override
                public void controleContaViewOk(List<ContaView> contaViews) {
                    list = contaViews ;
                    onControleContasAbertas.contaViewOk(list);
                }

                @Override
                public void controleContaViewErro(String msg) {
                    onControleContasAbertas.contaViewErro(msg);
                }
            }).getContasView(listContas);

        }
    }


    public void addContas(String uuid){
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("UUID", "=", uuid));
        try {
            new ConexaoContas(ctx, f, "") {
                @Override
                public void contasOk(List<Conta> l) {
                    if (l.size()>0)
                        verificar(l.get(0));
                    else onControleContasAbertas.contaViewErro("Conta não encontrada!");
                }

                @Override
                public void contasErro(String msg) {
                    onControleContasAbertas.contaViewErro(msg) ;
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContasAbertas.contaViewErro(e.getMessage());
        }
    }

    private void verificar(Conta c){
        if (seFoiSelecionado(c.getUUID())) {
            Log.i("contains", "sim") ;
            onControleContasAbertas.contaViewErro("Conta Já foi selecionada!");
        } else {
            Log.i("contains", "não") ;
            addContaView(c) ;
        }
    }

    private void addContaView(Conta c){
        new ControleContaView(ctx, new OnControleContaView() {
            @Override
            public void controleContaViewOk(ContaView contaView) {
                list.add( contaView);
                onControleContasAbertas.contaViewOk(list);
            }

            @Override
            public void controleContaViewErro(String msg) {
                onControleContasAbertas.contaViewErro(msg);
            }
        }).getContaView(c.getUUID());

    }

    private boolean seFoiSelecionado(String uuid){
        boolean f = false ;
        for (ContaView c : list){
            if (c.getConta().getUUID().equals(uuid))
                f = true ;
        }
        return f ;
    }

    public void remove(ContaView cv){
        list.remove(cv) ;
        onControleContasAbertas.contaViewOk(list);
    }
}
