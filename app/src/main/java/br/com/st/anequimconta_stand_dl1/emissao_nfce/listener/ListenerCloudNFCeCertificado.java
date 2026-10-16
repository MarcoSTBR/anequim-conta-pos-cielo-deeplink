package br.com.st.anequimconta_stand_dl1.emissao_nfce.listener;

import java.util.Date;

public interface ListenerCloudNFCeCertificado {
    void ok(String msg, Date validade) ;
    void erro(int cod, String msg) ;
}
