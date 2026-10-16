package br.com.st.anequimconta_stand_dl1.emissao_nfce.listener;

import br.com.st.anequimconta_stand_dl1.entidade.ContaMultNFCe;

public interface ListenerEmitirNFce {
    void ok(ContaMultNFCe vnfc) ;
    void erro(String msg) ;
}
