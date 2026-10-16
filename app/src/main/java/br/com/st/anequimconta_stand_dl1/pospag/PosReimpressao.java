package br.com.st.anequimconta_stand_dl1.pospag;

import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControlePosReimpressao;

public interface PosReimpressao {
    void reimpressao(PosPagamentoView posPagamentoView, OnControlePosReimpressao onControlePosReimpressao) ;
}
