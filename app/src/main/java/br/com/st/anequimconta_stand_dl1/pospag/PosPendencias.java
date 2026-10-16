package br.com.st.anequimconta_stand_dl1.pospag;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;

public class PosPendencias implements OnPosDesfazimento {

    private Context ctx ;
    private PosApi posApi ;
    private OnPosPagPendencias onPosPagPendencias ;
    private List<PosPagamentoView> list ;
    private int counter ;

    public PosPendencias(Context ctx, PosApi posApi, OnPosPagPendencias onPosPagPendencias) {
        this.ctx = ctx;
        this.onPosPagPendencias = onPosPagPendencias ;
        this.posApi = posApi ;
    }

    public void pendencias() {
        PosFilter f = new PosFilter() ;
        f.getTipoRecebimentoPos().add(TipoRecebimentoPos.tr_pendete);
        f.setDtaInicial(new Date());
        f.setDtaFinal(new Date());
        posApi.getList(f, new OnListPagamentos() {
            @Override
            public void OnSucess(List<PosPagamentoView> l) {
                if (l.size() > 0) {
                    list = l ;
                    executarCanelamento();
                } else onPosPagPendencias.pendenciasOk();
            }

            @Override
            public void OnErro(String msg) {
                onPosPagPendencias.erroPendencias(msg);
            }
        });
    }

    private void executarCanelamento() {
        Log.i("desfazimento", "size list "+list.size()) ;
        counter = 0 ;
        if ((counter + 1) > list.size())
            onPosPagPendencias.pendenciasOk();
        else cancelar(list.get(counter)) ;
    }

    private void cancelar(PosPagamentoView pay) {
        Log.i("desfazimento", "size pay id "+pay.getId()) ;
        new PosDesfazimentoBuilder(ctx, this).getPosdesfazimento().desfazerPayment(pay);
    }

    @Override
    public void desfazimentoOK(PosPagamentoView pay) {
        if ((counter + 1) > list.size())
            onPosPagPendencias.pendenciasOk();
        else cancelar(list.get(counter)) ;
    }

    @Override
    public void desfazimentoErro(String msg) {
        onPosPagPendencias.erroPendencias(msg);
    }
}
