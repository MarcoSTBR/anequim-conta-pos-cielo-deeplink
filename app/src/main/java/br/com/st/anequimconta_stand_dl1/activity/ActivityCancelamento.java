package br.com.st.anequimconta_stand_dl1.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityCancelamentoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPos;

public class ActivityCancelamento extends AppCompatActivity {

    private final String BUNDLE_EXTRA_RETURN_SCHEME = "returnscheme" ;
    private final String BUNDLE_EXTRA_ATK = "atk";
    private final String BUNDLE_EXTRA_AMOUNT = "amount" ;
    private final String BUNDLE_EXTRA_EDITABLE_AMOUNT = "editable_amount" ;
    private final String BUNDLE_EXTRA_THIRD_PARTY_THEME_ENABLED = "third_party_theme_enabled" ;

    private ActivityCancelamentoBinding binding ;
    private String data ;
    private RecebimentoPos recebimentoPos ;
    private String amount  ;
    private String atk  ;
    private boolean iniciar = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCancelamentoBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        data = getIntent().getStringExtra("DATA") ;
        Log.i("stone_", "data " + data.toString()) ;

        binding.sair.setOnClickListener(v -> sairSemCancelar());
        binding.tentarNovamente.setOnClickListener(v-> executar());
        binding.tentarNovamente.setText("Cancelar");
        setRecebimento();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!iniciar){
           iniciar = true ;
           buscarCanc();
        }

    }

    private void buscarCanc() {

    }

    private void sairSemCancelar() {
        Intent itt = new Intent();
        itt.putExtra("status", "erro") ;
        itt.putExtra("mensagem", "Sem Cancelamento") ;
        setResult(RESULT_CANCELED, itt);

    }


    private void setRecebimento() {
        try {
            recebimentoPos = new RecebimentoPos(new JSONObject(data)) ;
            JSONObject j = new JSONObject(recebimentoPos.getRecibo()) ;
            amount = j.getString("amount") ;
            atk = j.getString("atk") ;
            Double i = Integer.parseInt(amount) / 100.0 ;
            Log.i("stone_", "amount " + amount) ;
            Log.i("stone_", "atk " + atk) ;
            binding.textAtk.setText("Atk "+atk);
            binding.textAmount.setText(new DecimalFormat("R$ #0.00").format(i));
        } catch (JSONException e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            alertSair("Erro: "+e.getMessage()) ;
        }
    }

    private void executar(){
        binding.tentarNovamente.setText("Tentar Novamente");
        String deepLinkReturnScheme = getString(R.string.scheme_return_capture) ;

        Uri uriBuilder = new Uri.Builder()
                .scheme("cancel-app")
                .authority("cancel")
                .appendQueryParameter(BUNDLE_EXTRA_RETURN_SCHEME, deepLinkReturnScheme)
                .appendQueryParameter(BUNDLE_EXTRA_ATK, atk)
                .appendQueryParameter(BUNDLE_EXTRA_AMOUNT, amount)
                .appendQueryParameter(BUNDLE_EXTRA_EDITABLE_AMOUNT, "false")
                .appendQueryParameter(BUNDLE_EXTRA_THIRD_PARTY_THEME_ENABLED, "true")
                .build() ;


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uriBuilder) ;
        startActivity(intent) ;

    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        Log.i("stone_", "cancela onNewIntent ") ;
        String ms = "Cancelamento OK" ;
        Boolean ok = false ;
        Intent itt = new Intent();
        JSONObject j = new JSONObject() ;
        if ((intent != null) && (intent.getData() != null)){
            try {
                Log.i("stone_", "cancela "+intent.getData().toString()) ;
                String[] data =  intent.getData().toString().replace("deeplink_capture://cancel?","").split("&") ;
                for (String ss : data){
                    String[] data_add = ss.split("=") ;
                    j.put(data_add[0], data_add[1]) ;
                    Log.i("stone_", "cancela "+data_add[0] + " "+data_add[1]) ;
                    if (data_add[0].equals("success")) {
                        if (data_add[1].equals("true")) {
                            ok = true ;
                            itt.putExtra("status", "success");
                        } else {
                            itt.putExtra("status", "erro") ;
                        }
                    }
                    if (!ok && data_add[0].equals("reason"))
                        ms = "Erro [reason "+data_add[1]+"]" ;
                   //     itt.putExtra("status", "success") ;

                }
                itt.putExtra("mensagem", ms) ;
                itt.putExtra("resp", j.toString()) ;
                setResult(RESULT_OK, itt);
                //alertSair("Cancelamento OK") ;
                sairOk(j);
            } catch (JSONException e) {
                e.printStackTrace();
                setResult(RESULT_CANCELED);
                alertSair("Erro: "+e.getMessage()) ;
            }
        } else {
            itt.putExtra("status", "erro") ;
            itt.putExtra("mensagem", "Operação Cancelada!") ;
            setResult(RESULT_CANCELED, itt);
            alertSair("Operação Cancelada!") ;
        }
    }

    private void sairOk(JSONObject j){
        Log.i("stone_", "saiu direto "+j.toString()) ;
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
}