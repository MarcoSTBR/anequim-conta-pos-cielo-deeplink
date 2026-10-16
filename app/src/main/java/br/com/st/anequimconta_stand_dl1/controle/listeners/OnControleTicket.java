package br.com.st.anequimconta_stand_dl1.controle.listeners;

import android.graphics.Bitmap;

public interface OnControleTicket {

    void impTicketOk() ;
    void impTicketChange(String msg, Bitmap bitMap);
    void impTicketErro(String msg) ;
}
