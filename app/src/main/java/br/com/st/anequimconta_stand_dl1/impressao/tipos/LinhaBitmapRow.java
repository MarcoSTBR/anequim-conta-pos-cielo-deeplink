package br.com.st.anequimconta_stand_dl1.impressao.tipos;

import android.graphics.Bitmap;

import java.util.List;

public class LinhaBitmapRow {
    private int tamFont = 0;
    private List<RowImpressao> rows = null;
    private Bitmap bitmap = null;
    private LinhaBitmapStyle style = null;
    private List<String[]> linhaDupla = null;

    public LinhaBitmapRow(List<String[]> linhaDupla, int tamFont, LinhaBitmapStyle style) {
        this.tamFont = tamFont;
        this.style = style;
        this.linhaDupla = linhaDupla;
    }

    public LinhaBitmapRow(int tamFont, List<RowImpressao> rows) {
        this.tamFont = tamFont;
        this.rows = rows;
        bitmap = null;
        style = LinhaBitmapStyle.Stl_Normal;
    }

    public LinhaBitmapRow(int tamFont, List<RowImpressao> rows, LinhaBitmapStyle style) {
        this.tamFont = tamFont;
        this.rows = rows;
        this.style = style;
        bitmap = null;
    }

    public LinhaBitmapRow(Bitmap bitmap) {
        this.bitmap = bitmap;
        tamFont = bitmap.getHeight();
        rows = null;
        style = LinhaBitmapStyle.Stl_Normal;

    }

    public int getTamanhoLinha() {
        if (bitmap != null) {
            return bitmap.getHeight() + 3;
        } else if (linhaDupla != null) {
            return tamFont * linhaDupla.size();
        } else {
            return tamFont * rows.size();
        }
    }

    public int getTamFont() {
        return tamFont;
    }

    public void setTamFont(int tamFont) {
        this.tamFont = tamFont;
    }

    public List<RowImpressao> getRows() {
        return rows;
    }

    public void setRows(List<RowImpressao> rows) {
        this.rows = rows;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public LinhaBitmapStyle getStyle() {
        return style;
    }

    public void setStyle(LinhaBitmapStyle style) {
        this.style = style;
    }

    public List<String[]> getLinhaDupla() {
        return linhaDupla;
    }

    public void setLinhaDupla(List<String[]> linhaDupla) {
        this.linhaDupla = linhaDupla;
    }

}
