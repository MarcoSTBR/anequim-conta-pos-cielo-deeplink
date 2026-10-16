package br.com.st.anequimconta_stand_dl1.controle.listeners;

import android.graphics.Bitmap;

public interface OnControleComandaRemota {
    void comandaOk() ;
    void comandaChange(String msg, Bitmap bitmap) ;
    void comandaErro(String msg) ;

}
