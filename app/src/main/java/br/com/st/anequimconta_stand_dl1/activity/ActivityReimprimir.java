package br.com.st.anequimconta_stand_dl1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;

import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.orders.domain.ResultOrders; // ESSE AQUI, NÃO O DE SDK
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.printer.PrinterManager;


import br.com.st.anequimconta_stand_dl1.BuildConfig;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityReimprimirBinding;

public class ActivityReimprimir extends AppCompatActivity {

    private final String TAG = "reimpressao" ;
    private ActivityReimprimirBinding binding;
    private OrderManager orderManager;
    private PrinterManager printerManager;
    private String orderId;
    boolean isBound ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReimprimirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i(TAG, "Reimprimindo pedido: ");

        orderId = getIntent().getStringExtra("DATA");
        Log.i(TAG, "Order id "+orderId);
        binding.mensagem.setText("Order id "+orderId);
        Credentials Credential = new Credentials(BuildConfig.CREDENTIALS_CLIENT_ID, BuildConfig.CREDENTIALS_ACCESS_TOKEN);
        orderManager = new OrderManager(Credential, this);
        printerManager = new PrinterManager(this);
        orderManager.bind(this, new ServiceBindListener() {
            @Override
            public void onServiceBoundError(@NonNull Throwable throwable) {
                Log.i(TAG, "LIO Desconectada "+throwable.getMessage());
            }

            @Override
            public void onServiceBound() {
                isBound = true;
                Log.i(TAG, "LIO Conectada!");
                runOnUiThread(() -> {
                    binding.mensagem.setText("Conectado, buscando...");
                    procuraPorId();
                });
            }
            @Override
            public void onServiceUnbound() {
                isBound = false;
                Log.i(TAG, "LIO Desconectada");
            }
        });

        binding.sair.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("status", "erro");
            intent.putExtra("mensagem", binding.mensagem.getText().toString());
            setResult(RESULT_CANCELED, intent);
            finish();
        });
        binding.tentarNovamente.setOnClickListener(v -> {
            procuraPorId();
        });
    }

    private void procuraPorId() {
        binding.mensagem.setText("Buscando...");
        new Thread(() -> {
            try {
                ResultOrders result = orderManager.retrieveOrders(20, 0);
                Log.i(TAG, "Result classe: " + result.getClass().getName());
                Log.i(TAG, "Results null? " + (result.getResults() == null));
                if (result.getResults() == null) {
                    runOnUiThread(() -> sairErro("Comprovante Não Encontrado!"));
                    return;
                }
                Log.i(TAG, "size: " + result.getResults().size());
                for (Order o : result.getResults()) {
                    Log.i(TAG, "ID: " + o.getId() + " equals? " + o.getId().equals(orderId));
                    if (o.getId().equals(orderId)) {
                        runOnUiThread(() -> reimprimirOrder(o));
                        return;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro", e);
                sairErro("Erro "+e.getMessage());
            }
        }).start();
    }

    private void reimprimirOrder(Order order) {
        sairOk("Esta função não está ativa neste Terminal!") ;
    }

 /*
    private void reimprimirOrder(Order order) {
        Log.i(TAG, "Reimprimindo Order " + order.toString());
        Log.i(TAG, "Enviando Order para Cielo imprimir: " + order.getId());
        binding.mensagem.setText("Reimprimindo...");
        Log.i(TAG, "Reimpressao oficial: " + order.getId());
        String transactionId = "";
        String authCode = "";
        String mask = "";
        String cieloCode = "";
        String amount = "";
        String date = "";
        String product = "";
        String terminal = "";
        try {
            if (order.getPayments() != null && !order.getPayments().isEmpty()) {
                cielo.sdk.order.payment.Payment payment = (cielo.sdk.order.payment.Payment) order.getPayments().get(0);
                // CAMPOS OFICIAIS DO SEU SDK - AGORA COM NOME CERTO
                transactionId = payment.getExternalId(); // f8b701bc-cf7d-40ef-ab44-7e9a736bdcd3 - OFICIAL
                authCode = payment.getAuthCode(); // 160145
                mask = payment.getMask(); // ************9852
                cieloCode = payment.getCieloCode(); // 908538
                terminal = payment.getTerminal(); // 93000582
                amount = String.valueOf(payment.getAmount()); // 1376
                // paymentFields é um HashMap na sua versao
                java.util.HashMap fields = (java.util.HashMap) payment.getPaymentFields();
                if (fields != null) {
                    if (fields.get("productName") != null) product = fields.get("productName").toString();
                    if (fields.get("originalTransactionDate") != null) date = fields.get("originalTransactionDate").toString();
                    if (fields.get("paymentTransactionId") != null) transactionId = fields.get("paymentTransactionId").toString();
                }
                Log.i(TAG, "Dados oficiais: tx=" + transactionId + " auth=" + authCode + " mask=" + mask);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro", e);
        }
        // MONTA COMPROVANTE OFICIAL IGUAL AO DA CIELO
        StringBuilder sb = new StringBuilder();
        sb.append("        CIELO\n");
        sb.append("  COMPROVANTE DE VENDA\n");
        sb.append("--------------------------------\n");
        sb.append("DATA: ").append(date).append("\n");
        sb.append("TERMINAL: ").append(terminal).append("\n");
        sb.append("ESTAB: 0010000234570003\n");
        sb.append("--------------------------------\n");
        sb.append(product).append("\n"); // DEBITO_AVISTA
        sb.append("CARTAO: ").append(mask).append("\n");
        sb.append("VALOR: R$ ").append(String.format("%.2f", Integer.parseInt(amount)/100.0)).append("\n");
        sb.append("--------------------------------\n");
        sb.append("AUTORIZACAO: ").append(authCode).append("\n");
        sb.append("COD CIELO: ").append(cieloCode).append("\n");
        sb.append("ID: ").append(transactionId.substring(0, 8)).append("\n");
        sb.append("--------------------------------\n");
        sb.append("   TRANSACAO APROVADA\n");
        sb.append("\n\n\n");
        String reciboOficial = sb.toString();
        binding.mensagem.setText(reciboOficial);
        // IMPRIME OFICIAL COM A IMPRESSORA DA DX8000
        printerManager.printText(reciboOficial, new java.util.HashMap<String, Integer>(), new cielo.sdk.order.PrinterListener() {
            @Override
            public void onPrintSuccess() {
                Log.i(TAG, "Impressao oficial OK");
                runOnUiThread(() -> sairOk());
            }
            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "Erro impressao", throwable);
                runOnUiThread(() -> sairErro(throwable.getMessage()));
            }
            @Override
            public void onWithoutPaper() {
                runOnUiThread(() -> sairErro("Sem papel"));
            }
        });
    }
*/

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent") ;
    }

    private void sairOk(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Erro")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", ((dialogInterface, i) -> {
                    sairErro(msg) ;
                }))
                .show();
    }

    private void sairErro(String msg){
        Intent intt = new Intent();
        intt.putExtra("status", "erro");
        intt.putExtra("mensagem", msg);
        setResult(RESULT_CANCELED, intt);
        finish();
    }



}