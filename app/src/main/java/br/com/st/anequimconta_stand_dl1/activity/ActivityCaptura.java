package br.com.st.anequimconta_stand_dl1.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cielo.ordermanager.sdk.utils.Item;
import com.cielo.ordermanager.sdk.utils.OrderRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityCapturaBinding;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.pospag.payCielo.utils.UtilisJava;
import br.com.st.anequimconta_stand_dl1.pospag.paystone.OnSelectOpcaoStone;
import br.com.st.anequimconta_stand_dl1.pospag.paystone.OpcaoPagamentoStone;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;
import br.com.st.anequimconta_stand_dl1.BuildConfig;



public class ActivityCaptura extends AppCompatActivity implements OnSelectOpcaoStone {
    
    private final String TAG1 = "captura_" ;
    private final String TAG2 = "cielo_" ;
    private ActivityCapturaBinding binding ;
    private ContaPag contaPag;
    private Modalidade modalidade ;
    private String data ;
    private boolean flag ;
    private List<OpcaoPagamentoStone> list ;
    private OpcaoPagamentoStone opCorrente ;
    private int numeroId ;


    private String scheme ;
    private String responseHost ;
    private String callbackUrl ;
    private String callbackUrlSameActivity ;
    private String scheme_same_activity ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCapturaBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        data =  getIntent().getStringExtra("DATA") ;
        if (data != null)
           Log.i(TAG1, data) ;
         else Log.i(TAG1, "data nulo") ;

        binding.tentarNovamente.setText("Tentar Novamente");
        binding.tentarNovamente.setVisibility(View.GONE);
        binding.tentarNovamente.setOnClickListener(v -> executar());
        binding.sair.setOnClickListener(v -> sair());
        flag = false ;
        list = new ArrayList<OpcaoPagamentoStone>() ;

        scheme  =  getString(R.string.intent_scheme) ;
        responseHost = getString(R.string.intent_host) ;
        callbackUrl  =  scheme+"://"+responseHost ;
        scheme_same_activity = getString(R.string.intent_scheme_same_activity) ;
        callbackUrlSameActivity =  scheme_same_activity+"://"+getString(R.string.intent_host) ;
        //  setRecebimento();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if ((!flag) && (data != null)) {
            flag = true ;
            setRecebimento();
        }
    }


    private void setRecebimento() {
        try {
            contaPag = new ContaPag(new JSONObject(data));
            String valor =  new DecimalFormat("R$ #0.00").format(contaPag.getVALOR()) ;
            modalidade  = ModalidadeDao.getModalidadeDao(this).getModalidade(String.valueOf(contaPag.getMODALIDADE_ID())) ;
            Log.i(TAG1, "modalidade "+modalidade.getDescricao() + " " + modalidade.getTipoModalidade().valor) ;
            String modal = modalidade.getDescricao() ;
            binding.textDisplay.setText(modal+"\n"+valor+"\n ");
            Log.i(TAG2, modalidade.getTipoModalidade().valor) ;
            executar() ;
        } catch (JSONException e) {
            binding.textDisplay.setText(e.getMessage());
            saiOK(e.getMessage()) ;
        }
    }

    private String getReference() {
        return "uriapp #" + (System.currentTimeMillis() / 1000);
    }


/*
    private void getOrderID() {
        numeroId = UtilSet.getProximoNumero(this) ;
    }
*/

    private void executar(){
        Long sku =  ThreadLocalRandom.current().nextLong(500L, 1000L);
        numeroId = sku.intValue() ;
        String name = contaPag.getUUID().substring(1,5) ;
        Long amount = Long.parseLong(String.valueOf(Integer.valueOf(new DecimalFormat("#0.00").format(contaPag.getVALOR())
                .replace(".","")
                .replace(",",""))));
        Item item = new Item(sku.toString(), name, amount, 1, "unidade") ;
        List<Item> itens = new ArrayList();
        itens.add(item) ;
        String clientId = BuildConfig.CREDENTIALS_CLIENT_ID;
        String accessToken = BuildConfig.CREDENTIALS_ACCESS_TOKEN;
        OrderRequest ord = new OrderRequest(clientId,
                accessToken,
                amount,
                "DEBITO_AVISTA",
                1,
                "",
                null,
                getReference(),
                itens,
                null) ;


        String json = new Gson().toJson(ord).toString() ;
        String base64 = UtilisJava.getBase64(json) ;
//        String checkoutUri = "lio://payment?request="+base64+"&urlCallback="+callbackUrl ;
        String checkoutUri = "lio://payment?request="+base64+"&urlCallback="+callbackUrlSameActivity ;

        Log.i(TAG2, "start") ;
        Log.i(TAG2, "callbackUrl "+callbackUrl) ;
        Log.i(TAG2, "scheme_same_activity "+scheme_same_activity) ;
        Log.i(TAG2, "callbackUrlSameActivity "+callbackUrlSameActivity) ;
        UtilisJava.startForegroundServiceAndLaunchDeepLink(this, checkoutUri) ;
    }

    private String getPayment(){
        String ret = "" ;

        return ret ;
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        UtilisJava.callToStopService(this);
        Log.i(TAG2, "onNewIntent") ;
        if (intent != null) {
            Log.i(TAG2, "intent "+intent.getData().toString()) ;
            UtilisJava.deserializeQueryParameter(intent, "response", json -> {
                try {
                  Log.i(TAG2, "Intent recebida: " + json) ;
                  JSONObject j = new JSONObject(json) ;
                  if (j.isNull("code")) {
                      sairPago(j) ;
                  } else {
                      if (!j.isNull("reason"))
                          sairEx(j.getString("reason")) ;
                       else sairEx(j.toString()) ;
                  }
//                  Toast.makeText(this, "Intent recebida: " + json, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sairEx(e.getMessage()) ;
                }

            });
        } else {
            Log.i(TAG2, "Intent nulo!");

        }
    }

    private void sairPago(JSONObject j){
        Intent intt =  new Intent() ;
        intt.putExtra("numero_id", numeroId) ;
        intt.putExtra("status", "success");
        intt.putExtra("resp", j.toString());
        intt.putExtra("mensagem", "Ok");
        setResult(RESULT_OK, intt);
        finish();
    }

    private void sairEx(String msg) {
        Intent intt =  new Intent() ;
        intt.putExtra("numero_id", numeroId) ;
        intt.putExtra("status", "erro");
        intt.putExtra("mensagem", msg);
        setResult(RESULT_CANCELED, intt);
        finish();
    }

    private void saiOK(String msg){
        Log.i(TAG2, "finalizou com aviso "+msg) ;
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> finish())
                .show();
    }


    private void sair() {
        Intent intt =  new Intent() ;
        intt.putExtra("numero_id", numeroId) ;
        intt.putExtra("status", "erro");
        intt.putExtra("mensagem", "Operação Cancelada");
        setResult(RESULT_CANCELED, intt);
        finish();
    }


    @Override
    public void selectionado(OpcaoPagamentoStone op) {
        opCorrente =  op ;
        executar();
    }
}