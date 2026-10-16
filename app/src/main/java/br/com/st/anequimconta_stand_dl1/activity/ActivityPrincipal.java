package br.com.st.anequimconta_stand_dl1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import br.com.st.anequimconta_stand_dl1.R ;
import br.com.st.anequimconta_stand_dl1.controle.ControleAtualizar;
import br.com.st.anequimconta_stand_dl1.controle.ControleGradeView;
import br.com.st.anequimconta_stand_dl1.controle.ControleMaisVendidos;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityPrincipalBinding;

public class ActivityPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

/*
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pedido_corrente, R.id.nav_pedido_vendas,  R.id.nav_carrinho, R.id.nav_cancelamento, R.id.nav_transferencia)
                .setOpenableLayout(drawer)
                // .setFallbackOnNavigateUpListener(this)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        configInicial() ;
    }

    private void configInicial(){
        Log.i("onResume_", "foi ") ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ic_menu_login) startActivity(new Intent(this, ActivityLogin.class));
        else if (item.getItemId() == R.id.ic_menu_atualizar) atualizar();
        else if (item.getItemId() == R.id.ic_operacional) startActivity(new Intent(this, ActivityConfigucacoesOperacionais.class));
        else if (item.getItemId() == R.id.ic_configuracao) startActivity(new Intent(this, ActivityConfiguracao.class));
        else if (item.getItemId() == R.id.ic_menu_nfce) startActivity(new Intent(this, ActivityConfiguracaoNfce.class));
        else if (item.getItemId() == R.id.ic_autenticar) startActivity(new Intent(this, ActivityRegistro.class));
      //  else if (item.getItemId() == R.id.nav_qrcode_ticket) startActivity(new Intent(this, ActivityQrCodeTicket.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void atualizar(){
        new ControleAtualizar(this) {
            @Override
            public void atualizacaoOk() {
                alertAtualizacao("Atualização OK") ;
                ControleMaisVendidos.getControleMaisVendidos(ActivityPrincipal.this).setAtualizado(false);
                ControleGradeView.getControleGradeView(ActivityPrincipal.this).atualizar();
            }

            @Override
            public void atualizacaoErro(String msg) {
                alert(msg);
            }
        }.executar();
    }
    private void alertAtualizacao(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", null)
                .show();
        //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void alert(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
        //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().
        beginTransaction().
        replace(R.id.nav_host_fragment_content_main, fragment).
        commit() ;
    }


}