package br.com.st.anequimconta_stand_dl1.adapter.listeners;


import br.com.st.anequimconta_stand_dl1.entidade.Produto;

public interface ListenerAdapterProduto {
    void OnAddProduto(Produto produto) ;
    void OnChange(Produto produto) ;
    void OnProdutoErro(String msg) ;
}
