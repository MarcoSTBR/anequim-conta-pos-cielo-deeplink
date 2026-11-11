package br.com.st.anequimconta_stand_dl1.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cielo.ordermanager.sdk.orders.OrderManagerController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityCancelamentoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPos;
import br.com.st.anequimconta_stand_dl1.pospag.payCielo.orders.OrderManagerControllerJava;
import cielo.orders.domain.CancellationRequest;
import cielo.sdk.order.cancellation.CancellationListener;
import cielo.sdk.order.payment.PaymentError;

public class ActivityCancelamento extends AppCompatActivity implements CancellationListener{


    private static final String TAG = "CANCEL_PAYMENT";
    private OrderManagerController orderManagerController;
    private ActivityCancelamentoBinding binding ;
    private RecebimentoPos recebimentoPos ;
    private boolean iniciar = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCancelamentoBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        String data = getIntent().getStringExtra("DATA") ;
        Log.i(TAG, "data " + data.toString()) ;

        binding.sair.setOnClickListener(v -> sairSemCancelar());
        binding.tentarNovamente.setOnClickListener(v-> executar());
        binding.tentarNovamente.setText("Cancelar");
        // FIX PARA DX8000 DEBUG - Inicializa e espera o service da Cielo
        orderManagerController = OrderManagerController.Companion.getInstance(this);

        setRecebimento(data);
/*
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setRecebimento(data);
        }, 1500);
*/
    }

    private void sairSemCancelar() {
        Intent itt = new Intent();
        itt.putExtra("status", "erro") ;
        itt.putExtra("mensagem", "Sem Cancelamento") ;
        setResult(RESULT_CANCELED, itt);
    }


    private void setRecebimento(String data) {
        try {
            recebimentoPos = new RecebimentoPos(new JSONObject(data)) ;
            String d = recebimentoPos.getRecibo();
            Log.i(TAG, "d "+d);
            JSONObject j = new JSONObject(d) ;
            Log.i(TAG, "recibo");
            Log.i(TAG, j.toString());
            JSONObject jj = j.getJSONArray("payments").getJSONObject(0) ;
            String id = j.getString("id") ;
            Long amount = jj. getLong("amount") ;
            String authCode = jj.getString("authCode") ;
            String cieloCode = jj.getString("cieloCode") ;
            String merchantCode = jj.getString("merchantCode") ;
            Double i = amount / 100.0 ;
            Log.i(TAG, "id " + id) ;
            Log.i(TAG, "amount " + amount) ;
            Log.i(TAG, "authCode " + authCode) ;
            binding.textAtk.setText("authCode "+authCode);
            binding.textAmount.setText(new DecimalFormat("R$ #0.00").format(i));

            CancellationRequest request = new CancellationRequest.Builder()
                    .orderId(id)
                    .authCode(authCode)
                    .cieloCode(cieloCode)
                    .value(amount)
                    .ec(merchantCode)
                    .build();
            OrderManagerControllerJava orderManagerController = OrderManagerControllerJava.getInstance(this);

            orderManagerController.cancelOrder(request, this) ;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "JSONException "+e.getMessage()) ;
            setResult(RESULT_CANCELED);
            alertSair("Erro: "+e.getMessage()) ;
        }
    }

    private void executar(){
        binding.tentarNovamente.setText("Tentar Novamente");
    }

    private void sairOk(JSONObject j){
        Log.i(TAG, "saiu direto "+j.toString()) ;
        finish();
    }

    private void alertSair(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção:")
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("Ok", (dialogInterface, i) -> finish())
                .show();
    }

    @Override
    public void onCancel() {
        Intent i = new Intent() ;
        i.putExtra("status", "erro") ;
        i.putExtra("mensagem", "Operação Cancelada!") ;
        setResult(RESULT_CANCELED, i);
        Log.i(TAG, "onCancel ") ;
        finish();
    }

    @Override
    public void onSuccess(@NonNull cielo.orders.domain.Order order) {
        Intent i = new Intent() ;
        Gson g = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create() ;

        Log.i(TAG, "onSuccess "+ g.toJson(order)) ;
        i.putExtra("resp",  g.toJson(order));
        i.putExtra("status", "success");
        i.putExtra("mensagem", "Cancelamento OK!") ;
        setResult(RESULT_OK, i);
        Log.i(TAG, "onSuccess "+order.toString()) ;
        finish();
    }

    @Override
    public void onError(@NonNull PaymentError paymentError) {
        Intent i = new Intent() ;
        i.putExtra("status", "erro") ;
        i.putExtra("mensagem", paymentError.getDescription()) ;
        setResult(RESULT_CANCELED, i);
        Log.i(TAG, "onError "+paymentError.getDescription()) ;
        finish();
    }
}