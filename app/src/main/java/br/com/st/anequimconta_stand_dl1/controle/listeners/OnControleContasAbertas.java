package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.util.List;

public interface OnControleContasAbertas {
    void contaViewOk(List<ContaView> contaViews) ;
    void contaViewErro(String msg);
}
