package br.com.st.anequimconta_stand_dl1.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cielo.sdk.order.PrinterListener;
import cielo.sdk.printer.PrinterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.st.anequimconta_stand_dl1.databinding.ActivityImpressaoBinding;

public class ActivityImpressao extends AppCompatActivity {

    private final String TAG = "impressao_" ;
    private ActivityImpressaoBinding binding ;
    private String data ;
    private boolean hasPrinted = false;
    private PrinterManager printerManager;
    private Bitmap bitmapToPrint;

    private Map<String, Integer> alignCenter = new HashMap<String, Integer>() {{
        put("ALIGN", 1); // 0 = LEFT, 1 = CENTER, 2 = RIGHT
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImpressaoBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        data = getIntent().getStringExtra("DATA");
        binding.sair.setOnClickListener(v -> finishWithResult("erro", "Interrupção de Usuário!", RESULT_CANCELED));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPrinted) {
            hasPrinted = true ;
            imprimirComConfirmacao();
        }
    }

    private void imprimirComConfirmacao() {
        bitmapToPrint = transformarDataEmBitmap();
        if (bitmapToPrint != null) {
            // Registra o listener com retorno
            ArrayList<Map<String, Integer>> styles = new ArrayList<>();
            styles.add(alignCenter);
            printerManager = new PrinterManager(this) ;
            printerManager.printImage(bitmapToPrint, alignCenter, new PrinterListener() {
                @Override
                public void onPrintSuccess() {
                    Log.i(TAG, "onPrintSuccess - CONFIRMADO");
                    runOnUiThread(() -> {
                        finishWithResult("success", "Impressão OK", RESULT_OK) ;
                    });

                }

                @Override
                public void onError(@Nullable Throwable throwable) {
                    Log.e(TAG, "onPrintError: " + throwable.getMessage());
                    String s = throwable.getMessage();
                    runOnUiThread(() -> {
                        finishWithResult("erro", "Impressão OK", RESULT_CANCELED) ;
                    });
                }

                @Override
                public void onWithoutPaper() {
                    finishWithResult("erro", "Impressora sem papel!", RESULT_CANCELED) ;
                }
            });
            Log.i(TAG, "printImage chamado, aguardando callback...");
        } else  finishWithResult("erro", "Erro ao imprimir!", RESULT_CANCELED) ;
    }

    private Bitmap transformarDataEmBitmap() {
        try {
            String cleanBase64 = data;
            if (data.contains(",")) cleanBase64 = data.split(",")[1];
            byte[] decoded = Base64.decode(cleanBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } catch (Exception e) {
            return null;
        }
    }

    private void finishWithResult(String status, String msg, int result){
        if (bitmapToPrint != null && !bitmapToPrint.isRecycled()) {
            bitmapToPrint.recycle();
            bitmapToPrint = null;
        }
        Intent intent = new Intent();
        intent.putExtra("status", status);
        intent.putExtra("mensagem", msg);
        setResult(result, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapToPrint != null && !bitmapToPrint.isRecycled()) {
            bitmapToPrint.recycle();
        }
    }

}