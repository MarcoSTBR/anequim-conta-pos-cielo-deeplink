package br.com.st.anequimconta_stand_dl1.controle.listeners;


import android.content.Intent;

import androidx.annotation.Nullable;

public interface OnControleValidaTicket {
    void startActivityForResult(int requestCode, int resultCode, @Nullable Intent data);

    void ticketRecebido(String obs) ;
    void ticketInvalido(String msg) ;
    void ticketErro(String msg) ;
}
