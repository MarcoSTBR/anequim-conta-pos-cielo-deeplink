package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.CartaoRFid;

public interface OnControleCartaoRFid {
    void OnControleRFid(CartaoRFid rfid) ;
    void OnControleRFidErro(String msg) ;
}
