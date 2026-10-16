package br.com.st.anequimconta_stand_dl1.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityAddPedidoBinding;

import br.com.st.anequimconta_stand_dl1.controle.ControleAddPedido;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleAddPedido;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.pospag.payCielo.services.OnCrachaLidoListener;
import br.com.st.anequimconta_stand_dl1.pospag.payCielo.services.LeituraCracha;

public class ActivityAddPedido extends AppCompatActivity implements View.OnClickListener, OnControleAddPedido, OnCrachaLidoListener {

    private final String TAG = "addpedido" ;
    private ActivityAddPedidoBinding binding ;
    private long num_pedido = 0 ;
    private ControleAddPedido controleAddPedido ;
    private LeituraCracha leituraCracha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPedidoBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.b0.setOnClickListener(this);
        binding.b1.setOnClickListener(this);
        binding.b2.setOnClickListener(this);
        binding.b3.setOnClickListener(this);
        binding.b4.setOnClickListener(this);
        binding.b5.setOnClickListener(this);
        binding.b6.setOnClickListener(this);
        binding.b7.setOnClickListener(this);
        binding.b8.setOnClickListener(this);
        binding.b9.setOnClickListener(this);
        binding.bEntrar.setOnClickListener(this);
        binding.bApagar.setOnClickListener(this);
        binding.bTPedidoCodigo.setOnClickListener((view -> addCodigo()));
        binding.bTAddpedido.setOnClickListener(view -> addPedido());

        binding.bTCrachaRFC.setOnClickListener(view -> dispararLeituraCieloMifare());

        controleAddPedido = new ControleAddPedido(this, this) ;
        leituraCracha = new LeituraCracha(this,this);
    }


    private void addCodigo() {
        barcodeLauncher.launch(new ScanOptions());
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                Log.i(TAG, "ActivityResultLauncher") ;
                if (result.getContents() != null) {
                    entrar(result.getContents()) ;
                    // Handle success
                }
            });



    @Override
    protected void onResume() {
        super.onResume();
        displayNumero();
   //     rfidReceiver.config();
    }


    @Override
    protected void onPause() {
        super.onPause();
        leituraCracha.desconectar() ;
    }


    // Tradução exata do código que você encontrou para Java nativo
    private void dispararLeituraCieloMifare() {
        leituraCracha.start();
    }


    private void addPedido() {
        Intent intent = new Intent(this, ActivityPainelContas.class) ;
        addpedido.launch(intent);
        //  startActivity(new Intent(getActivity(), ActivityAddPedido.class));
    }

    ActivityResultLauncher addpedido = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                entrar(result.getData().getStringExtra("CONTA_UUID")) ;
                //setAddNumeroConta(num) ;
            }
        } ;
    });

    private void apagar() {
        String s = String.valueOf(num_pedido) ;
        if (s.length() > 1) {
            s = s.substring(0, s.length()-1) ;
        } else if (s.length() <= 1) {
            s = "0" ;
        }
        num_pedido = Integer.valueOf(s) ;
        displayNumero();
    }

    private void entrar(String uuid) {
           controleAddPedido.addUUID(uuid);
    }

    private void entrar() {
        if (num_pedido > 0) {
            controleAddPedido.addNumero(num_pedido);
        } else {
            Toast.makeText(this, "Pedido inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNumero(String t){
        try {
            num_pedido = Integer.valueOf(String.valueOf(num_pedido) + t);
            displayNumero();
        } catch (java.lang.NumberFormatException e){
            e.printStackTrace();
            displayNumero();
            alert("Número Inválido!");
        }
    }

    private void displayNumero(){
        if (num_pedido>0)
            binding.numPedido.setText(String.valueOf(num_pedido));
        else
            binding.numPedido.setText("");
    }

    private void alert(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Erro")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.b0) addNumero("0") ;
        else if (view.getId() == R.id.b1) addNumero("1") ;
        else if (view.getId() == R.id.b2) addNumero("2") ;
        else if (view.getId() == R.id.b3) addNumero("3") ;
        else if (view.getId() == R.id.b4) addNumero("4") ;
        else if (view.getId() == R.id.b5) addNumero("5") ;
        else if (view.getId() == R.id.b6) addNumero("6") ;
        else if (view.getId() == R.id.b7) addNumero("7") ;
        else if (view.getId() == R.id.b8) addNumero("8") ;
        else if (view.getId() == R.id.b9) addNumero("9") ;
        else if (view.getId() == R.id.bEntrar) entrar() ;
        else if (view.getId() == R.id.bApagar)  apagar() ;
        else alert("Opção não encontrada "+view.getId());
    }

    @Override
    public void numeroPedidoLiberado(String num) {
        Intent i = new Intent() ;
        i.putExtra("NUMERO", String.valueOf(num)) ;
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void numeroPedidoTravado(Conta c) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage("Conta git branch -M master[ "+c.getNUMERO()+" ] foi emitida, deseja liberar?")
                .setCancelable(false)
                .setPositiveButton("Sim", (dialogInterface, i) -> controleAddPedido.liberar(c))
                .setNegativeButton("Não", (dialogInterface, i) -> {
                    finish();
                })
                .show();

    }

    @Override
    public void numeroPedidoErro(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Erro")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
    }


    @Override
    public void onCrachaDetectado(String codigoUid) {
        Log.i(TAG, "onCrachaDetectado "+codigoUid) ;
        numeroPedidoErro(codigoUid) ;
    }

    @Override
    public void onCrachaErro(String msg) {
        Log.i(TAG, "onCrachaErro "+msg) ;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCrachaChange(String msg) {
        Log.i(TAG, "onCrachaChange "+msg) ;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}