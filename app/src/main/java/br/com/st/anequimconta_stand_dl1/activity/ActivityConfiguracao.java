package br.com.st.anequimconta_stand_dl1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterConfMaisVendidos;
import br.com.st.anequimconta_stand_dl1.controle.ControleGradeView;
import br.com.st.anequimconta_stand_dl1.controle.ControleMaisVendidos;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityConfiguracaoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.GradeView;
import br.com.st.anequimconta_stand_dl1.impressao.util.UtilContaParam;
import br.com.st.anequimconta_stand_dl1.tipos.TipoModalidade;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.util.ArrayList;
import java.util.List;

public class ActivityConfiguracao extends AppCompatActivity {

    private ActivityConfiguracaoBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfiguracaoBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValoresMaisVendidos() ;
        setButtonsMasiVendidos() ;
        setOutrosvalores() ;
    }

    private void setOutrosvalores(){
        binding.quantLarConta.setText(String.valueOf(UtilContaParam.getlargMaxConta(this)));
        binding.maisLarConta.setOnClickListener(v -> {
            int l = UtilContaParam.getlargMaxConta(ActivityConfiguracao.this) ;
            UtilContaParam.setlargMaxConta(ActivityConfiguracao.this,  l + 1) ;
            setOutrosvalores();
        });
        binding.menosLarConta.setOnClickListener(v -> {
            int l = UtilContaParam.getlargMaxConta(ActivityConfiguracao.this) ;
            if (l < 30)
                Toast.makeText(ActivityConfiguracao.this, "Valor Inválido!", Toast.LENGTH_SHORT).show();
             else
                UtilContaParam.setlargMaxConta(ActivityConfiguracao.this,  l - 1) ;
            setOutrosvalores();
        });

        binding.quantFontConta.setText(String.valueOf(UtilContaParam.getTamFont(this)));
        binding.maisFontConta.setOnClickListener(v -> {
            int l = UtilContaParam.getTamFont(ActivityConfiguracao.this) ;
            UtilContaParam.setTamFont(ActivityConfiguracao.this,  l + 1) ;
            setOutrosvalores();
        });
        binding.menosFontConta.setOnClickListener(v -> {
            int l = UtilContaParam.getTamFont(ActivityConfiguracao.this) ;
            if (l < 5)
                Toast.makeText(ActivityConfiguracao.this, "Valor Inválido!", Toast.LENGTH_SHORT).show();
            else
                UtilContaParam.setTamFont(ActivityConfiguracao.this,  l - 1) ;
            setOutrosvalores();
        });

    }

    private void setButtonsMasiVendidos(){
        binding.maisLinhas.setOnClickListener(view -> {
            int lin = ControleMaisVendidos.getColunas(ActivityConfiguracao.this) ;
            ControleMaisVendidos.setColunas(ActivityConfiguracao.this, ++lin) ;
            binding.quantLinhas.setText(String.valueOf(ControleMaisVendidos.getColunas(ActivityConfiguracao.this)));
        });
        binding.menosLinhas.setOnClickListener(view -> {
            int lin = ControleMaisVendidos.getColunas(ActivityConfiguracao.this) ;
            if (lin <= 1) {
                alert("Quantidade  Inválida!");
            } else {
                ControleMaisVendidos.setColunas(ActivityConfiguracao.this, --lin);
                binding.quantLinhas.setText(String.valueOf(ControleMaisVendidos.getColunas(ActivityConfiguracao.this)));
            }
        });
    }

    private void setValoresMaisVendidos() {
        binding.quantLinhas.setText(String.valueOf(ControleMaisVendidos.getColunas(this)));
        binding.numeroTerminal.setText("Número do Terminal : "+ UtilSet.getSerialNumberMAC(this));
        binding.gradeConfMaisVendidos.setLayoutManager(new GridLayoutManager(this, 1));
        binding.gradeConfMaisVendidos.setAdapter(new AdapterConfMaisVendidos(this, getGrade()) {
            @Override
            public void selecionado(GradeView g) {
                ControleMaisVendidos.setGradeID(ActivityConfiguracao.this, g.getGrade().getId());
                ControleMaisVendidos.getControleMaisVendidos(ActivityConfiguracao.this).atualizar();
                setValoresMaisVendidos();
                alert(g.getGrade().getDescricao()+ " Selecionado!") ;
            }
        });

        binding.desmarcarGrade.setOnClickListener(view->{
            ControleMaisVendidos.setGradeID(ActivityConfiguracao.this, 0);
            ControleMaisVendidos.getControleMaisVendidos(ActivityConfiguracao.this).atualizar();
            setValoresMaisVendidos();
            alert("Todos Desmarcados!") ;
        });
    }

    private void alert(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();
    }

    private List<GradeView> getGrade(){
        return ControleGradeView.getControleGradeView(this).getList() ;
    }

}