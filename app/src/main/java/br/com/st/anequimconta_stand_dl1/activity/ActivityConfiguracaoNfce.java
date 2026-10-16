package br.com.st.anequimconta_stand_dl1.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityConfiguracaoNfceBinding;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.ConexaoCloudNFceCertificado;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.ConexaoConfiguracaoCloudNFCe;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.ConfiguracaoCloudNFceNFCe;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerCloudNFCeCertificado;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerConfiguracaoCloudNFCe;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityConfiguracaoNfce extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener  {

    private ActivityConfiguracaoNfceBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfiguracaoNfceBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot() /*R.layout.activity_configuracao_nfce*/);
        binding.recuperarToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new ConexaoConfiguracaoCloudNFCe(ActivityConfiguracaoNfce.this, new ListenerConfiguracaoCloudNFCe() {
                        @Override
                        public void ok(String token) {
                            //Toast.makeText(getContext(), token, Toast.LENGTH_LONG).show();
                            ConfiguracaoCloudNFceNFCe.setCloudNfce_token(ActivityConfiguracaoNfce.this, token);
                            setToken(token) ;
                            getCertificado() ;

                        }

                        @Override
                        public void erro(int cod, String msg) {
                            alert(msg);

                        }
                    }).getToken();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    alert(e.getMessage());
                }
            }
        });

        binding.editCloudNFeSerie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")){
                    ConfiguracaoCloudNFceNFCe.setCloudNfce_serie(ActivityConfiguracaoNfce.this, Integer.valueOf(editable.toString()));
                }
            }
        });

        binding.editCloudNFeNumero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")){
                    ConfiguracaoCloudNFceNFCe.setCloudNfce_numero(ActivityConfiguracaoNfce.this, Integer.valueOf(editable.toString()));
                }
            }
        });

        binding.testeCertificadoCloudnfce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCertificado() ;
            }
        });

        binding.nfceOpcao.setOnCheckedChangeListener(this::onCheckedChanged);
/*
        binding.nfceSat.setOnCheckedChangeListener(this::onCheckedChanged) ;
        binding.nfceNenhum.setOnCheckedChangeListener(this);
        binding.nfceCloudNfce.setOnCheckedChangeListener(this);
        binding.cloudNFeAmbienteProducao.setOnCheckedChangeListener(this);
        binding.cloudNFeAmbienteHomologacao.setOnCheckedChangeListener(this);
*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        setSerieNumero() ;
        setOpcoes() ;
        setToken(ConfiguracaoCloudNFceNFCe.getCloudNfce_token(this)) ;
    }

    private void setSerieNumero() {
        binding.editCloudNFeSerie.setText(ConfiguracaoCloudNFceNFCe.getCloudNfce_serie(this).toString()) ;
        binding.editCloudNFeNumero.setText(ConfiguracaoCloudNFceNFCe.getCloudNfce_numero(this).toString()) ;
    }

    private void setOpcoes() {
        int op = ConfiguracaoCloudNFceNFCe.getOpcaoNFCE(this) ;
        Log.i("setopcao", "opção "+op) ;
        switch (op){
            case 2 : binding.nfceSat.setChecked(true);
                break;
            case 1 : binding.nfceCloudNfce.setChecked(true);
                break;
            default: binding.nfceNenhum.setChecked(true);
        }
        if (ConfiguracaoCloudNFceNFCe.getOpcaoAmbiente(this) == 0){
            binding.cloudNFeAmbienteHomologacao.setChecked(true);
        } else {
            binding.cloudNFeAmbienteProducao.setChecked(true);
        }
    }

    private void getCertificado() {
        try {
            new ConexaoCloudNFceCertificado(this, new ListenerCloudNFCeCertificado(){
                @Override
                public void ok(String msg, Date validade) {
                    ConfiguracaoCloudNFceNFCe.setCertificadoValidade(ActivityConfiguracaoNfce.this, validade) ;
                    SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy") ;
                    alert(msg+" Validade "+sd.format(ConfiguracaoCloudNFceNFCe.getCertificadoValidade(ActivityConfiguracaoNfce.this)));
                    //Toast.makeText(getContext(), msg+" validade "+sd.format(ConfiguracaoCloudNFceNFCe.getCertificadoValidade(getContext())), Toast.LENGTH_LONG).show(); ;
                }


                @Override
                public void erro(int cod, String msg) {
                    alert("COD: "+cod+" "+msg);

                }
            }).execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage()) ;
        }

    }
    private void setToken(String token){
        binding.cloudNfceToken.setText(token);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.nfce_sat : ConfiguracaoCloudNFceNFCe.setOpcaoNFCE(this, 2);
                binding.layoutCloudNfce.setVisibility(View.GONE);
                break;

            case R.id.nfce_cloud_nfce : ConfiguracaoCloudNFceNFCe.setOpcaoNFCE(this, 1);
                binding.layoutCloudNfce.setVisibility(View.VISIBLE);
                break;

            case R.id.nfce_nenhum: ConfiguracaoCloudNFceNFCe.setOpcaoNFCE(this, 0);
                binding.layoutCloudNfce.setVisibility(View.GONE);
                break;

            case R.id.cloudNFe_ambiente_homologacao: ConfiguracaoCloudNFceNFCe.setOpcaoAmbiente(this, 0);
                break;

            case R.id.cloudNFe_ambiente_producao:  ConfiguracaoCloudNFceNFCe.setOpcaoAmbiente(this, 1);
                break;

            default:
                Toast.makeText(this, "Nenhuma Opção Selecionada", Toast.LENGTH_SHORT).show();
        }
    }

    private void alert(String msg){
        new AlertDialog.Builder(this)
                //  .setIcon(R.drawable.ic_notifications_black_24dp)
                .setTitle("Atençao")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show() ;
    }

}