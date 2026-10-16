package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;

import java.util.List;

public interface OnControlePagamento {
    void contasList(List<Conta> l) ;
    void pagamentoList(List<ContaPag> l) ;
    void total(Double total, Double totalApagar, Double TotalPago) ;
    void pagamentoErro(String msg) ;
}
