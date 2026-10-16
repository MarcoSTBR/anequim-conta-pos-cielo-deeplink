package br.com.st.anequimconta_stand_dl1.controle.listeners;

public interface ListenerControleLiberacaoAcesso {
    void acessoOk(int usuario_id) ;
    void acessoErro(String msg) ;
}
