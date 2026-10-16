package br.com.st.anequimconta_stand_dl1.emissao_nfce.listener;


import br.com.st.anequimconta_stand_dl1.entidade.ContaMultNFCe;

public interface ListenerCancelarNFce {
    void ok(ContaMultNFCe nfCe) ;
    void erro(String msg) ;
}
