package br.com.st.anequimconta_stand_dl1.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityReimprimirBinding;

public class ActivityReimprimir extends AppCompatActivity {

    private ActivityReimprimirBinding binding;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReimprimirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        data = getIntent().getStringExtra("DATA");
        binding.mensagem.setText("Atk "+data);
        binding.sair.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("status", "erro");
            intent.putExtra("mensagem", binding.mensagem.getText().toString());
            setResult(RESULT_CANCELED, intent);
            finish();
        });
        binding.tentarNovamente.setOnClickListener(v -> {
            imprimir();
        });
      //  imprimir();

    }

    private void imprimir() {
        Log.i("stone_", "reimprimir ActivityReimprimir");
        String showFeedback = "true";
        String deeplinkReturnScheme = getString(R.string.scheme_caixa_return);
        Uri uriBuilder = new Uri.Builder().scheme("reprinter-app")
                .authority("reprint")
                .appendQueryParameter("SHOW_FEEDBACK_SCREEN", showFeedback)
                .appendQueryParameter("SCHEME_RETURN", deeplinkReturnScheme)
                .appendQueryParameter("ATK", data)
                .appendQueryParameter("TYPE_CUSTOMER", "MERCHANT")
                .build();
        startMyActivity(uriBuilder);
    }

    private void startMyActivity(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        Log.i("stone_", "onNewIntent");
        Intent intt = new Intent();

        if (intent != null && intent.getData() != null) {
            Log.i("stone_", intent.getData().toString());
            String caixa_imp = intent.getData().toString().replace("caixa_imp://print?", "");
            if (caixa_imp.equals("DEEPLINK_RETURN=SUCCESS")) {
                intt.putExtra("status", "success");
                intt.putExtra("mensagem", "Impressão OK");
                setResult(RESULT_OK, intt);
                finish();
            } else {
                intt.putExtra("status", "erro");
                intt.putExtra("mensagem", caixa_imp);
                setResult(RESULT_CANCELED, intt);
                finish();
            }
        }

    }
}