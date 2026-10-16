package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.app.AlertDialog;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoCaixa;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.Date;

public abstract class ControleFecharCaixa {

    private Activity ctx;
    private Caixa caixa;

    public ControleFecharCaixa(Activity ctx, Caixa caixa) {
        this.ctx = ctx;
        this.caixa = caixa;
    }

    public void executar() {
        new AlertDialog.Builder(ctx)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Caixa")
                .setMessage("Deseja Fechar O Caixa?")
                .setCancelable(false)
                .setPositiveButton("Sim", (dialogInterface, i) -> fechar())
                .setNegativeButton("Não", (dialogInterface, i) -> fechamentoErro("Caixa Aberto!"))
                .show();
/*

        if (!seVendaAberta())
          fechar();
        else vendaAberta();
*/
//        fechar() ;
    }

    private void fechar() {
        caixa.setSTATUS(2);
        caixa.setDATA_FECHAMENTO(new Date());
        try {
            new ConexaoCaixa(ctx, caixa) {
                @Override
                public void ok(Caixa c){
                    fechamentoOK() ;
                }

                @Override
                public void erro(String msg) {
                    fechamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fechamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            fechamentoErro(e.getMessage());
        }
    }

    public abstract void fechamentoOK();
    public abstract void vendaAberta();
    public abstract void fechamentoErro(String msg);


}
