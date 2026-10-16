package br.com.st.anequimconta_stand_dl1.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.adapter.AdapterOperacional;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityConfigucacoesOperacionaisBinding;

public class ActivityConfigucacoesOperacionais extends AppCompatActivity {

    private ActivityConfigucacoesOperacionaisBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigucacoesOperacionaisBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setParamentros() ;
    }

    private void setParamentros() {
        binding.gradeOperacionais.setAdapter(new AdapterOperacional(this));
    }


}