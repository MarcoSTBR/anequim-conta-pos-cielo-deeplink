package br.com.st.anequimconta_stand_dl1.controle.listeners;


import br.com.st.anequimconta_stand_dl1.entidade.Produto;

public interface ListenerControleGradeProduto {

    void OnProdutoAdd(Produto p) ;
    void OnErro(String msg) ;
}
