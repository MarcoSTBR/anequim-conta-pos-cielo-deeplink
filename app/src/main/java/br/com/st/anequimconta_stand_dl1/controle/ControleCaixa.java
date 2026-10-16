package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoCaixa;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.ui.FragmentCaixa;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import java.net.MalformedURLException;
import java.util.Date;

public abstract class ControleCaixa {

    private Activity ctx;
    private int usuario_id ;

    public ControleCaixa(Activity ctx) {
        this.ctx = ctx;
    }

    public void getCaixaAberto(int usuarioId) {
        this.usuario_id = usuarioId ;
        getCaixa() ;
  }

    private void getCaixa() {
        try {
            new ConexaoCaixa(ctx) {
                @Override
                public void ok(Caixa c) {
                    if (c == null)
                        caixaFechado();
                    else caixaAberto(c);
                }

                @Override
                public void erro(String msg) {
                    caixaErro(msg) ;
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            caixaErro(e.getMessage()) ;
        } catch (JSONException e) {
            e.printStackTrace();
            caixaErro(e.getMessage()) ;
        }

    }


    public void abrirCaixa(int usuarioId) {
       this.usuario_id = usuarioId ;
        new ControleRecebeValor(ctx, "Fundo de Troco: ") {
            @Override
            public void valorDigitado(Double v) {
                abrir(v);
            }

            @Override
            public void erro(String msg) {
                caixaErro(msg);
            }
        };
    }

    private void abrir(Double v) {
        Caixa c = new Caixa(0, UtilSet.getUUID(),UtilSet.getLojaId(ctx), UtilSet.getUsuarioId(ctx), 2, UtilSet.getTerminalId(ctx),
                new Date(), new Date(), v, 1);
        gravarWeb(c);
    }

    private void gravarWeb(final Caixa c){
        try {
            new ConexaoCaixa(ctx, c) {
                @Override
                public void ok(Caixa c) {
                        getCaixa();
                }

                @Override
                public void erro(String msg) {
                    caixaErro(msg) ;
                }
            }.executar();
        } catch (MalformedURLException | JSONException e) {
            e.printStackTrace();
            caixaErro(e.getMessage()) ;
        }
    }

    public abstract void caixaAberto(Caixa c);

    public abstract void caixaFechado();

    public abstract void caixaErro(String msg);

}
