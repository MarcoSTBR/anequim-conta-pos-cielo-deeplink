package br.com.st.anequimconta_stand_dl1.pospag;

import java.util.Date;

public interface PosPagamentoView {
    String getId();
    String getBandeiraNome();
    String getDescricaoStatus();
    String getLast4Digits();
    Date getData();
    Double getValor();
}
