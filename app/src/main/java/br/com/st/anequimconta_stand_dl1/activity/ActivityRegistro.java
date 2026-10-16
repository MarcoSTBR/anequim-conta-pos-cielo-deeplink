package br.com.st.anequimconta_stand_dl1.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoAutenticacao;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoCNPJ;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoConfTerminal;
import br.com.st.anequimconta_stand_dl1.daoclass.DBHelper;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityRegistroBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Terminal;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.net.MalformedURLException;

public class ActivityRegistro extends AppCompatActivity {

    private ActivityRegistroBinding binding ;
    private String oldCnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.buttonRegistrarCnpj.setOnClickListener(view -> verificaCNPJ());
        oldCnpj = UtilSet.getCnpj(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.editTextCNPJ.setText(UtilSet.getCnpj(this));
        binding.editTextLogin.setText(UtilSet.getLogin(this));
    }

    private void verificaCNPJ() {
        new ConexaoCNPJ(this, binding.editTextCNPJ.getText().toString()) {
            @Override
            public void oK(String cnpj, String url) {
                setCnpj();
            }

            @Override
            public void erro(int cod, String msg) {
                alert("Erro ", msg);
            }

        }.executar();
    }

    private void setCnpj() {
        try {
            new ConexaoAutenticacao(this, binding.editTextCNPJ.getText().toString(), binding.editTextLogin.getText().toString(), binding.editTextPassword.getText().toString()) {
                @Override
                public void oK(String cnpj, String login) {
                    setTerminal();
                }

                @Override
                public void erro(int cod, String msg) {
                    alert("Erro:" + cod, msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            setCnpjRepetir(e.getMessage());
        }
    }

    private void setCnpjRepetir(String txt) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Repetir", (dialogInterface, i) -> setCnpj())
                .setNegativeButton("Finalizar", (dialogInterface, i) -> finish())
                .show();
    }

    private void setTerminal() {
        String mac = UtilSet.getSerialNumberMAC(this);
        new ConexaoConfTerminal(this, mac) {
            @Override
            public void ok(Terminal t) {
                if ((oldCnpj.equals(UtilSet.getCnpj(ActivityRegistro.this))) || (oldCnpj.length() == 0))
                    sair();
                else sairLimpar();
            }

            @Override
            public void erroMsg(int cod, String msg) {
                startActivity(new Intent(ActivityRegistro.this, ActivityTerminal.class));
            }
        }.executar();
    }

    private void alert(String Titulo, String txt) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle(Titulo)
                .setMessage(txt)
                .setCancelable(false)
                .setPositiveButton("Ok", null).show();
    }

    private void sair() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Parabéns")
                .setMessage("CNPJ Registrado!")
                .setCancelable(false)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    finish();
                })
                .show();
    }

    private void sairLimpar() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Limpar Dados")
                .setMessage("Deseja Limpar os Dados Internos?")
                .setCancelable(false)
                .setPositiveButton("Sim", (dialogInterface, i) -> {
                    new DBHelper(ActivityRegistro.this).limparTudo(getBaseContext());
                    finish();
                })
                .setNegativeButton("Não", (dialogInterface, i) -> finish())
                .show();
    }

}