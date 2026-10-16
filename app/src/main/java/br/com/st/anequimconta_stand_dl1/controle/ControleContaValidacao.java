package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaEdit;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaValidacao;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import org.json.JSONException;

import java.net.MalformedURLException;

public class ControleContaValidacao {

    private Activity ctx ;
    private OnControleContaValidacao onControleContaValidacao ;
    private Conta conta ;

    public ControleContaValidacao(Activity ctx, OnControleContaValidacao onControleContaValidacao) {
        this.ctx = ctx;
        this.onControleContaValidacao = onControleContaValidacao;
    }

    public void validarConta(Conta conta){

    }

    public void validarConta(ContaView contaView){
        conta = contaView.getConta() ;
        if (contaView.getContaItemViews().size() == 0) {
            fecharConta("Não há itens, deseja fechar?");
        }

    }

    private void fecharConta(String s) {
        new AlertDialog.Builder(ctx)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(s)
                .setCancelable(false)
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", (dialogInterface, i) -> {
                     fechar() ;
                })
                .show();
    }

    private void fechar() {
        conta.setSTATUS(0);
        try {
            new ConexaoContaEdit(ctx, conta) {
                @Override
                public void contasOk(Conta conta) {
                    if (conta.getSTATUS() == 0)
                        onControleContaValidacao.contaValidFechada();
                     else onControleContaValidacao.contaValidErro("Tente fechar a conta novamente!");
                }

                @Override
                public void contasErro(String msg) {
                    onControleContaValidacao.contaValidErro(msg);

                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleContaValidacao.contaValidErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleContaValidacao.contaValidErro(e.getMessage());
        }

    }
}
