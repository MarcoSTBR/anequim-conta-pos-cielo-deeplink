package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class ContaMultView {

    private List<Conta> contaList ;
    private ContaMult contaMult ;
    private List<ContaPag> contaPagList ;

    public ContaMultView(List<Conta> contaList, ContaMult contaMult, List<ContaPag> contaPagList) {
        this.contaList = contaList;
        this.contaMult = contaMult;
        this.contaPagList = contaPagList;
    }

    public List<Conta> getContaList() {
        return contaList;
    }

    public void setContaList(List<Conta> contaList) {
        this.contaList = contaList;
    }

    public ContaMult getContaMult() {
        return contaMult;
    }

    public void setContaMult(ContaMult contaMult) {
        this.contaMult = contaMult;
    }

    public List<ContaPag> getContaPagList() {
        return contaPagList;
    }

    public void setContaPagList(List<ContaPag> contaPagList) {
        this.contaPagList = contaPagList;
    }
}
