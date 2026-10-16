package br.com.st.anequimconta_stand_dl1.impressao.stone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.activity.ActivityReimprimir;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoPos;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPos;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControlePosReimpressao;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;
import br.com.st.anequimconta_stand_dl1.pospag.PosReimpressao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;

public class PosReimpressaoPayStone implements PosReimpressao {

    private Activity ctx ;
    private PosPagamentoView posPagamentoView ;
    private OnControlePosReimpressao onControlePosReimpressao ;
    private ActivityResultLauncher<Intent> respostaImpressao ;

    public PosReimpressaoPayStone(FragmentListCartao fragmentListCartao) {
        ctx = fragmentListCartao.getActivity() ;
        respostaImpressao = fragmentListCartao.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.i("stone_", "ok "+result.getResultCode() ) ;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Log.i("stone_", "ok") ;
                        }
                    }
                }
        );
    }

    @Override
    public void reimpressao(PosPagamentoView posPagamentoView, OnControlePosReimpressao onControlePosReimpressao) {
        this.posPagamentoView = posPagamentoView;
        this.onControlePosReimpressao = onControlePosReimpressao;
        try {
            String id = posPagamentoView.getId() ;
            DaoFilters f = new DaoFilters() ;
            f.add(new DaoFilter("CONTA_PAG_UUID", "=", id));
            f.add(new DaoFilter("STATUS", "=", "1"));
            new ConexaoRecebimentoPos(ctx, f, "") {
                @Override
                public void recebimentoPosOk(List<RecebimentoPos> l) {
                    try {
                        if (l.size() > 0){
                            RecebimentoPos recebimentoPos = l.get(0) ;
                            Intent i = new Intent(ctx, ActivityReimprimir.class) ;
                            JSONObject j = new JSONObject(recebimentoPos.getRecibo()) ;
                            i.putExtra("DATA", j.getString("atk")) ;
                            respostaImpressao.launch(i);
                        } else onControlePosReimpressao.impressaoErro("Recebimento Não Encontrado! "+id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onControlePosReimpressao.impressaoErro(e.getMessage());
                    }
                }

                @Override
                public void recebimentoPosErro(String msg) {

                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onControlePosReimpressao.impressaoErro(e.getMessage());
        }
    }


}
