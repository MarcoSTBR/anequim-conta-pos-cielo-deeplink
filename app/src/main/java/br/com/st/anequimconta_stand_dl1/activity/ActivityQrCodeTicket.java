package br.com.st.anequimconta_stand_dl1.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.controle.ControleValidaTicket;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleValidaTicket;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityQrCodeTicketBinding;

public class ActivityQrCodeTicket extends AppCompatActivity implements OnControleValidaTicket {

    private ActivityQrCodeTicketBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrCodeTicketBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot() /* R.layout.activity_qr_code_ticket*/);

        binding.buttonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltar();
            }
        });

        binding.imageViewCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lerQRCode() ;
            }
        });


    }

    private void voltar(){
        super.onBackPressed();
    }

    private void lerQRCode() {
/*
        Intent intent = new Intent(getContext().getApplicationContext(), CaptureActivity.class);
        intent.putExtra("SCAN_FORMATS", "QR_CODE,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,CODE_39,CODE_93,CODE_128,ITF,CODABAR,DATA_MATRIX");
        intent.setAction(Intents.Scan.ACTION);
        startActivityForResult(intent, 0);
*/


/*
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        intent.setAction(Intents.Scan.ACTION);
        startActivityForResult(intent, 0);
*/

        //    addScanner.launch(intent);


        //  startActivityForResult(intent, SCAN_ACTIVITY);


        IntentIntegrator scanner = new IntentIntegrator(this) ;
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) ;
        scanner.setBeepEnabled(true) ;
        scanner.initiateScan();


/*
        IntentIntegrator.forSupportFragment(this).
        setCaptureActivity(FragmentQrcodeTicket.class).
        setOrientationLocked(false).
        setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES).
        setPrompt("Scanning Ticket").
        initiateScan();
*/
    }

    ActivityResultLauncher addScanner = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                String num = result.toString() ;// getData().getStringExtra("NUMERO");
                display("Scaneado: "+num);
            } else display("Numero sem retorno!") ;
        } else display("nada") ;
    });



    @Override
    public void startActivityForResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("retorno", " retorno") ;
        if (resultCode == Activity.RESULT_OK) {
            Log.i("retorno", "ok") ;
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data) ;
            if (result.getContents() == null){
                display("Nenhum QRCode Lido!");
                Toast.makeText(this, "Nenhum QRCode lido!", Toast.LENGTH_SHORT) ;
            } else {
                verificarTicket(result.getContents()) ;
                display("Scaneado: "+result.getContents());
                Toast.makeText(this, "Scaneado: "+result.getContents(), Toast.LENGTH_LONG) ;
            }
        } else {
            Log.i("retorno", " sem retorno") ;
            display("Sem retorno!");
            Toast.makeText(this, "Sem retorno!", Toast.LENGTH_LONG) ;
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void verificarTicket(String uuid) {
        new ControleValidaTicket(this, this).validarUUID(uuid) ;
    }

    private void display(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();

    }

    @Override
    public void ticketRecebido(String obs) {
        binding.textRetorno.setBackgroundResource(R.drawable.back_verde);
        binding.textRetorno.setText(obs);
    }

    @Override
    public void ticketInvalido(String msg) {
        binding.textRetorno.setBackgroundResource(R.drawable.back_vermelho);
        binding.textRetorno.setText(msg);

    }

    @Override
    public void ticketErro(String msg) {
        binding.textRetorno.setBackgroundResource(R.drawable.back_vermelho);
        binding.textRetorno.setText(msg);
        display(msg);

    }

}