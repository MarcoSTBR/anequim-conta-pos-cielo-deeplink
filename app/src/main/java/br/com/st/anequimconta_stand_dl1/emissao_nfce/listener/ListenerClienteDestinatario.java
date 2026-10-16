package br.com.st.anequimconta_stand_dl1.emissao_nfce.listener;

import br.com.st.anequimconta_stand_dl1.entidade.ContaMultNFCe;

public interface ListenerClienteDestinatario {

    void ok(ContaMultNFCe contaMultNFCe);
    void cancelar(String msg) ;
}
