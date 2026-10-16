package br.com.st.anequimconta_stand_dl1.controle.listeners;

import android.graphics.Bitmap;

public interface ListenerImpRelatorioFechamento {
    void ImpRelatorioFechamentoOk(Bitmap bitmap) ;
    void ImpRelatorioFechamentoErro(String msg) ;

}
