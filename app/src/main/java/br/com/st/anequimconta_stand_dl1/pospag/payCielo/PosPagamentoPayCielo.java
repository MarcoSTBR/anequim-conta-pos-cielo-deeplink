package br.com.st.anequimconta_stand_dl1.pospag.payCielo;

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
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoEditPos;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoPos;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPos;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosCancelamento;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagamento;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamento;
import br.com.st.anequimconta_stand_dl1.pospag.TipoRecebimentoPos;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListCartao;
import br.com.st.anequimconta_stand_dl1.ui.FragmentListPix;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class PosPagamentoPayCielo implements PosPagamento {

    private final String TAG = "cielo_"  ;

    private Activity ctx  ;
    private ContaPag contaPag;
    private RecebimentoPos recebimentoPos;
    private OnPosPagamento onPosPagamento;
    private OnPosCancelamento onPosCancelamento;
//    private Caixa caixa;
    private Modalidade modalidade;
    private ActivityResultLauncher<Intent> respostaPagamento ;
    private ActivityResultLauncher<Intent> respostaCancelamento ;
    private Intent intentPagamento ;
    private Intent intentCancel ;


    public PosPagamentoPayCielo(FragmentListCartao appCompatActivity) {
        this.ctx = appCompatActivity.getActivity();
        intentPagamento = new Intent(appCompatActivity.getActivity(), ActivityCaptura.class);
        respostaPagamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i(TAG, "ok "+result.getResultCode()) ;
            pago(result.getResultCode(), result.getData());
        });
        intentCancel = new Intent(appCompatActivity.getActivity(), ActivityCancelamento.class);
        respostaCancelamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i(TAG, "ok "+result.getResultCode()) ;
            cancela(result.getResultCode(), result.getData());
        }) ;
    }


    public PosPagamentoPayCielo(FragmentListPix appCompatActivity) {
        this.ctx = appCompatActivity.getActivity();
        intentPagamento = new Intent(appCompatActivity.getActivity(), ActivityCaptura.class);
        respostaPagamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i(TAG, "ok "+result.getResultCode()) ;
            pago(result.getResultCode(), result.getData());
        });
        intentCancel = new Intent(appCompatActivity.getActivity(), ActivityCancelamento.class);
        respostaCancelamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i(TAG, "ok "+result.getResultCode()) ;
            cancela(result.getResultCode(), result.getData());
        }) ;
    }

    public PosPagamentoPayCielo(FragmentPagamento appCompatActivity) {
        this.ctx = appCompatActivity.getActivity();
        intentPagamento = new Intent(appCompatActivity.getActivity(), ActivityCaptura.class);
        respostaPagamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Log.i(TAG, "ok "+result.getResultCode()) ;
            pago(result.getResultCode(), result.getData());
        });
        intentCancel = new Intent(appCompatActivity.getActivity(), ActivityCancelamento.class);
        respostaCancelamento = appCompatActivity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
             Log.i(TAG, "ok "+result.getResultCode()) ;
             cancela(result.getResultCode(), result.getData());
        }) ;
    }


    @Override
    public void pagamento(ContaPag pag, OnPosPagamento onPosPagamento) {
        this.contaPag = pag;
        this.onPosPagamento = onPosPagamento;
        setModalidade();
        /* aqui */
        int id = UtilSet.getProximoNumero(ctx);
        recebimentoPos = new RecebimentoPos(0, UtilSet.getUUID(), id, String.valueOf(id), new Date(), pag.getUUID(), TipoRecebimentoPos.tr_pendete, 1, "", "");
        startPagamento();
    }

    private void pago(int resultCode, Intent intent){
        if (intent != null)  {
            String resp = intent.getStringExtra("resp");
            String mensagem = intent.getStringExtra("mensagem");
            String status = intent.getStringExtra("status");
            Log.i(TAG, "ok data " + resp);
            Log.i(TAG, "ok status " + status);
            Log.i(TAG, "ok mensagem " + mensagem);

            try {
               if (resultCode == Activity.RESULT_OK) {

                   if (status.equals("success")) {
                       Log.i(TAG, "success resp " + resp);
                       JSONObject j = new JSONObject(resp);
                       Log.i(TAG, "success resp OK " + resp);
                      // recebimentoPos.setPagamento_uuid(j.getJSONArray("payments").getJSONObject(0).getString("authCode"));
                       recebimentoPos.setPagamento_uuid(j.getString("id"));
                       recebimentoPos.setStatus_rec(TipoRecebimentoPos.tr_confirmado);
                       recebimentoPos.setRecibo(j.toString());
                       insertRec(recebimentoPos) ;
                   } else onPosPagamento.onError(mensagem);
               } else {
                   onPosPagamento.onError(mensagem);
               }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "pago JSONException " + e.getMessage());
                onPosPagamento.onError(e.getMessage());
            }

        } else onPosPagamento.onError("Sem Retorno");

    }

    private void insertRec(RecebimentoPos rc){
        try {
            Log.i(TAG, "RecebimentoPos rc ");

            new ConexaoRecebimentoEditPos(ctx, rc) {
                @Override
                public void recebimentoEditPosOk(RecebimentoPos r) {
                    //RecebimentoPosDao.getRecebimentoPosDao(ctx).insert(recebimentoPos.geJSON());
                    onPosPagamento.onSucess(contaPag);
                }

                @Override
                public void recebimentoEditPosErro(String msg) {
                    onPosPagamento.onError(msg) ;
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "insertRec MalformedURLException " + e.getMessage());
            onPosPagamento.onError(e.getMessage()) ;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "insertRec JSONException " + e.getMessage());
            onPosPagamento.onError(e.getMessage()) ;
        }

    }

    private void cancela(int resultCode, Intent intent) {
        if (intent != null)  {
            String resp = intent.getStringExtra("resp");
            String mensagem = intent.getStringExtra("mensagem");
            String status = intent.getStringExtra("status");
            // Log.e(TAG, "intent " + intent.toString()) ;
            Log.e(TAG, "resp " + resp) ;
            Log.e(TAG, "mensagem " + mensagem) ;
            Log.e(TAG, "status "+ status) ;
            try {
                if (resultCode == Activity.RESULT_OK) {
                    if (status.equals("success")) {
                        JSONObject j = new JSONObject(resp);
                        //recebimentoPos.setStatus_rec(TipoRecebimentoPos.tr_canelado);
                        recebimentoPos.setStatus(0);
                        editaRec(recebimentoPos, j) ;
//                        RecebimentoPosDao.getRecebimentoPosDao(ctx).update(recebimentoPos.geJSON());
                    } else onPosCancelamento.onCancelamentoErro(mensagem);
                }
            } catch (JSONException e) {
                 e.printStackTrace();
                 onPosCancelamento.onCancelamentoErro(e.getMessage());
            }
        } else onPosCancelamento.onCancelamentoErro("Sem Retorno");
    }

    private void editaRec(RecebimentoPos rc, JSONObject j){
        try {
            String atk =  j.getString("atk") ;
            String json = j.toString() ;
            new ConexaoRecebimentoEditPos(ctx, rc) {
                @Override
                public void recebimentoEditPosOk(RecebimentoPos r) {
                    recebimentoPos =  new RecebimentoPos(0, UtilSet.getUUID(), UtilSet.getProximoNumero(ctx), atk, new Date(), contaPag.getUUID(), TipoRecebimentoPos.tr_canelado, 1, atk, json);
                    inserirCancelamento(recebimentoPos);
                }

                @Override
                public void recebimentoEditPosErro(String msg) {
                    onPosCancelamento.onCancelamentoErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onPosCancelamento.onCancelamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onPosCancelamento.onCancelamentoErro(e.getMessage());
        }

    }

    private void inserirCancelamento(RecebimentoPos rc){
        try {
            new ConexaoRecebimentoEditPos(ctx, rc) {
                @Override
                public void recebimentoEditPosOk(RecebimentoPos r) {
                    onPosCancelamento.onCancelamentoSucess(contaPag);
                }

                @Override
                public void recebimentoEditPosErro(String msg) {
                    onPosCancelamento.onCancelamentoErro(msg);

                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onPosCancelamento.onCancelamentoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            onPosCancelamento.onCancelamentoErro(e.getMessage());
        }
    }


    @Override
    public void cancelamento(ContaPag pag, OnPosCancelamento onPosCancelamento) {
        this.contaPag = pag;
        this.onPosCancelamento = onPosCancelamento;
        setModalidade();
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("CONTA_PAG_UUID", "=", pag.getUUID()));
        f.add(new DaoFilter("STATUS", "=", "1"));
        try {
            new ConexaoRecebimentoPos(ctx, f, "") {
                @Override
                public void recebimentoPosOk(List<RecebimentoPos> l) {
                    if (l.size() > 0) {
                        recebimentoPos = l.get(0) ;
                       starCancelamento();
                    } else onPosCancelamento.onCancelamentoErro("Recebimento não encontrado!");
                }

                @Override
                public void recebimentoPosErro(String msg) {
                    onPosCancelamento.onCancelamentoErro(msg);
                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onPosCancelamento.onCancelamentoErro(e.getMessage());
        }
    }

    private void setModalidade() {
        modalidade = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(contaPag.getMODALIDADE_ID()));
    }

    private void starCancelamento() {
        try {
            intentCancel.putExtra("DATA", recebimentoPos.geJSON().toString());
            respostaCancelamento.launch(intentCancel);
        } catch (JSONException e) {
            e.printStackTrace();
            onPosCancelamento.onCancelamentoErro(e.getMessage());
        }

    }

    private void startPagamento() {
        try {
            Log.i(TAG,"start "+contaPag.geJSONData().toString());
            intentPagamento.putExtra("DATA", contaPag.geJSONData().toString());
            respostaPagamento.launch(intentPagamento);
        } catch (JSONException e) {
            e.printStackTrace();
            onPosPagamento.onError(e.getMessage());
        }
    }

}
