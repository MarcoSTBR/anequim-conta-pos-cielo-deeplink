package br.com.st.anequimconta_stand_dl1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityImpressaoBinding;

public class ActivityImpressao extends AppCompatActivity {

    private ActivityImpressaoBinding binding ;
    private String data ;
    private boolean flag = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImpressaoBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        data = getIntent().getStringExtra("DATA");
        binding.sair.setOnClickListener(v -> {
            Intent intent = new Intent() ;
            intent.putExtra("status", "erro") ;
            intent.putExtra("mensagem", "Interrupção de Usuário!") ;
            setResult(RESULT_CANCELED, intent);
            finish();
        });
        binding.tentarNovamente.setOnClickListener(v -> {imprimir();});
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!flag) {
            flag = true ;
            imprimir();
        }
    }

    private void imprimir() {
        Log.i("stone_", "imprimirBitMap Activity Impressao") ;
        String showFeedback = "false" ;
        String deeplinkReturnScheme = getString(R.string.scheme_caixa_return) ;
        Uri uriBuilder = new Uri.Builder().scheme("printer-app")
                .authority("print")
                .appendQueryParameter("SHOW_FEEDBACK_SCREEN", showFeedback)
                .appendQueryParameter("SCHEME_RETURN", deeplinkReturnScheme)
                .appendQueryParameter("PRINTABLE_CONTENT", data)
                .build();
        Log.i("stone_",  Intent.ACTION_VIEW.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW) ;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        intent.setData(uriBuilder) ;
        startActivity(intent) ;
    }


    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        try {
            Log.i("stone_", "onNewIntent") ;
            Intent intt = new Intent() ;

            if (intent != null && intent.getData() != null) {
                Log.i("stone_", intent.getData().toString());
                String caixa_imp = null;
                caixa_imp = URLDecoder.decode(intent.getData().toString().replace("caixa_imp://print?", ""), "UTF-8");
                if (caixa_imp.equals("DEEPLINK_RETURN=SUCCESS")) {
                    intt.putExtra("status", "success");
                    intt.putExtra("mensagem", "Impressão OK");
                    setResult(RESULT_OK, intt);
                    fechaActivity("Impressão OK") ;
                } else {
                    intt.putExtra("status", "erro");
                    intt.putExtra("mensagem", caixa_imp);
                    setResult(RESULT_CANCELED, intt);
                    fechaActivity(caixa_imp) ;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void fechaActivity(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();

    }

}