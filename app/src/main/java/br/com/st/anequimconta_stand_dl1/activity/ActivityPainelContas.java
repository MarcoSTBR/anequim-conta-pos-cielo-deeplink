package br.com.st.anequimconta_stand_dl1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterContas;
import br.com.st.anequimconta_stand_dl1.controle.ControleConta;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleConta;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityPainelContasBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;

import java.util.List;

public class ActivityPainelContas extends AppCompatActivity implements OnControleConta {

    private ActivityPainelContasBinding binding ;
    private String titulo ;
    private ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPainelContasBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.textTitulo.setText("");
        titulo = getIntent().getStringExtra("TITULO") ;
        if (titulo != null)
            binding.textTitulo.setText(titulo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Consultando....");
        progressDialog.setCancelable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContas();
    }

    private void setContas() {
        progressDialog.show();
        new ControleConta(this, this).getContasAbertas();
    }

    @Override
    public void contasOk(List<Conta> l) {
        progressDialog.dismiss();
       if (l.size() > 0) {
           l.sort(new Conta.NumeroComparador());
           GridLayoutManager layout = new GridLayoutManager(this, 3);
           binding.gradeContas.setLayoutManager(layout);
           binding.gradeContas.setAdapter(new AdapterContas(this, l) {
               @Override
               public void selecionado(Conta conta) {
                   Intent i = new Intent();
                   i.putExtra("CONTA_UUID", conta.getUUID());
                   i.putExtra("NUMERO", conta.getNUMERO());
                   setResult(RESULT_OK, i);
                   finish();
               }
           });
       } else contaErro("Não Existe Contas Abertas!") ;

    }

    @Override
    public void contaErro(String msg) {
        progressDialog.dismiss();
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (d,i)-> finish())
                .show();
    }
}