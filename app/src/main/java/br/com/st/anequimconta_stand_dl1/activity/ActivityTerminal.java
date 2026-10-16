package br.com.st.anequimconta_stand_dl1.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoConfTerminal;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityTerminalBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Terminal;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class ActivityTerminal extends AppCompatActivity {

    private ActivityTerminalBinding binding;
    private Terminal terminal;
    private int opcao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTerminalBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarTerminal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        terminal = new Terminal(0, UtilSet.getCnpj(this), UtilSet.getSerialNumberMAC(this), "", 0, 1);
        binding.terminalConfirmar.setOnClickListener(view -> atualizar());
    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.toolbarTerminal.setTitle("Terminal "+UtilSet.getSerialNumberMAC(this));
        binding.textViewLoja.setText(UtilSet.getLojaNome(this));
        selecionar(0);
        getTermnal();
    }

    public int selecionado() {
        int op = binding.radiogrupotipoemissao.getCheckedRadioButtonId();
        if (op == R.id.bteNenhuma) opcao = 0 ;
        else if (op == R.id.bteNfce) opcao = 1 ;
        else if (op == R.id.bteSAT) opcao = 2 ;
        else {
            opcao = 0;
            alert("Nenhuma Opção Selecionada!") ;
        }
        return opcao;
    }

    public void selecionar(int op) {
        switch (op) {
            case 0:
                binding.bteNenhuma.setChecked(true);
                break;
            case 1:
                binding.bteNfce.setChecked(true);
                break;
            case 2:
                binding.bteSAT.setChecked(true);
                break;
        }
    }

    public void getTermnal() {
        //String m = "38-EA-A7-D2-53-6C" ;
        String m = UtilSet.getSerialNumberMAC(this);
        binding.editTerminal.setText(m);
        new ConexaoConfTerminal(this, m) {

            @Override
            public void ok(Terminal t) {
                setDadosTerminal(t);
            }

            @Override
            public void erroMsg(int cod, String msg) {
                alert("COD: "+cod+" "+msg) ;
                //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();

            }
        }.executar();
        ;
    }

    public void setDadosTerminal(Terminal t) {
        binding.editTerminal.setText(t.getDescricao());
        selecionar(t.getTipo_emissao());
        terminal = t;
    }

    private void atualizar() {
        terminal.setDescricao(binding.editTerminal.getText().toString());
        terminal.setTipo_emissao(selecionado());

        new ConexaoConfTerminal(this, UtilSet.getLojaId(this), UtilSet.getSerialNumberMAC(this), binding.editTerminal.getText().toString()){
            @Override
            public void ok(Terminal t) {
                setOK(t);
            }

            @Override
            public void erroMsg(int cod, String msg) {
                alert("COD: "+cod+" "+msg) ;
                // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
        }.executar();
    }

    private void alert(String msg) {
        Log.i("terminal", "msg "+msg) ;
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
    }

    private void setOK(Terminal t) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Terminal OK:")
                .setMessage("Cadastrado com sucesso\n" + t.getMac() + "\n" + t.getDescricao())
                .setCancelable(false)
                .setPositiveButton("Continuar", (dialog, which) -> finish()).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;//super.onOptionsItemSelected(item);
    }

}