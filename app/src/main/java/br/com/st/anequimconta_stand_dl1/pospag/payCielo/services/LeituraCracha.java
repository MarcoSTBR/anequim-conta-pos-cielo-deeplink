package br.com.st.anequimconta_stand_dl1.pospag.payCielo.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LeituraCracha extends BroadcastReceiver {

    // Ação customizada que você define para identificar o retorno
    private final String TAG = "RFID_CIELO" ;
    public static final String ACTION_RFID_CALLBACK = "com.seuapp.rfid.CALLBACK_DETECT";
    private Context ctx ;
    private final OnCrachaLidoListener listener;

    public LeituraCracha(Context ctx, OnCrachaLidoListener listener) {
        this.ctx = ctx ;
        this.listener = listener;
        config() ;
    }

    public void desconectar(){
        // Desregistra para evitar vazamento de memória (Memory Leak)
        ctx.unregisterReceiver(this);

        // Boa prática recomendada pela Cielo: Cancelar o modo de detecção ao sair
        desativarLeituraCieloMifare();
    }

    public void start(){
        String packageName = ctx.getPackageName(); // Pega o pacote do SEU app automaticamente
        String cbAction = LeituraCracha.ACTION_RFID_CALLBACK; // "com.seuapp.rfid.CALLBACK_DETECT"

        Intent intent = new Intent("cielo.lio.cashless.mifare.DETECT");
        intent.setPackage("cielo.lio.cashless");

        // Passa os parâmetros obrigatórios exigidos pelo ecossistema Cielo LIO/Axium
        intent.putExtra("cbType", 'B'); // 'B' para BroadcastReceiver
        intent.putExtra("cbPackage", packageName);
        intent.putExtra("cbAction", cbAction);

        // O terminal DX8000 exige que serviços de terceiros em background usem startForegroundService se o app sumir da tela
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ctx.startForegroundService(intent);
        } else {
            ctx.startService(intent);
        }

        Toast.makeText(ctx, "Aproxime o crachá no topo...", Toast.LENGTH_LONG ).show();
        // txtRfidCodigo.setText("Aproxime o crachá no topo...");

    }

    // Função opcional para encerrar a sessão de leitura quando necessário
    private void desativarLeituraCieloMifare() {
        Intent intent = new Intent("cielo.lio.cashless.mifare.DEACTIVATE");
        intent.setPackage("cielo.lio.cashless");
        ctx.startService(intent);
    }

    private void config(){
        IntentFilter filter = new IntentFilter(LeituraCracha.ACTION_RFID_CALLBACK);

        // No Android 14+, é recomendado especificar se o receiver é exportado ou não
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ctx.registerReceiver(this, filter, Context.RECEIVER_EXPORTED);
            Log.i(TAG, "rfidReceiver, filter, Context.RECEIVER_EXPORTED") ;
        } else {
            ctx.registerReceiver(this, filter);
            Log.i(TAG, "rfidReceiver, filter") ;
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        Log.d(TAG, "--- NOVA LEITURA CAPTURADA ---");

        Bundle bundle = intent.getExtras();
        String codigoDetectado = null;

        if (bundle != null) {
            // 🔥 DEBUG COMPLETO: Varre todas as chaves enviadas pela Cielo e exibe no Logcat
            for (String key : bundle.keySet()) {
                Log.d(TAG, "Chave encontrada -> [" + key + "] com o valor: " + bundle.get(key));
            }

            // Tentativas de captura por mapeamentos históricos da Cielo LIO / Axium:
            if (bundle.containsKey("output")) {
                codigoDetectado = bundle.getString("output");
            } else if (bundle.containsKey("uid")) {
                codigoDetectado = bundle.getString("uid");
            } else if (bundle.containsKey("cardId")) {
                codigoDetectado = bundle.getString("cardId");
            } else if (bundle.containsKey("id")) {
                codigoDetectado = bundle.getString("id");
            } else if (bundle.containsKey("data")) {
                // Algumas versões devolvem em array de bytes
                Object data = bundle.get("data");
                if (data instanceof byte[]) {
                    codigoDetectado = bytesToHex((byte[]) data);
                } else if (data instanceof String) {
                    codigoDetectado = (String) data;
                }
            }
        }

        // Se conseguiu isolar o código, avisa a MainActivity
        if (codigoDetectado != null && listener != null) {
            Log.i(TAG, "codigoDetectado "+codigoDetectado);
            listener.onCrachaDetectado(codigoDetectado);
        } else {
            listener.onCrachaErro("ID desconhecido!");
            Log.e(TAG, "O evento chegou, mas o ID do crachá não estava em nenhuma chave conhecida.");
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}

