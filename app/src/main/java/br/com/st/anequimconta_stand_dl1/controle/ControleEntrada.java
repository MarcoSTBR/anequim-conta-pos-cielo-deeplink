package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoConfTerminal;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRefreshToken;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleRefreshToken;
import br.com.st.anequimconta_stand_dl1.entidade.Terminal;
import br.com.st.anequimconta_stand_dl1.util.TokenSet;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ControleEntrada {

    private final String DATA_PRODUTOS = "DATA_PRODUTOS" ;
    private Activity ctx;

    public ControleEntrada(Activity ctx) {
        this.ctx = ctx;
    }

    public void executar() {
        if (verificaTokenVazio())
            autenticacao();
        else refreshToken();
    }


    private void refreshToken() {
        new ConexaoRefreshToken(ctx, new ListenerControleRefreshToken() {
            @Override
            public void ok() {
                verificarTerminal();

            }

            @Override
            public void erro(int cod, String msg) {
                if (cod != 0) {
                    UtilSet.gravaParamString(ctx, DATA_PRODUTOS, "") ;
                    autenticacao();
                } else entradaErro("COD: " + cod + " " + msg);

            }
        }).executar();
    }

    private void verificarTerminal() {
        new ConexaoConfTerminal(ctx, UtilSet.getSerialNumberMAC(ctx)) {
            @Override
            public void ok(Terminal t) {
                if (UtilSet.getUsuarioId(ctx) == 0)
                    login();
                else
                  atualizarDados() ;
            }

            @Override
            public void erroMsg(int cod, String msg) {
                terminal();
            }
        }.executar();
    }

    private void atualizarDados(){
        final String d = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;
        if (!UtilSet.lerParamString(ctx, DATA_PRODUTOS).equals(d)) {
            new ControleAtualizar(ctx) {
                @Override
                public void atualizacaoOk() {
                    venda();
                    UtilSet.gravaParamString(ctx, DATA_PRODUTOS, d) ;
                }

                @Override
                public void atualizacaoErro(String msg) {
                    entradaErro(msg);
                }
            }.executar();
        } else venda() ;

    }

    private Boolean verificaTokenVazio() {
        Log.i("token_", "Token: " + UtilSet.getToken(ctx));
        String t = UtilSet.getToken(ctx);
        if (t.isEmpty())
            return true;
        else return false;
    }

    private Boolean verificaValidade() {
        try {
            if (false)  // eliminar data de validade
                TokenSet.validate(ctx);
            return true;
        } catch (TokenSet.ExceptionTokenExpirado e) {
            // e.getMessage() ;
            return false;
        }
    }

    public abstract void venda();

    public abstract void entradaErro(String msg);

    public abstract void autenticacao();

    public abstract void login();

    public abstract void terminal();


}
