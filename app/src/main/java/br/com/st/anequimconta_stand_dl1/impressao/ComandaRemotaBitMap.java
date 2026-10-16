package br.com.st.anequimconta_stand_dl1.impressao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;

import br.com.st.anequimconta_stand_dl1.entidade.ImpressaoRemota;
import br.com.st.anequimconta_stand_dl1.entidade.ImpressaoRemotaItem;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.LinhaBitmapRow;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.LinhaBitmapStyle;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.RowImpressao;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoAlinhamento;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoEstiloFont;
import br.com.st.anequimconta_stand_dl1.impressao.util.UtilContaParam;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ComandaRemotaBitMap {

    private Activity ctx ;
    private ImpressaoRemota impressaoRemota;

    private Canvas canvas ;
    private List<LinhaBitmapRow> listaLinhas ;
    private int larguraBitmap = 700;
    private int linhaNormal = 20;

    private int linhaCorrente, linhaIntervalo ;
    private int marginLeft = 1 ;
    private int margemTop = 10 ;
    private int spacamento = 0 ;
    private int larguraMax ;
    private int tamCol = 48 ;

    public ComandaRemotaBitMap(Activity ctx, ImpressaoRemota impressaoRemota) {
        this.ctx = ctx ;
        this.impressaoRemota = impressaoRemota;
    }

    public Bitmap getBitMap(){
        larguraMax = UtilContaParam.getLarguraBitmap(ctx) ;
        linhaNormal = UtilContaParam.getTamFont(ctx);
        larguraBitmap = larguraMax;
        linhaCorrente = margemTop + linhaNormal ;
        linhaIntervalo = linhaCorrente ;
        listaLinhas = new ArrayList<>();

        listaLinhas.add(new LinhaBitmapRow(linhaNormal , getLinhaPontilhada(), LinhaBitmapStyle.Stl_Blond)) ;
        listaLinhas.add(new LinhaBitmapRow(linhaNormal + 5, getCabecalho(), LinhaBitmapStyle.Stl_Blond)) ;
        listaLinhas.add(new LinhaBitmapRow(linhaNormal , getLinhaPontilhada(), LinhaBitmapStyle.Stl_Blond)) ;
        listaLinhas.add(new LinhaBitmapRow(linhaNormal, getItens(), LinhaBitmapStyle.Stl_Normal)) ;
        listaLinhas.add(new LinhaBitmapRow(linhaNormal , getLinhaPontilhada(), LinhaBitmapStyle.Stl_Blond)) ;
        Bitmap bitmapFinal = Bitmap.createBitmap(larguraMax, (int) getAlturaMax(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmapFinal);
        canvas.drawColor(Color.WHITE);
        for (LinhaBitmapRow l : listaLinhas) {
            if (l.getBitmap() != null)
                imprimeBitMap(l);
            else if (l.getLinhaDupla() != null)
                imprimeLinhaDupla(l);
            else
                imprimeLinha(l);
        }
        return bitmapFinal ;
    }

/*
    private List<RowImpressao> getReferencia() {
        String s = contaView.getConta().getNUMERO() ;
        List<RowImpressao> l = new ArrayList<>() ;
        l.add(new RowImpressao(" ", TipoAlinhamento.ptCenter,  20)) ;
        l.add(new RowImpressao( s, TipoAlinhamento.ptCenter,  20)) ;
        l.add(new RowImpressao(" ", TipoAlinhamento.ptCenter,  20)) ;
        return l ;
    }
*/


    private void imprimeBitMap(LinhaBitmapRow l) {
        Log.i("imprimeLinha", "bitmap getHeight() "+l.getBitmap().getHeight());
        //  canvas.drawBitmap(l.getBitmap(), new Rect(100,100,150,150), new Rect(0,0,50,50), null);
        float posicaoCol = (larguraMax / 2) -  (l.getBitmap().getWidth() / 2) ;//(canvas.getWidth() / 2) - (l.getBitmap().getWidth() / 2);
        Paint tPaint = new Paint();
        tPaint.setTextSize(1);
        tPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawBitmap(l.getBitmap(), posicaoCol, linhaCorrente, tPaint);
        Log.i("imprimeLinha", "tamFont "+ l.getBitmap().getHeight() +"linhacorrente "+linhaCorrente+" / "+getAlturaMax()+" bitmap max ");
        linhaCorrente += l.getBitmap().getHeight() ;
        linhaIntervalo+= l.getBitmap().getHeight() ;
        //  linhaCorrente += l.getBitmap().getHeight() + linhaIntervalo * 2 + 3 + spacamento ;
    }

    private void imprimeLinhaDupla(LinhaBitmapRow l) {
        int colorFont = Color.BLACK ;
        for (String[] txt : l.getLinhaDupla()) {
            Paint tPaint = new Paint();
            tPaint.setTextSize(l.getTamFont());
            tPaint.setStyle(Paint.Style.FILL);
            tPaint.setTextAlign(Paint.Align.LEFT);
            tPaint.setColor(colorFont);
            if (l.getStyle() == LinhaBitmapStyle.Stl_Blond)
                tPaint.setTypeface(Typeface.DEFAULT_BOLD) ;
            else tPaint.setTypeface(Typeface.DEFAULT) ;
            canvas.drawText(txt[0], marginLeft, linhaCorrente, tPaint);
            tPaint = new Paint();
            tPaint.setTextSize(l.getTamFont());
            tPaint.setStyle(Paint.Style.FILL);
            tPaint.setTextAlign(Paint.Align.RIGHT);
            tPaint.setColor(colorFont);
            if (l.getStyle() == LinhaBitmapStyle.Stl_Blond)
                tPaint.setTypeface(Typeface.DEFAULT_BOLD) ;
            else tPaint.setTypeface(Typeface.DEFAULT) ;
            canvas.drawText(txt[1], larguraMax, linhaCorrente, tPaint);
            linhaCorrente += l.getTamFont() + spacamento;
            linhaIntervalo = l.getTamFont() + spacamento;
        }
    }

    private void imprimeLinha(LinhaBitmapRow l) {
        int col =0;
        int colorFont = Color.BLACK ;
        for (RowImpressao row : l.getRows()) {
            Paint tPaint = new Paint();
            tPaint.setTextSize(l.getTamFont());
            tPaint.setStyle(Paint.Style.FILL);
            String txt = row.getLinha();
            switch (row.getTipoAlinhamento()) {
                case ptCenter: {
                    col = larguraMax / 2;
                    tPaint.setTextAlign(Paint.Align.CENTER);
                }
                break;
                case ptRight: {
                    col = larguraMax ;// marginLeft ; //getLarguraMax() - tamLength - tamRecuo  ;
                    tPaint.setTextAlign(Paint.Align.RIGHT);
                }
                break;
                default: {
                    col = marginLeft;
                    tPaint.setTextAlign(Paint.Align.LEFT);
                }
            }
            if (row.getTipoEstilo() == TipoEstiloFont.ptsBold) {
                //tPaint.setTypeface(Typeface.DEFAULT_BOLD) ;
                tPaint.setTypeface(Typeface.DEFAULT_BOLD) ;
            } else {
                tPaint.setTypeface(Typeface.DEFAULT);
            }
            if (row.getTipoEstilo() == TipoEstiloFont.ptsReverso) {
                tPaint.setTextSize(l.getTamFont()+2);
                tPaint.setTypeface(Typeface.DEFAULT_BOLD) ;
            }
            tPaint.setColor(colorFont);
            canvas.drawText(txt, col, linhaCorrente, tPaint);
            Log.i("imprimeLinha", "tamFont "+ l.getTamFont() + " linhaCorrente "+linhaCorrente+" / "+getAlturaMax()+" "+txt);

            linhaCorrente += l.getTamFont() + spacamento;
            linhaIntervalo = l.getTamFont() + spacamento;
        }
    }

    private float getAlturaMax() {
        int max = margemTop;
        for (LinhaBitmapRow l : listaLinhas) {
            max += l.getTamanhoLinha();
        }
        return max + margemTop ;
    }

    private List<RowImpressao> getCabecalho(){
        List<RowImpressao> l = new ArrayList<>() ;
        l.add(new RowImpressao(impressaoRemota.getPEDIDO(), TipoAlinhamento.ptCenter, 10)) ;
        if (!impressaoRemota.getPEDIDO().equals(impressaoRemota.getREFERENCIA()))
        l.add(new RowImpressao("["+impressaoRemota.getREFERENCIA()+"]", TipoAlinhamento.ptCenter, 10)) ;
        l.add(new RowImpressao("Data : " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(impressaoRemota.getDATA()), TipoAlinhamento.ptCenter, 10)) ;
        return l ;
    }

    private List<RowImpressao> getItens(){
        List<RowImpressao> l = new ArrayList<>() ;
        for (ImpressaoRemotaItem it : impressaoRemota.getITENS()) {
            Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(it.getPRODUTO_ID()) ;
            String desc = p.getDescricao() ;
            String qp = new DecimalFormat("#0.###").format(it.getQUANTIDADE())+" "+desc ;
            l.add(new RowImpressao(qp, TipoAlinhamento.ptLeft, 20)) ;
            if (it.getOBS().length()>0)
                l.add(new RowImpressao(it.getOBS(), TipoAlinhamento.ptLeft, 10)) ;

        }
        return l ;
    }

    private List<String[]> getLinhaDupla(String a, String b){
        List<String[]> l = new ArrayList<String[]>() ;
        l.add(new String[]{a,b}) ;
        return l ;
    }

    private List<RowImpressao> getLinhaPontilhada(){
        List<RowImpressao> l = new ArrayList<>() ;
        l.add(new RowImpressao(repetir("_", tamCol), TipoAlinhamento.ptCenter, 10)) ;
        return l ;
    }
    private List<RowImpressao> getLinha(String s, TipoAlinhamento t){
        List<RowImpressao> l = new ArrayList<>() ;
        l.add(new RowImpressao(s, t, 10)) ;
        return l ;
    }

    private String repetir(String s, int q){
        String txt = "" ;
        for (int i = 0 ; i<=q ; i++){
            txt += s ;
        }
        return txt ;
    }

}
