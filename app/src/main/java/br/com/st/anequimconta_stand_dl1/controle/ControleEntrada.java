package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoConfTerminal;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoPayStoneKey;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRefreshToken;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleRefreshToken;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Terminal;
import br.com.st.anequimconta_stand_dl1.pospag.ConfigPOS;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagType;
import br.com.st.anequimconta_stand_dl1.util.TokenSet;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.net.MalformedURLException;
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

    private void setPayStoneKey() {
        try {
            DaoFilters f = new DaoFilters();
            f.add(new DaoFilter("LOJA_ID", "=", String.valueOf(UtilSet.getLojaId(ctx))));
            f.add(new DaoFilter("STATUS", "=", "1"));
            f.add(new DaoFilter("TERMINAL_NUMERO", "=", UtilSet.getSerialNumberMAC(ctx)));
            f.add(new DaoFilter("TIPO", "=", "2")); /* 1-Venda Vista  2-Conta */
            new ConexaoPayStoneKey(ctx, f, "") {
                @Override
                public void ok(boolean b) {
                    if (b) ConfigPOS.setPosPagType(ctx, PosPagType.posCielo);
                    else ConfigPOS.setPosPagType(ctx, PosPagType.posNenhum);
                    verificarTerminal();
                }

                @Override
                public void erro(String msg) {
                    entradaErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            entradaErro(e.getMessage());
        }
    }

    private void refreshToken() {
        new ConexaoRefreshToken(ctx, new ListenerControleRefreshToken() {
            @Override
            public void ok() {
                setPayStoneKey();
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
