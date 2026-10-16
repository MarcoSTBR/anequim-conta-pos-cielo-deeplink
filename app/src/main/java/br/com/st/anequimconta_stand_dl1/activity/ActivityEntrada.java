package br.com.st.anequimconta_stand_dl1.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.controle.ControleEntrada;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityEntradaBinding;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class ActivityEntrada extends AppCompatActivity {

    private ActivityEntradaBinding binding ;

    private boolean flagEntrada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntradaBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.btEntrar.setOnClickListener(view -> entrar());
        binding.textTitulo.setText("Anequim Conta\n"+UtilSet.getVersao(this));
        flagEntrada = false;
    }


    private void entrar() {

        new ControleEntrada(this) {
            @Override
            public void venda() {
                startActivity(new Intent(ActivityEntrada.this, ActivityPrincipal.class));
                //startActivity(new Intent(ActivityEntrada.this, ActivityVenda.class));
            }

            @Override
            public void entradaErro(String msg) {
                alert(msg);
            }

            @Override
            public void autenticacao() {
                startActivity(new Intent(ActivityEntrada.this, ActivityRegistro.class));
            }

            @Override
            public void login() {
                startActivity(new Intent(ActivityEntrada.this, ActivityLogin.class));
            }

            @Override
            public void terminal() {
                startActivity(new Intent(ActivityEntrada.this, ActivityTerminal.class));
            }


        }.executar();


       // startActivity(new Intent(this, ActivityPrincipal.class));
    }

    private void alert(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", ((dialogInterface, i) -> finish()))
                .show();
    }

}