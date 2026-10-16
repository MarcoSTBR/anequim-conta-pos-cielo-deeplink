package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.activity.ActivityEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRequisicaoRemota;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleComandaRemota;
import br.com.st.anequimconta_stand_dl1.entidade.ImpressaoRemota;
import br.com.st.anequimconta_stand_dl1.impressao.ComandaRemotaBitMap;
import br.com.st.anequimconta_stand_dl1.impressao.ImpressaoBitmapBuilder;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.util.List;

public class ControleComandaRemota implements ListenerImpressaoBipMap {
    private Activity ctx ;
    private OnControleComandaRemota onControleComandaRemota ;
    private int ponteiro ;
    private List<ImpressaoRemota> list ;
    private OnImpressaoBitMap onImpressaoBitMap ;

    public ControleComandaRemota(ActivityEnvioPedidos ctx, OnControleComandaRemota onControleComandaRemota) {
        this.ctx = ctx;
        this.onControleComandaRemota = onControleComandaRemota;
        onImpressaoBitMap = new ImpressaoBitmapBuilder(ctx, this).getImpressao(ctx) ;
    }

    public void requisicao(JSONArray req){
        onControleComandaRemota.comandaChange("Verificando Requisição...", null);
        if (req.length() > 0) {
            enviarRequisicao(req) ;
        } else {
            onControleComandaRemota.comandaChange("Nenhuma Comanda Para Impressão!", null);
            onControleComandaRemota.comandaOk();
        }
    }

    private void enviarRequisicao(JSONArray req) {
        try {
            onControleComandaRemota.comandaChange("Enviando Requisição...", null);
            new ConexaoRequisicaoRemota(ctx, req) {
                @Override
                public void reqRemotaOk(List<ImpressaoRemota> l) {
                    onControleComandaRemota.comandaChange("Recebendo Requisição...", null);
                    imprimirComanda(l) ;
                }

                @Override
                public void reqRemotaErro(String msg) {
                    onControleComandaRemota.comandaErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControleComandaRemota.comandaErro(e.getMessage());
        }
    }

    private void imprimirComanda(List<ImpressaoRemota> l) {
        list = l ;
        ponteiro = 0 ;
        Log.i("imprimirComanda", "size "+list.size()) ;
        onControleComandaRemota.comandaChange("Impressão OK! "+l.size(), null);
        if (l.size() > 0)
            imprimir(list.get(ponteiro));
         else onControleComandaRemota.comandaOk();
    }

    private void imprimir(ImpressaoRemota impressaoRemota){
        if (impressaoRemota.getITENS().size() > 0) {
            Log.i("imprimirComanda", "size "+impressaoRemota.getITENS().get(0).getPRODUTO_ID()) ;
            Bitmap bitmap = new ComandaRemotaBitMap(ctx, impressaoRemota).getBitMap();
            onControleComandaRemota.comandaChange("Imprimindo Comanda [" + impressaoRemota.getPEDIDO() + "]", bitmap);
            onImpressaoBitMap.imprimirBitMap(bitmap);
        } else {
            if (++ponteiro < list.size())
                imprimir(list.get(ponteiro));
             else  onControleComandaRemota.comandaOk();
        }
    }

    @Override
    public void imprimirBitMapOk() {
        Log.i("imprimirComanda", "size "+list.size()+" ponteiro "+ponteiro) ;
        if (++ponteiro < list.size()) {
            imprimir(list.get(ponteiro));
        } else {
            Log.i("imprimirComanda", "saiu size "+list.size()+" ponteiro "+ponteiro) ;
            onControleComandaRemota.comandaOk();
        }
    }

    @Override
    public void imprimirBitMapErro(String msg) {
        onControleComandaRemota.comandaErro(msg);
    }
}
