package br.com.st.anequimconta_stand_dl1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterAcompanhamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleAcompanhamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleGravarVendaItem;
import br.com.st.anequimconta_stand_dl1.controle.ControleProdutoAdd;
import br.com.st.anequimconta_stand_dl1.controle.ControleProdutoValorAdd;
import br.com.st.anequimconta_stand_dl1.controle.ControleValidacaoProdutoAdd;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenrControleVenda;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityProdutoAddBinding;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItem;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.text.DecimalFormat;
import java.util.List;

public class ActivityProdutoAdd extends AppCompatActivity {


    private ActivityProdutoAddBinding binding;
    private Produto produto = null;
    private int produto_id;
    private PedidoItem pedidoItem;
    private List<AcompanhamentoView> acompanhamentoViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProdutoAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        produto_id = getIntent().getIntExtra("PRODUTO_ID", 0);
        binding.voltar.setOnClickListener((View) -> finish());
        binding.mais.setOnClickListener((View) -> setMais());
        binding.menos.setOnClickListener((View) -> setMenos());
        setProduto();
    }

    private void setProduto() {
        produto = ProdutoDao.getProdutoDao(this).getProduto(produto_id);
        if (produto != null) {
            acompanhamentoViews = ControleAcompanhamento.getControleAcompanhamento(ActivityProdutoAdd.this).list(produto);
            setQuantidade();
        } else sair("Produto Não Encontrado! [" + produto_id + "]");
    }

    private void setQuantidade() {
        new ControleProdutoValorAdd(this) {
            @Override
            public void ok(Produto p, Double quantidade) {
                setValores(quantidade);
            }

            @Override
            public void erro(String msg) {
                sair(msg);

            }
        }.addProduto(produto);

    }
    private void setValores(Double q) {
        new ControleProdutoAdd(ActivityProdutoAdd.this, new ListenrControleVenda() {
            @Override
            public void vendaOk(PedidoItem it) {
                pedidoItem = it;
                display();
            }

            @Override
            public void vendaErro(String msg) {
                sair(msg);
            }
        }).addProduto(produto, q);
    }

    private void display() {
        binding.descricao.setText(produto.getDescricao() + "\n" + new DecimalFormat("R$ #0.00").format(pedidoItem.getVALOR()));
        binding.quantidade.setText(new DecimalFormat(" #0.###").format(pedidoItem.getQUANTIDADE()));
        grade();
    }

    private void grade() {
        if (acompanhamentoViews.size() > 0) {
            binding.layoutAcompanhamento.setVisibility(View.VISIBLE);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            binding.gradeAcompanhamento.setLayoutManager(layoutManager);
            binding.gradeAcompanhamento.setAdapter(new AdapterAcompanhamento(this, acompanhamentoViews) {
                @Override
                public void change() {
                    addchange();
                }
            });
        } else
            binding.layoutAcompanhamento.setVisibility(View.GONE);
        addchange();
    }

    private void addchange() {
        new ControleValidacaoProdutoAdd(this) {
            @Override
            public void liberado(Double valor) {
                binding.precoTotal.setEnabled(true);
                binding.precoTotal.setText(new DecimalFormat("R$ #0.00").format(valor));
                binding.precoTotal.setOnClickListener((View) -> fechar());
            }

            @Override
            public void naoLiberado(Double valor) {
                binding.precoTotal.setEnabled(false);
                binding.precoTotal.setOnClickListener(null);
                binding.precoTotal.setText(new DecimalFormat("R$ #0.00").format(valor));
            }

            @Override
            public void mensagem(String msg) {
                mensagemRapida(msg);
            }
        }.validate(pedidoItem, acompanhamentoViews);
    }

    private void fechar(){
        new ControleGravarVendaItem(this) {
            @Override
            public void setVendaItemOK() {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void erroNoItem(String msg) {
                sair(msg) ;
            }
        }.gravarVendaItem(pedidoItem, acompanhamentoViews);
    }

    private void setMais(){
        pedidoItem.setQUANTIDADE(pedidoItem.getQUANTIDADE() + 1);
        pedidoItem.setVALOR(pedidoItem.getQUANTIDADE() * pedidoItem.getPRECO());
        display() ;
    }

    private void setMenos(){
        if (pedidoItem.getQUANTIDADE() <= 1) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_notification)
                    .setTitle("Atenção")
                    .setMessage("Deseja excluir o produto?")
                    .setCancelable(false)
                    .setNegativeButton("Não", (dialogInterface, i) -> {
                        display();
                    })
                    .setPositiveButton("Sim", (dialogInterface, i) -> {
                        finish();
                    })
                    .show();
        } else {
            pedidoItem.setQUANTIDADE(pedidoItem.getQUANTIDADE() - 1);
            pedidoItem.setVALOR(pedidoItem.getQUANTIDADE() * pedidoItem.getPRECO());
            display();
        }
    }

    private void mensagemRapida(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void sair(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    finish();
                })
                .show();
    }


}