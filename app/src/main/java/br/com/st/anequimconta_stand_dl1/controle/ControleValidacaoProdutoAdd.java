package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;

import java.util.List;

public abstract class ControleValidacaoProdutoAdd {
    private Activity ctx;
    private Double valor;
    private String msng;


    public ControleValidacaoProdutoAdd(Activity ctx) {
        this.ctx = ctx;
    }

    public String getMsg() {
        return msng;
    }

    public void validate(PedidoItem it, List<AcompanhamentoView> listAc) {
        valor = it.getQUANTIDADE() * it.getPRECO();
        Boolean flag = true;
        for (AcompanhamentoView ac : listAc) {
            if (!acomp_ok(it.getQUANTIDADE(), ac)) {
                flag = false;
            }
        }
        if (!flag) naoLiberado(valor);
        else liberado(valor);
    }

    public Boolean acomp_ok(Double quantUnit, AcompanhamentoView ac) {
        if (ac.getAcompanhamento().getMin() == 0) return true;
        else {
            int aAcc = 0;
            Boolean flagIt = true;
            for (AcompanhamentoItemView acv : ac.getAcompanhamentoItems()) {
                aAcc += acv.getQuantidade();
                valor += quantUnit * acv.getQuantidade() * acv.getAcompanhamentoItem().getPreco();
                if (acv.getQuantidade() < acv.getAcompanhamentoItem().getMin()) {
                    msng = "Mínimo de " + acv.getAcompanhamentoItem().getMin() + " unidade(s)!";
                    flagIt = false;
                }
                if (acv.getQuantidade() > acv.getAcompanhamentoItem().getMax()) {
                    msng = "Máximo de " + acv.getAcompanhamentoItem().getMax() + " unidade(s)!";
                    flagIt = false;
                }
            }
            if (!flagIt) {
                mensagem(msng);
                return false;
            }
            if (aAcc < ac.getAcompanhamento().getMin()) {
                msng = "Mínimo de " + ac.getAcompanhamento().getMin() + " para " + ac.getAcompanhamento().getDescricao();
                mensagem(msng);
                return false;
            }
            if (aAcc > ac.getAcompanhamento().getMax()) {
                msng = "Máximo de " + ac.getAcompanhamento().getMax() + " para " + ac.getAcompanhamento().getDescricao();
                mensagem(msng);
                return false;
            }
            return true;
        }
    }

    public abstract void liberado(Double valor);

    public abstract void naoLiberado(Double valor);

    public abstract void mensagem(String msg);
}
