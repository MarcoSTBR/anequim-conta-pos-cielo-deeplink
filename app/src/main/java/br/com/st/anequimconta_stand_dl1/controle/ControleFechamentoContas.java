package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContasFechar;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleFechamentoContas;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;

import org.json.JSONException;

import java.net.MalformedURLException;

public class ControleFechamentoContas {

    private Activity ctx ;
    private Caixa caixa ;
    private ContaMult contaMult ;
    private OnControleFechamentoContas onControleFechamentoContas;

    public ControleFechamentoContas(Activity ctx, Caixa caixa, ContaMult contaMult, OnControleFechamentoContas onControleFechamentoContas) {
        this.ctx = ctx;
        this.caixa = caixa;
        this.contaMult = contaMult;
        this.onControleFechamentoContas = onControleFechamentoContas;
    }

    public void fechar(){

        try {
            new ConexaoContasFechar(ctx, caixa, contaMult) {
                @Override
                public void contasFechamentoOk(ContaMult contaMult) {
                    onControleFechamentoContas.controleFechamentoContasOk(contaMult);
                }

                @Override
                public void contaFechamentoErro(String msg) {
                    onControleFechamentoContas.controleFechamentoContasErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleFechamentoContas.controleFechamentoContasErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onControleFechamentoContas.controleFechamentoContasErro(e.getMessage());
        }
    }
}
