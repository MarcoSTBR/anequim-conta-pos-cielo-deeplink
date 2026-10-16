package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.Produto;


public abstract class ControleProdutoValorAdd {
    private Activity ctx;

    public ControleProdutoValorAdd(Activity ctx) {
        this.ctx = ctx;
    }

    public void addProduto(Produto p) {
        ok(p, 1.0);
    }

    public abstract void ok(Produto p, Double quantidade);

    public abstract void erro(String msg);
}
