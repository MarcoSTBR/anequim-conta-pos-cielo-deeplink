package br.com.st.anequimconta_stand_dl1.emissao_nfce.listener;

public interface ListenerConfiguracaoCloudNFCe {

    void ok(String token) ;
    void erro(int cod, String msg) ;
}
