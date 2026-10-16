package br.com.st.anequimconta_stand_dl1.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.controle.ControleValidaTicket;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleValidaTicket;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentQrcodeTicketBinding;

public class FragmentQrcodeTicket extends Fragment implements OnControleValidaTicket {

    private FragmentQrcodeTicketBinding binding ;

    public FragmentQrcodeTicket() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQrcodeTicketBinding.inflate(inflater, container, false) ;
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

        return binding.getRoot() ;//inflater.inflate(R.layout.fragment_qrcode_ticket, container, false);
    }

    private void voltar(){
        requireActivity().onBackPressed();
    }

    private void lerQRCode() {
/*
        Intent intent = new Intent(getContext().getApplicationContext(), CaptureActivity.class);
        intent.putExtra("SCAN_FORMATS", "QR_CODE,EAN_13,EAN_8,RSS_14,UPC_A,UPC_E,CODE_39,CODE_93,CODE_128,ITF,CODABAR,DATA_MATRIX");
        intent.setAction(Intents.Scan.ACTION);
*/
        //startActivityForResult(intent, 0, null);


/*
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        intent.setAction(Intents.Scan.ACTION);
        startActivityForResult(intent, 0);
*/

    //    addScanner.launch(intent);


      //  startActivityForResult(intent, SCAN_ACTIVITY);

/*
        IntentIntegrator scanner = IntentIntegrator.forSupportFragment(this) ;
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) ;
        scanner.setBeepEnabled(true) ;
        scanner.initiateScan();
*/

/*
        IntentIntegrator.forSupportFragment(this).
        setCaptureActivity(FragmentQrcodeTicket.class).
        setOrientationLocked(false).
        setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES).
        setPrompt("Scanning Ticket").
        initiateScan();
*/
/*
        integrator.initiateScan();
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "ONE_D_MODE");
        intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
        startActivityForResult(intent, 1);
*/
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(FragmentQrcodeTicket.this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan QR code Ticket");
        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("retorno", "requestCode "+requestCode+" resultCode "+resultCode) ;

        if (resultCode == Activity.RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data) ;
            if (result.getContents() != null){
                verificarTicket(result.getContents()) ;
            } else {
                display("Nenhum Código Encontrado!");
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
            display("Nenhum Código Registrado!");
        }
    }

    /*
    ActivityResultLauncher addScanner = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                String num = result.toString() ;// getData().getStringExtra("NUMERO");
                display("Scaneado: "+num);
            } else display("Numero sem retorno!") ;
        } else display("nada") ;
    });
*/

/*
    @Override
    public void onActivityForResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("retorno", " retorno") ;
        if (resultCode == Activity.RESULT_OK) {
            Log.i("retorno", "ok") ;
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data) ;
            if (result.getContents() == null){
                display("Nenhum QRCode Lido!");
                Toast.makeText(getContext(), "Nenhum QRCode lido!", Toast.LENGTH_SHORT) ;
            } else {
                verificarTicket(result.getContents()) ;
                display("Scaneado: "+result.getContents());
                Toast.makeText(getContext(), "Scaneado: "+result.getContents(), Toast.LENGTH_LONG) ;
            }
        } else {
            Log.i("retorno", " sem retorno") ;
            display("Sem retorno!");
            Toast.makeText(getContext(), "Sem retorno!", Toast.LENGTH_LONG) ;
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
*/

    private void verificarTicket(String uuid) {
        new ControleValidaTicket(getActivity(), this).validarUUID(uuid) ;
    }

    private void display(String msg){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();

    }

    @Override
    public void startActivityForResult(int requestCode, int resultCode, @Nullable Intent data) {

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