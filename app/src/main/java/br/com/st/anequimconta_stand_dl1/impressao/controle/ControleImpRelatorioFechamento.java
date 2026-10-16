package br.com.st.anequimconta_stand_dl1.impressao.controle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaPag;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerImpRelatorioFechamento;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.LinhaBitmapRow;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.LinhaBitmapStyle;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.RowImpressao;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoAlinhamento;
import br.com.st.anequimconta_stand_dl1.impressao.tipos.TipoEstiloFont;
import br.com.st.anequimconta_stand_dl1.impressao.util.UtilFechamentoParam;
import br.com.st.anequimconta_stand_dl1.tipos.TipoModalidade;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ControleImpRelatorioFechamento {
    private Activity ctx;
    private int tamCol = 40;

    private int larguraMax = 512 ;
    private int linhaNormal = 20;
    private int margemTop = 20 ;

    private int marginLeft = 0 ;
    private int spacamento = 0 ;
    private int linhaIntervalo ;

    private int linhaCorrente ;
    private List<LinhaBitmapRow> listaLinhas ;
    private Canvas canvas ;

    private Caixa caixa ;
    private List<ContaPag> listPag;

    private ListenerImpRelatorioFechamento listenerImpRelatorioFechamento ;

    public ControleImpRelatorioFechamento(Activity ctx, ListenerImpRelatorioFechamento listenerImpRelatorioFechamento) {
        this.ctx = ctx;
        this.listenerImpRelatorioFechamento = listenerImpRelatorioFechamento ;
    }

    public int getTamCol() {
        return tamCol;
    }

    public void setTamCol(int tamCol) {
        this.tamCol = tamCol;
    }

    public String getCabecalho(Caixa c) {
        SimpleDateFormat frm = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String cab = UtilSet.getCnpj(ctx)+"\n"+
                     UtilSet.getLojaNome(ctx)+"\n"+
                    "Abertura " + frm.format(c.getDATA());
        return cab;
    }

    public void getBitmap(Caixa c, int larguraMax, int linhaNormal){
        caixa = c ;
        this.larguraMax = larguraMax ;
        this.linhaNormal = linhaNormal ;
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("CAIXA_UUID", "=", caixa.getUUID()));
        f.add(new DaoFilter("STATUS", ">=", "1"));
        try {
            new ConexaoContaPag(ctx, f, "") {
                @Override
                public void conexaoContaPagOk(List<ContaPag> l) {
                    listPag = l;
                    contruirBitmap() ;
                }

                @Override
                public void conexaoContaPagErro(String msg) {
                    listenerImpRelatorioFechamento.ImpRelatorioFechamentoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerImpRelatorioFechamento.ImpRelatorioFechamentoErro(e.getMessage());
        }

    }

    public void getBitmap(Caixa c){
        caixa = c ;
        larguraMax = UtilFechamentoParam.getLarguraBitmap(ctx) ;
        linhaNormal = UtilFechamentoParam.getTamFont(ctx) ;
        getBitmap(c, larguraMax, linhaNormal) ;
    }

    private void contruirBitmap(){
        listaLinhas = new ArrayList<LinhaBitmapRow>();
        listaLinhas.add(new LinhaBitmapRow(linhaNormal , getCabecalhoRow(), LinhaBitmapStyle.Stl_Normal)) ;
        listaLinhas.add(new LinhaBitmapRow(getModalidadeRow(), linhaNormal, LinhaBitmapStyle.Stl_Blond)) ;
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
        listenerImpRelatorioFechamento.ImpRelatorioFechamentoOk(bitmapFinal);
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

    private List<RowImpressao> getCabecalhoRow(){
        SimpleDateFormat frm = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        List<RowImpressao> l = new ArrayList<RowImpressao>() ;
        l.add(new RowImpressao(UtilSet.getCnpj(ctx), TipoAlinhamento.ptCenter, 10)) ;
        l.add(new RowImpressao(UtilSet.getLojaNome(ctx), TipoAlinhamento.ptCenter, 10)) ;
        l.add(new RowImpressao("Abertura " + frm.format(caixa.getDATA()), TipoAlinhamento.ptCenter, 10)) ;
        return l ;
    }

    private List<String[]> getModalidadeRow(){
        List<String[]> l = new ArrayList<String[]>() ;
        DecimalFormat frv = new DecimalFormat("R$ #0.00");
//        String corpo = "";
        String md = "" ;
        String vl = "" ;
        l.add(new String[]{repetir("_", tamCol/2), repetir("_", tamCol/2)}) ;
        for (Modalidade m : getModalidades()) {
            md = m.getDescricao();
            Double vlr = getValorModalidade(m.getId()) ;
            if (m.getTipoModalidade() == TipoModalidade.pgDinheiro) {
                vlr -= getTroco(); // TIRA O TROCO
                l.add(new String[]{md+" ABERTURA", frv.format(caixa.getVALOR())}) ;
                l.add(new String[]{md+" VENDA", frv.format(vlr)}) ;
                vlr += caixa.getVALOR() ;
                l.add(new String[]{md+" TOTAL ", frv.format(vlr)}) ;
            } else {
                vl = frv.format(vlr);
                if (vlr > 0)
                    l.add(new String[]{md, vl});
            }
            //corpo += "\n" + md + repetir(" ", tamCol - md.length() - vl.length()) + vl;
        }
        l.add(new String[]{repetir("_", tamCol/2), repetir("_", tamCol/2)}) ;
        if ((getDesconto() > 0) || (caixa.getVALOR() > 0)) {
            md = "TOTAL BRUTO";
            vl = frv.format(getValorTotal() + caixa.getVALOR());
            l.add(new String[]{md, vl}) ;
            md = " - DESCONTO";
            vl = frv.format(getDesconto());
            l.add(new String[]{md, vl});
            md = " - ABERTURA";
            vl = frv.format(caixa.getVALOR());
            l.add(new String[]{md, vl});
            md = "TOTAL LIQUIDO";
            vl = frv.format(getValorTotal() - getDesconto());
            l.add(new String[]{md, vl});
        } else {
            md = "TOTAL LIQUIDO";
            vl = frv.format(getValorTotal());
            l.add(new String[]{md, vl}) ;
        }
        l.add(new String[]{repetir("_", tamCol/2), repetir("_", tamCol/2)}) ;
//        corpo += "\n" + repetir("_", tamCol);
      return l ;
    }
    private Double getValorModalidade(int id) {
        Double vl = 0.0;
        for (ContaPag pag : listPag) {
            if ((pag.getSTATUS() == 1) && (pag.getMODALIDADE_ID() == id))
                vl += pag.getVALOR();
        }
        return vl;
    }

    private Double getValorTotal() {
        Double vl = 0.0;
        for (ContaPag pag : listPag) {
            Modalidade m = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(pag.getMODALIDADE_ID()));
            if (pag.getSTATUS() == 1) {
                if (m.getTipoModalidade() == TipoModalidade.pgTroco)
                    vl -= pag.getVALOR();
                else
                    vl += pag.getVALOR();
            }
        }
        return vl;
    }

    private Double getTroco() {
        Double vl = 0.0;
        for (ContaPag pag : listPag) {
            Modalidade m = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(pag.getMODALIDADE_ID()));
            if ((pag.getSTATUS() == 1) && (m.getTipoModalidade() == TipoModalidade.pgTroco))
                vl += pag.getVALOR();
        }
        return vl;
    }

    private Double getDesconto() {
        Double vl = 0.0;
        for (ContaPag pag : listPag) {
            Modalidade m = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(pag.getMODALIDADE_ID()));
            if ((pag.getSTATUS() == 1) && (m.getTipoModalidade() == TipoModalidade.pgDesconto))
                vl += pag.getVALOR();
        }
        return vl;
    }

    private List<Modalidade> getModalidades() {
        DaoFilters f = new DaoFilters();
        f.add(new DaoFilter("TIPOMODALIDADE", "<>", TipoModalidade.pgTroco.valor));
        List<Modalidade> l = new ArrayList<>();
        try {
            l = ModalidadeDao.getModalidadeDao(ctx).getList(f, "ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l;
    }

    private String repetir(String s, int count) {
        String r = "";
        for (int i = 0; i < count; i++) {
            r = r + s;
        }
        return r;
    }
}
