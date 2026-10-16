package br.com.st.anequimconta_stand_dl1.pospag.payCielo.services;

public interface OnCrachaLidoListener {
    void onCrachaDetectado(String codigoUid);
    void onCrachaErro(String msg) ;
    void onCrachaChange(String msg) ;

}
