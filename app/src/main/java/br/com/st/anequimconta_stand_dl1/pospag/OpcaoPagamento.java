package br.com.st.anequimconta_stand_dl1.pospag;

public class OpcaoPagamento {


    private String descricao ;

    public OpcaoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
