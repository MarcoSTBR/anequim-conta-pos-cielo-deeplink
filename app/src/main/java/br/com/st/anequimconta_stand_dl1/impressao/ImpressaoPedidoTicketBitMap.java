package br.com.st.anequimconta_stand_dl1.impressao;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAddTicketView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoTicketView;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.LinhaBitmapRow;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.LinhaBitmapStyle;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.RowImpressao;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoAlinhamento;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoEstiloFont;
import br.com.st.anequimconta_stand_dl1.impressao.util.UtilContaParam;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ImpressaoPedidoTicketBitMap {

    private Activity ctx ;
    private List<PedidoTicketView> pedidoTickets;
    private PedidoTicketView pedidoTicketCorrente ;

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

    public ImpressaoPedidoTicketBitMap(Activity ctx, List<PedidoTicketView> pedidoTickets) {
        this.ctx = ctx ;
        this.pedidoTickets = pedidoTickets;
    }

    public Bitmap getBitMap(){
        larguraMax = UtilContaParam.getLarguraBitmap(ctx) ;
        linhaNormal = UtilContaParam.getTamFont(ctx);
        return getBitMap(larguraMax, linhaNormal) ;
    }

    public Bitmap getBitMap(int larguraMax, int linhaNormal){
        this.larguraMax = larguraMax ;
        this.linhaNormal = linhaNormal;
        larguraBitmap = larguraMax;
        linhaCorrente = margemTop + linhaNormal ;
        linhaIntervalo = linhaCorrente ;
        listaLinhas = new ArrayList<>();
        for (PedidoTicketView pt : pedidoTickets) {
            pedidoTicketCorrente = pt ;
            Log.i("tickets", "bitMap "+pedidoTicketCorrente.getProduto().getDescricao()) ;
            listaLinhas.add(new LinhaBitmapRow(linhaNormal, getLinhaPontilhada(), LinhaBitmapStyle.Stl_Blond));
            listaLinhas.add(new LinhaBitmapRow(linhaNormal + 5, getCabecalho(), LinhaBitmapStyle.Stl_Blond));
            listaLinhas.add(new LinhaBitmapRow(linhaNormal + 10, getItens(), LinhaBitmapStyle.Stl_Normal));
            Bitmap qrCode = getQRCodeBitMap(pt.getPedidoTiket().getUUID());
            listaLinhas.add(new LinhaBitmapRow(qrCode)) ;
            listaLinhas.add(new LinhaBitmapRow(linhaNormal, getLinhaPontilhada(), LinhaBitmapStyle.Stl_Blond));
        }
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

    private List<RowImpressao> getCabecalho(){
        List<RowImpressao> l = new ArrayList<>() ;
        l.add(new RowImpressao("TICKET", TipoAlinhamento.ptCenter, 20)) ;
        return l ;
    }

    private List<RowImpressao> getItens(){
        List<RowImpressao> l = new ArrayList<>() ;
        l.add(new RowImpressao(pedidoTicketCorrente.getProduto().getDescricao(), TipoAlinhamento.ptCenter, 10)) ;
        if (pedidoTicketCorrente.getPedidoItemAddTicketViews().size() > 0) {
            for (PedidoItemAddTicketView add : pedidoTicketCorrente.getPedidoItemAddTicketViews()) {
                l.add(new RowImpressao("*"+new DecimalFormat("#0.###").format(add.getQuantidade())+ " "+add.getProduto().getDescricao(), TipoAlinhamento.ptCenter, 10)) ;
            }
        }
        return l ;
    }

    public Bitmap getQRCodeBitMap(String dados) {
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(dados, BarcodeFormat.QR_CODE, 512, 512);
            int w = bitMatrix.getWidth();
            int h = bitMatrix.getHeight();
            Bitmap bitmapRet = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    bitmapRet.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmapRet;
        } catch (WriterException e){
            e.printStackTrace();
            return null ;
        }
    }


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
