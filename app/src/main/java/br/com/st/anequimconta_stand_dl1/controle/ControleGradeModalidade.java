package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoModalidadePos;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleModalidade;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;

import java.util.List;

public class ControleGradeModalidade {

    private Activity ctx ;
    private OnControleModalidade onControleModalidade ;

    public ControleGradeModalidade(Activity ctx, OnControleModalidade onControleModalidade) {
        this.ctx = ctx;
        this.onControleModalidade = onControleModalidade;

    }

    public void executar(){
        new ConexaoModalidadePos(ctx) {
            @Override
            public void ok(List<Modalidade> l) {
                onControleModalidade.controleModalidadeOk(l);

            }

            @Override
            public void erro(String msg) {
                onControleModalidade.controleModalidadeErro(msg);
            }
        }.execute();
    }
}
