package br.com.st.anequimconta_stand_dl1.controle.listeners;

public interface OnControleContaValidacao {
    void contaValidOk() ;
    void contaValidFechada() ;
    void contaValidErro(String msg) ;

}
