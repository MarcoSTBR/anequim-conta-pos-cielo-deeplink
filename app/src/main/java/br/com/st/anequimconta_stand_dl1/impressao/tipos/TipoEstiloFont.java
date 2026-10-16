package br.com.st.anequimconta_stand_dl1.impressao.tipos;

public enum TipoEstiloFont {
    ptsNormal(0), ptsBold(1),  ptsReverso(2) ;

    public int valor;

    TipoEstiloFont(int v) {
        valor = v;
    }

}
