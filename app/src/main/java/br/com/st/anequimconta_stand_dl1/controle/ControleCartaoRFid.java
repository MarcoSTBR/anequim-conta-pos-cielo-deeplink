package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import java.net.MalformedURLException;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoCartaoRFid;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleCartaoRFid;
import br.com.st.anequimconta_stand_dl1.entidade.CartaoRFid;

public class ControleCartaoRFid {

    private Activity ctx ;
    private OnControleCartaoRFid onControleCartaoRFid ;

    public ControleCartaoRFid(Activity ctx, OnControleCartaoRFid onControleCartaoRFid) {
        this.ctx = ctx;
        this.onControleCartaoRFid = onControleCartaoRFid;
    }


    public void pesquisar(String rfid){
        try {
            new ConexaoCartaoRFid(ctx, rfid) {
                @Override
                public void ok(CartaoRFid rFid) {
                    if (rFid.getSTATUS() == 1) {
                        onControleCartaoRFid.OnControleRFid(rFid);
                    } else {
                        onControleCartaoRFid.OnControleRFidErro("["+rFid.getSTATUS()+"] "+rFid.getMOTIVO());
                    }
                }

                @Override
                public void erro(String msg) {
                    onControleCartaoRFid.OnControleRFidErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleCartaoRFid.OnControleRFidErro(e.getMessage());
        }


    }
}
