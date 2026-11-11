package br.com.st.anequimconta_stand_dl1.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterSetorObs;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoSetorObs;
import br.com.st.anequimconta_stand_dl1.dao.PedidoItemDao;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityAddPedidoBinding;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityItemObsBinding;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.entidade.SetorObs;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class ActivityItemObs extends AppCompatActivity {

    private ActivityItemObsBinding binding ;
    private PedidoItem pedidoItem ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemObsBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        binding.limpar.setOnClickListener(v -> binding.obs.setText(""));
        binding.sair.setOnClickListener(v -> finish());
        binding.gravar.setOnClickListener(v -> gravar());
        setPedidoItem(getIntent().getStringExtra("DATA")) ;
    }

    private void gravar(){
        try {
            pedidoItem.setOBS(binding.obs.getText().toString());
            PedidoItemDao.getPedidoItemDao(this).update(pedidoItem.geJSON());
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void setPedidoItem(String data){
        try {
            pedidoItem = new PedidoItem(new JSONObject(data)) ;
            Produto p = ProdutoDao.getProdutoDao(this).getProduto(pedidoItem.getPRODUTO_ID()) ;
            binding.produto.setText(p.getDescricao());
            binding.obs.setText(pedidoItem.getOBS());
            getSetor(p.getId());
        } catch (JSONException e) {
            e.printStackTrace();
            alert(e.getMessage()) ;
        }
    }

    private void getSetor(int produto_id){
        try {
          DaoFilters f = new DaoFilters();
          f.add(new DaoFilter("LOJA_ID", "=", String.valueOf(UtilSet.getLojaId(this))));
          f.add(new DaoFilter("PRODUTO_ID", "=", String.valueOf(produto_id)));
          f.add(new DaoFilter("STATUS", "=", "1"));
          new ConexaoSetorObs(this, f, "OBSERVACAO") {
              @Override
              public void setorObsOk(List<SetorObs> l) {
                  filter(l);
              }

              @Override
              public void erro(String msg) {
                  alert(msg) ;
              }
          }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage()) ;
        }

    }

    private void filter(List<SetorObs> l) {
        GridLayoutManager layoutManager = new GridLayoutManager(this,  1);
        binding.gradeObs.setLayoutManager(layoutManager);
        binding.gradeObs.setAdapter(new AdapterSetorObs(this, l) {
            @Override
            public void setorObsOk(SetorObs setorObs) {
                if (binding.obs.getText().length() > 0)
                    binding.obs.setText(binding.obs.getText()+" \n"+setorObs.getOBSERVACAO()) ;
                 else binding.obs.setText(setorObs.getOBSERVACAO()) ;
            }
        });
    }

    private void alert(String msg){
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Sim", null)
                .show();
    }


}