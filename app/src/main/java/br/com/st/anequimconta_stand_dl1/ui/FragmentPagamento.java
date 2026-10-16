package br.com.st.anequimconta_stand_dl1.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;
import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.activity.ActivityPainelContas;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterContaPagamentos;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterModalidade;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoContaViewPagView;
import br.com.st.anequimconta_stand_dl1.controle.ControleCaixa;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaPagamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleGradeModalidade;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidosAnteriores;
import br.com.st.anequimconta_stand_dl1.controle.ControleRecebeValor;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleFechamentoContas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleModalidade;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControlePagamento;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentPagamentoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMult;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControleImpContaPagNFCeView;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControleImpContaPagView;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControleImpContaPagView;
import br.com.st.anequimconta_stand_dl1.pospag.ControlePosConected;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagConnect;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagType;
import br.com.st.anequimconta_stand_dl1.tipos.TipoModalidade;
import br.com.st.anequimconta_stand_dl1.util.ConfiguracoesOperacionais;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentPagamento extends Fragment implements OnControlePagamento, OnControleModalidade, OnControleFechamentoContas, OnControleImpContaPagView {

    private FragmentPagamentoBinding binding ;
    private ControleContaPagamento controleContaPagamento ;
    private Double valorPagamento = 0.0 ;
    private List<Conta> contaList ;
    private ProgressDialog progressDialog ;
    private ControleImpContaPagView controleImpContaPagView ;
    private ControleImpContaPagNFCeView controleImpContaPagNFCeView ;
    private boolean flag_iniciar = true ;

    public FragmentPagamento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("FP_", "onCreateView") ;
        binding = FragmentPagamentoBinding.inflate(inflater, container, false) ;
        binding.btAddPedido.setOnClickListener(view -> addPedido());
        binding.btAddPedido.setEnabled(true);
        binding.parcial.setOnClickListener(view -> valorParcial());

        controleImpContaPagView = new ControleImpContaPagView(this, this) ;
        controleImpContaPagNFCeView = new ControleImpContaPagNFCeView(this, this) ;
        controleContaPagamento = new ControleContaPagamento(this,this);

        AndroidThreeTen.init(getActivity()) ;
        contaList = new ArrayList<Conta>() ;
        return binding.getRoot() ;// inflater.inflate(R.layout.fragment_pagamento, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FP_", "onCreate") ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("FP_", "onViewCreated") ;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FP_", "onResume") ;
        Log.i("FP_", "onResume "+flag_iniciar) ;
        if (flag_iniciar) {
            flag_iniciar = false ;
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle("Consultando....");
            progressDialog.setCancelable(true);
            progressDialog.show();
            getCaixa();
        }
    }

    private void getCaixa(){
        new ControleCaixa(getActivity()) {
            @Override
            public void caixaAberto(Caixa c) {
                controleContaPagamento.setCaixa(c);
                iniciar();
            }

            @Override
            public void caixaFechado() {
                alertSair("Caixa Fechado!");
            }

            @Override
            public void caixaErro(String msg) {
                alertSair(msg);
            }
        }.getCaixaAberto(UtilSet.getUsuarioId(getActivity()));
    }

    private void iniciar(){
        binding.resumoPagamento.setText("");
        binding.pedidos.setText("Conta->");
        ControlePedidosAnteriores.getControlePedidosAnteriores().getNumeros().clear();
        new ControleGradeModalidade(getActivity(), this).executar();
        Log.i("FP_", "iniciou") ;
    }

    private void valorParcial() {
        controleContaPagamento.display();
        if (contaList.size() == 1) {
            new ControleRecebeValor(getActivity(), "Valor a ser pago") {
                @Override
                public void valorDigitado(Double v) {
                    if (v > 0) {
                        valorPagamento = v ;
                        binding.textValorApagar.setText(new DecimalFormat("R$ #0.00").format(valorPagamento));
                    } else alert("Valor Inválido!");
                }

                @Override
                public void erro(String msg) {
                    alert(msg);
                }
            };
        } else alert("Operação não permitida!") ;
    }

    @Override
    public void controleModalidadeOk(List<Modalidade> l) {
        progressDialog.dismiss();
     //   GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity()) ;
        binding.gradeModalidade.setLayoutManager(layoutManager);
        binding.gradeModalidade.setAdapter(new AdapterModalidade(getActivity(), l) {
            @Override
            public void selecionado(Modalidade m) {
                Log.i("FP_", "addModalidade "+m.getDescricao()) ;
                addModalidade(m) ;
            }
        });
    }

    @Override
    public void contasList(List<Conta> l) {
        progressDialog.dismiss();
        binding.btAddPedido.setEnabled(true);
        contaList = l ;
        String s = "";
        if (l.size() > 0) {
            for (Conta c : l) {
                String n = "Conta [" +c.getNUMERO()+"]" ;
                if (s.length() == 0)
                   s += n;
                else s += "\n"+n;
            }
            binding.pedidos.setText(s);
        } else
          binding.pedidos.setText("Conta->");
    }

    @Override
    public void pagamentoList(List<ContaPag> l) {
        Log.i("FP_", "pagamentoList l "+l.size()) ;
       // GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(), RecyclerView.VERTICAL, false) ;
        binding.gradePagamentos.setLayoutManager(layoutManager);
        binding.gradePagamentos.setAdapter(new AdapterContaPagamentos(getActivity(), l) {
            @Override
            public void cancelar(ContaPag canPag) {
                controleContaPagamento.cancelar(canPag) ;
            }
        });
    }

    @Override
    public void total(Double total, Double totalAPagar, Double totalPago) {
        Log.i("total_", "total "+ total) ;
        Log.i("total_", "totalAPagar "+ totalAPagar) ;
        Log.i("total_", "totalPago "+ totalPago) ;
        progressDialog.dismiss();
        binding.textValorApagar.setText(new DecimalFormat("R$ #0.00").format(totalAPagar));
        binding.resumoPagamento.setText("Pago "+new DecimalFormat("R$ #0.00").format(totalPago));
        valorPagamento = totalAPagar ;
        BigDecimal tot = new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN) ;
        BigDecimal tpag = new BigDecimal(totalPago).setScale(2, RoundingMode.HALF_EVEN) ;
        if ((tpag.compareTo(tot)  == 0) || (tpag.compareTo(tot)  == 1)){
            controleContaPagamento.fechar(this);
        }
    }

    @Override
    public void pagamentoErro(String msg) {
        progressDialog.dismiss();
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Erro:")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
    }

    private void addPedido() {
        Log.i("FP_", "addPedido") ;
        addpedido.launch(new Intent(getActivity(), ActivityPainelContas.class) );
    }

    ActivityResultLauncher addpedido = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                String uuid = result.getData().getStringExtra("CONTA_UUID");
                addconta(uuid) ;
            } else Toast.makeText(getActivity(), "Nenhuma Conta Selecionada!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getActivity(), "Nenhuma Conta Selecionada!", Toast.LENGTH_SHORT).show();
    });

    private void addconta(String uuid){
        progressDialog.show();
        try {
            if (contaList.size() > 0)
                throw new Exception("Conta já foi selecionada!") ;
            for (Conta c : contaList) {
                if (uuid == c.getUUID())
                    throw new Exception("Está conta já foi selecionada!") ;
            }
            controleContaPagamento.addContas(uuid);
        } catch (Exception e){
            e.printStackTrace();
            alert(e.getMessage());
        }
    }

    private void addModalidade(Modalidade m){
        progressDialog.show();
        controleContaPagamento.addPagamento(m, valorPagamento);
    }

    @Override
    public void controleModalidadeErro(String msg) {
        progressDialog.dismiss();
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Erro Modalidade:")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
    }

    private void alertSair(String msg) {
       progressDialog.dismiss();

       new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        voltar() ;
                    }
                })
                .show();

    }

    private void alert(String msg){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção:")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();

    }

    private void voltar(){
        requireActivity().onBackPressed();
    }

    @Override
    public void controleFechamentoContasOk(ContaMult contaMult) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        List<String> lm = new ArrayList<>() ;
        lm.add(contaMult.getUUID()) ;
        try {
            new ConexaoContaViewPagView(getActivity(), lm) {
                @Override
                public void contaViewPagViewOk(List<ContaViewPagView> l) {
                    if (l.size()>0){
                        if (ConfiguracoesOperacionais.getConfOperacional(getActivity(),ConfiguracoesOperacionais.operacao.pag_per))
                            setContaNfce(l.get(0)) ;
                        else if (ConfiguracoesOperacionais.getConfOperacional(getActivity(),ConfiguracoesOperacionais.operacao.pag_cnt))
                            contaNormal(l.get(0)) ;
                        else if (ConfiguracoesOperacionais.getConfOperacional(getActivity(),ConfiguracoesOperacionais.operacao.pag_nfc))
                            contaNFCe(l.get(0)) ;
                        else alertSair("Conta Fechada!");
                    } else  alertSair("Conta Não Encontrada!");
                }

                @Override
                public void contaViewPagViewErro(String msg) {
                    alertSair(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alertSair(e.getMessage());
        }
    }

    @Override
    public void controleFechamentoContasErro(String msg) {
        alertSair(msg);
    }

    private void setContaNfce(ContaViewPagView contaViewPagView){
        CharSequence options[] = new CharSequence[]{Html.fromHtml("<b>Simples Conferência</b>"),
                Html.fromHtml("<b>NFC-e</b>"),
                Html.fromHtml("<b>Sair</b>")};

        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Selecione:")
                .setItems(options, (dialogInterface, i) -> {
                    switch (i){
                        case 0 : contaNormal(contaViewPagView);
                            break;
                        case 1 : contaNFCe(contaViewPagView);
                            break;
                        default: voltar();
                    }
                })
                .show();
    }

    private void contaNormal(ContaViewPagView contaViewPagView){
        progressDialog.show();
        controleImpContaPagView.imprimir(contaViewPagView);
    }


    private void contaNFCe(ContaViewPagView contaViewPagView){
        progressDialog.show();
        controleImpContaPagNFCeView.imprimir(contaViewPagView);
    }

    @Override
    public void controleImpContaPagViewOk() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        alertSair("Impressão OK!");
      //  requireActivity().onBackPressed();
    }

    @Override
    public void controleImpContaPagViewErro(String msg) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        alertSair(msg);
    }
}