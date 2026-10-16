package br.com.st.anequimconta_stand_dl1.pospag.paystone;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.activity.ActivityCancelamento;
import br.com.st.anequimconta_stand_dl1.activity.ActivityCaptura;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoPixEditPos;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoPixPos;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPosPix;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPix;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class PosPagamentoPixPayStone implements PosPagamentoPix{

    private Activity ctx ;
    private OnPosPagamentoPix onPosPagamentoPix;
    private OnPosCancelamentoPix onPosCancelamentoPix;
    private ContaPag contaPag;
    private RecebimentoPosPix recebimentoPosPix;
    private Modalidade modalidade;
    private ActivityResultLauncher<Intent> respostaPagamento ;
    private ActivityResultLauncher<Intent> respostaCancelamento ;
    private Intent intentPagamento;
    private Intent intentCancel ;

    public PosPagamentoPixPayStone(Activity ctx) {
        this.ctx = ctx;
    }


    public PosPagamentoPixPayStone(FragmentPagamento appCompatActivity) {
        this.ctx = appCompatActivity.getActivity();
        intentPagamento = new Intent(appCompatActivity.getActivity(), ActivityCaptura.class);
        intentPagamento.putExtra("TIPO", "PIX");
        respostaPagamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i("stone_", "ok "+result.getResultCode()) ;
            pago(result.getResultCode(), result.getData());
        });
        intentCancel = new Intent(appCompatActivity.getActivity(), ActivityCancelamento.class);
        respostaCancelamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i("stone_", "ok "+result.getResultCode()) ;
            cancela(result.getResultCode(), result.getData());
        }) ;
    }
/*
    public PosPagamentoPixPayStone(ActivityListPixPagamentos appCompatActivity) {
        this.ctx = appCompatActivity;
        intentPagamento = new Intent(appCompatActivity, ActivityCaptura.class);
        intentPagamento.putExtra("TIPO", "PIX");
        respostaPagamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i("stone_", "ok "+result.getResultCode()) ;
            pago(result.getResultCode(), result.getData());
        });
        intentCancel = new Intent(appCompatActivity, ActivityCancelamento.class);
        respostaCancelamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i("stone_", "ok "+result.getResultCode()) ;
            cancela(result.getResultCode(), result.getData());
        }) ;
    }
*/

    @Override
    public void pagamento(ContaPag pag, OnPosPagamentoPix onPosPagamentoPix) {
        this.contaPag = pag;
        this.onPosPagamentoPix = onPosPagamentoPix;
        String uuid = UtilSet.getUUID();
        setModalidade();
        /* aqui */
        recebimentoPosPix = new RecebimentoPosPix(uuid, new Date(), uuid, contaPag.getUUID(), 1, "");
        startPagamento();
    }

    private void insertRec(RecebimentoPosPix rc){
        try {
            new ConexaoRecebimentoPixEditPos(ctx, rc) {
                @Override
                public void recebimentoEditPixPosOk(RecebimentoPosPix r) {
                    onPosPagamentoPix.onPixSucess(contaPag);
                }

                @Override
                public void recebimentoEditPixPosErro(String msg) {
                    onPosPagamentoPix.onPixError(msg) ;
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onPosPagamentoPix.onPixError(e.getMessage()) ;
        } catch (JSONException e) {
            e.printStackTrace();
            onPosPagamentoPix.onPixError(e.getMessage()) ;
        }

    }

    private void pago(int resultCode, Intent intent) {

        if (intent != null)  {
            String resp = intent.getStringExtra("resp");
            String mensagem = intent.getStringExtra("mensagem");
            String status = intent.getStringExtra("status");
            Log.i("stone_", "ok data " + resp);
            Log.i("stone_", "ok status " + status);

            try {
                if (resultCode == Activity.RESULT_OK) {

                    if (status.equals("success")) {
                        JSONObject j = new JSONObject(resp);
                        recebimentoPosPix.setPAGAMENTO_UUID(j.getString("atk"));
                        recebimentoPosPix.setSTATUS(0);
                        recebimentoPosPix.setRECIBO(resp);
                        insertRec(recebimentoPosPix) ;
                    } else onPosPagamentoPix.onPixError(mensagem);
                } else {
                    onPosPagamentoPix.onPixError(mensagem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                onPosPagamentoPix.onPixError(e.getMessage());
            }

        } else onPosPagamentoPix.onPixError("Sem Retorno");
    }


    private void cancela(int resultCode, Intent intent) {
        if (intent != null)  {
            String resp = intent.getStringExtra("resp");
            String mensagem = intent.getStringExtra("mensagem");
            String status = intent.getStringExtra("status");
            try {
                if (resultCode == Activity.RESULT_OK) {
                    if (status.equals("success")) {
                        JSONObject j = new JSONObject(resp);
                        recebimentoPosPix.setSTATUS(0);
                        inserirCancelamento(recebimentoPosPix) ;
                    } else onPosCancelamentoPix.onCancelamentoPixError(mensagem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                onPosCancelamentoPix.onCancelamentoPixError(e.getMessage());
            }
        } else onPosCancelamentoPix.onCancelamentoPixError("Sem Retorno");
    }

    private void inserirCancelamento(RecebimentoPosPix rc){
        try {
            new ConexaoRecebimentoPixEditPos(ctx, rc) {
                @Override
                public void recebimentoEditPixPosOk(RecebimentoPosPix r) {
                    onPosCancelamentoPix.onCancelamentoPixSucess(contaPag);
                }

                @Override
                public void recebimentoEditPixPosErro(String msg) {
                    onPosCancelamentoPix.onCancelamentoPixError(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onPosCancelamentoPix.onCancelamentoPixError(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onPosCancelamentoPix.onCancelamentoPixError(e.getMessage());
        }
    }


    @Override
    public void cancelamento(ContaPag pag, OnPosCancelamentoPix onPosCancelamentoPix) {
        this.contaPag = pag;
        this.onPosCancelamentoPix = onPosCancelamentoPix;
        this.contaPag = pag;
        setModalidade();
        /* aqui */
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("CONTA_PAG_UUID", "=", pag.getUUID()));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoRecebimentoPixPos(ctx, f, "") {
                @Override
                public void recebimentoPixPosOk(List<RecebimentoPosPix> l) {
                    if (l.size() > 0) {
                        recebimentoPosPix = l.get(0) ;
                        starCancelamento();
                    } else onPosCancelamentoPix.onCancelamentoPixError("Recebimento não encontrado!");

                }

                @Override
                public void recebimentoPixPosErro(String msg) {
                    onPosCancelamentoPix.onCancelamentoPixError(msg);

                }

            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onPosCancelamentoPix.onCancelamentoPixError(e.getMessage());
        }
    }

    private void setModalidade() {
        modalidade = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(contaPag.getMODALIDADE_ID()));
    }

    private void starCancelamento() {
        try {
            intentCancel.putExtra("DATA", recebimentoPosPix.geJSON().toString());
            respostaCancelamento.launch(intentPagamento);
        } catch (JSONException e) {
            e.printStackTrace();
            onPosCancelamentoPix.onCancelamentoPixError(e.getMessage());
        }
    }

    private void startPagamento() {
        try {
            intentPagamento.putExtra("DATA", contaPag.geJSON().toString());
            respostaPagamento.launch(intentPagamento);
        } catch (JSONException e) {
            e.printStackTrace();
            onPosCancelamentoPix.onCancelamentoPixError(e.getMessage());
        }
    }


}
