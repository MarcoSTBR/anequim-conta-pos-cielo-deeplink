package br.com.st.anequimconta_stand_dl1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaEdit;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaView;
import br.com.st.anequimconta_stand_dl1.controle.ControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.controle.ControleRecebeValor;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaView;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityImprimirContasBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;
import br.com.st.anequimconta_stand_dl1.impressao.ContaViewBitMap;
import br.com.st.anequimconta_stand_dl1.impressao.ImpressaoBitmapBuilder;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControleTotaisContaView;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;
import br.com.st.anequimconta_stand_dl1.impressao.util.ImpressaoUtil;
import br.com.st.anequimconta_stand_dl1.impressao.util.UtilContaParam;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;

import java.net.MalformedURLException;

public class ActivityImprimirContas extends AppCompatActivity implements OnControleContaView, ListenerImpressaoBipMap {

    private ActivityImprimirContasBinding binding ;
    private String uuid ;
    private int num_pessoa ;
    private ContaView contaView ;
    private int usuario_aut_id ;
    private OnImpressaoBitMap onImpressaoBitMap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImprimirContasBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot() /*R.layout.activity_imprimir_contas*/);
        uuid     = getIntent().getStringExtra("CONTA_UUID") ;
        binding.contaNormal.setOnClickListener(view -> contanormal());
        binding.contaSemComissao.setOnClickListener(view -> contaSemComissao());
        binding.contaDesconto.setOnClickListener(view -> contaNormalComDesconto());
        binding.contaSemComisaoDesconto.setOnClickListener(view -> contaSemComisaoDesconto());
        binding.imprimirSair.setOnClickListener(view->finish());
        binding.imprimirPessoasMenos.setOnClickListener(view -> {
            if (num_pessoa < 1)
                alert("Número de pessoa inválido");
             else {
                num_pessoa-- ;
                binding.imprimirNumPessoas.setText(String.valueOf(num_pessoa));
            }
        });
        binding.imprimirPessoasMais.setOnClickListener(view -> {
                num_pessoa++ ;
                binding.imprimirNumPessoas.setText(String.valueOf(num_pessoa));
        });
        onImpressaoBitMap = new ImpressaoBitmapBuilder(this, this)
                .getImpressao(this) ;
    }

    private void contanormal() {
        // EMITIR CONTA
        usuario_aut_id = UtilSet.getUsuarioId(this) ;
        new ControleLiberacaoAcesso(this,  new ListenerControleLiberacaoAcesso() {
            @Override
            public void acessoOk(int usuario_id) {
                imprimirconta(1, 0.00);
            }

            @Override
            public void acessoErro(String msg) {
                alert(msg);
            }
        }).acesso(23);
    }

    private void contaSemComissao() {
        usuario_aut_id = UtilSet.getUsuarioId(this) ;
        new ControleLiberacaoAcesso(this,  new ListenerControleLiberacaoAcesso() {
            @Override
            public void acessoOk(int usuario_id) {
                imprimirconta(0, 0.00);
            }

            @Override
            public void acessoErro(String msg) {
                alert(msg);
            }
        }).acesso(17);
    }

    private void contaNormalComDesconto() {
        usuario_aut_id = UtilSet.getUsuarioId(this) ;
        new ControleLiberacaoAcesso(this,  new ListenerControleLiberacaoAcesso() {
            @Override
            public void acessoOk(int usuario_id) {
                new ControleRecebeValor(ActivityImprimirContas.this, "Desconto: ") {
                    @Override
                    public void valorDigitado(Double v) {
                        imprimirconta(1, v);
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                };
            }

            @Override
            public void acessoErro(String msg) {
                alert(msg);
            }
        }).acesso(21);

    }

    private void contaSemComisaoDesconto() {
        usuario_aut_id = UtilSet.getUsuarioId(this) ;
        new ControleLiberacaoAcesso(this, new ListenerControleLiberacaoAcesso() {
            @Override
            public void acessoOk(int usuario_id) {
                new ControleRecebeValor(ActivityImprimirContas.this, "Desconto: ") {
                    @Override
                    public void valorDigitado(Double v) {
                        imprimirconta(0, v);
                    }

                    @Override
                    public void erro(String msg) {
                        alert(msg);
                    }
                };
            }

            @Override
            public void acessoErro(String msg) {
                alert(msg);
            }
        }).acesso(24);

    }

    private void imprimirconta(int statusComissao, Double desconto){
        try
        {
            ControleTotaisContaView t = new ControleTotaisContaView(this, contaView) ;
            if (desconto > t.getTotaItens()) throw new Exception("Valor de Desconto Inválido!") ;
            if ((desconto > 0 ) && (ModalidadeDao.getModalidadeDao(this).getModalidadeDesconto() == null)) throw new Exception("Desconto Não Autorizado!") ;
            if (contaView.getConta().getNUM_IMPRESSAO() > 0) {
                // REIMPRESSAO
                new ControleLiberacaoAcesso(this, new ListenerControleLiberacaoAcesso() {
                    @Override
                    public void acessoOk(int usuario_id) {
                        imprimirBitrMap(statusComissao, desconto) ;
                    }

                    @Override
                    public void acessoErro(String msg) {
                        alert(msg);
                    }
                }).acesso(19);
            } else imprimirBitrMap(statusComissao, desconto);
        } catch (Exception e) {
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void imprimirBitrMap(int statusComissao, Double desconto){
        contaView.getConta().setNUM_PESSOAS(num_pessoa);
        contaView.getConta().setNUM_IMPRESSAO(contaView.getConta().getNUM_IMPRESSAO()+1);
        contaView.getConta().setSTATUS_COMISSAO(statusComissao) ;
        contaView.getConta().setDESCONTO(desconto);
        contaView.getConta().setTRAVA(1);
        onImpressaoBitMap.imprimirBitMap(new ContaViewBitMap(this, contaView)
                .getBitMap(UtilContaParam.getlargMaxConta(this), UtilContaParam.getLinhaNomalConta(this)));
    }

    private void sair(){
        Intent intent = new Intent() ;
        intent.putExtra("CONTA_UUID", uuid) ;
        setResult(RESULT_OK, intent);
        finish();
    }

    private void gravaWeb(){
        try {
            new ConexaoContaEdit(this, contaView.getConta()) {
                @Override
                public void contasOk(Conta conta) {
                    sair();
                }

                @Override
                public void contasErro(String msg) {
                    alert(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert(e.getMessage());
        } catch (JSONException e) {
            alert(e.getMessage());
        }

    }

    private void alert(String msg) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ControleContaView(this, this).getContaView(uuid);
    }

    @Override
    public void controleContaViewOk(ContaView contaView) {
        this.contaView = contaView ;
        Log.i("contaView", "numero "+ contaView.getConta().getNUM_PESSOAS()) ;
        num_pessoa = contaView.getConta().getNUM_PESSOAS() ;
        binding.imprimirNumPessoas.setText(String.valueOf(num_pessoa));
    }

    @Override
    public void controleContaViewErro(String msg) {
        alert(msg);
    }

    @Override
    public void imprimirBitMapOk() {
        gravaWeb();
    }

    @Override
    public void imprimirBitMapErro(String msg) {
        alert(msg);
    }
}