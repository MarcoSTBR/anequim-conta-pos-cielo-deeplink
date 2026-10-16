package br.com.st.anequimconta_stand_dl1.impressao.tipos;

public class RowImpressao {

    private String linha ;
    private TipoAlinhamento tipoAlinhamento ;
    private int tamFont ;
    private TipoEstiloFont tipoEstilo ;

    public RowImpressao(String linha, TipoAlinhamento tipoAlinhamento, int tamFont) {
        this.linha = linha;
        this.tipoAlinhamento = tipoAlinhamento;
        this.tamFont = tamFont;
        this.tipoEstilo = TipoEstiloFont.ptsNormal ;
    }

    public RowImpressao(String linha, TipoAlinhamento tipoAlinhamento, int tamFont, TipoEstiloFont tipoEstilo) {
        this.linha = linha;
        this.tipoAlinhamento = tipoAlinhamento;
        this.tamFont = tamFont;
        this.tipoEstilo = tipoEstilo ;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public TipoAlinhamento getTipoAlinhamento() {
        return tipoAlinhamento;
    }

    public void setTipoAlinhamento(TipoAlinhamento tipoAlinhamento) {
        this.tipoAlinhamento = tipoAlinhamento;
    }

    public int getTamFont() {
        return tamFont;
    }

    public void setTamFont(int tamFont) {
        this.tamFont = tamFont;
    }

    public TipoEstiloFont getTipoEstilo() {
        return tipoEstilo;
    }

    public void setTipoEstilo(TipoEstiloFont tipoEstilo) {
        this.tipoEstilo = tipoEstilo;
    }
}
