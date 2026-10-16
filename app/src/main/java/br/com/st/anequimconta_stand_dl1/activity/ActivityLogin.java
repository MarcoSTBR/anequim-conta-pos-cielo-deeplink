package br.com.st.anequimconta_stand_dl1.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoUsuarioLogin;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityLoginBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Usuario;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.net.MalformedURLException;

public class ActivityLogin extends AppCompatActivity {

    private ActivityLoginBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.toolbarLogin.setTitle("Login");
        setSupportActionBar(binding.toolbarLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarLogin.setTitle("Login");
        binding.buttonEntrar.setOnClickListener((View) -> logar());
    }


    @Override
    protected void onResume() {
        super.onResume();
        binding.editTextUsuario.setText("");
        binding.editTextSenha.setText("");
    }

    private void login(){
        try {
            new ConexaoUsuarioLogin(this, binding.editTextUsuario.getText().toString(), binding.editTextSenha.getText().toString()) {
                @Override
                public void oK(Usuario usu) {
                    UtilSet.setUsuarioId(ActivityLogin.this, usu.getId());
                    UtilSet.setUsuarioNome(ActivityLogin.this, usu.getNOME());
                    finish();
                    //Toast.makeText(ActivityLogin.this, "Atualização OK", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void erro(String msg) {
                    mensagemRapida(msg) ;
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mensagemRapida(e.getMessage()) ;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logar(){
        Log.i("login", binding.editTextUsuario.getText().toString()) ;
        Log.i("login", binding.editTextSenha.getText().toString()) ;
        if (( binding.editTextUsuario.getText().toString().length() == 0) || ( binding.editTextSenha.getText().toString().length() == 0)){
            //Toast.makeText(this, "Dados Incorretos!", Toast.LENGTH_SHORT).show();
            mensagemRapida("Dados Incorretos!") ;
        } else login() ;
    }

    private void mensagemRapida(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
        //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void alertOK(String txt){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }
}