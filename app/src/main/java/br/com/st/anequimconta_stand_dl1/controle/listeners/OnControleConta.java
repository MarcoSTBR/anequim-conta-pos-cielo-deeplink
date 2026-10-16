package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.Conta;

import java.util.List;

public interface OnControleConta {
    void contasOk(List<Conta> l) ;
    void contaErro(String msg) ;
}
