package br.com.st.anequimconta_stand_dl1.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.activity.ActivityEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterGradeCarrinho;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoView;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidosAnteriores;
import br.com.st.anequimconta_stand_dl1.controle.ControleProdutoVendaCodigo;
import br.com.st.anequimconta_stand_dl1.controle.ControleVendaViewEdit;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleVendaCodigo;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentCarrinhoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;
import br.com.st.anequimconta_stand_dl1.util.ConfiguracoesOperacionais;

public class FragmentCarrinho extends Fragment {

    private FragmentCarrinhoBinding binding;
    private PedidoView pedidoView ;

    public FragmentCarrinho(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarrinhoBinding.inflate(inflater, container, false);
        binding.BtEnviar.setOnClickListener(view -> enviar() );
        binding.BtVoltar.setOnClickListener(view -> voltar() );
        binding.codbarras.setVisibility(View.GONE);
        binding.escreverCodigoBarras.setOnClickListener(v -> {
            if (binding.codbarras.getVisibility() == View.VISIBLE){
                binding.codbarras.setVisibility(View.GONE);
                fecharTeclado() ;
            } else {
                binding.codbarras.setVisibility(View.VISIBLE);
                binding.codbarras.setText("");
                binding.codbarras.setHint("Digite o Código");
                binding.codbarras.requestFocus();
                abrirTeclado() ;
            }
        });
        binding.codbarras.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                    fecharTeclado();
                    entrandoCodigo();
                    return true;
                }

            }
            return false;
        });
        binding.lerCodigoBarras.setOnClickListener(v -> lerCodBarras());

        return binding.getRoot();
    }

    private void voltar() {
     //   requireActivity().addOnBackPressedDispatcher()
        requireActivity().onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.numeroPedido.setText("Nenhum Pedido Aberto!");
        atualizaPedido() ;
    }

    private void atualizaPedido(){
        pedidoView = new ControlePedidoView(getActivity()).getCorrente() ;
        if (pedidoView != null) {
            grade();
        } else enviou();

    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    entrandoCodigo(result.getContents()) ;
                    // Handle success
                }
            });

    public void lerCodBarras() {
        barcodeLauncher.launch(new ScanOptions());
    }

    private void abrirTeclado(){
        binding.codbarras.requestFocus() ;
        InputMethodManager i = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE) ;
        i.showSoftInput(binding.codbarras, InputMethodManager.SHOW_FORCED);
    }

    private void fecharTeclado(){
        binding.codbarras.requestFocus() ;
        InputMethodManager i = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE) ;
        i.hideSoftInputFromWindow(getView().getWindowToken(), 0) ;
    }

    private void entrandoCodigo(){
        if (binding.codbarras.getText().length() > 0){
            entrandoCodigo(binding.codbarras.getText().toString()) ;
            binding.codbarras.setText("");
        }
    }

    private void entrandoCodigo(String cod){
        if (cod.length() > 0){
            new ControleProdutoVendaCodigo(getActivity(), new ListenerControleVendaCodigo() {
                @Override
                public void vendaCodigoOk() {
                    atualizaPedido();
                }

                @Override
                public void vendaCodigoErro(String msg) {
                    alert(msg);
                }
            }).setCodigo(pedidoView, cod);
        }
    }

    private void enviar() {
        Intent intent = new Intent(getContext(), ActivityEnvioPedidos.class) ;
        redicecionaEnvio.launch(intent);

    }

    ActivityResultLauncher<Intent> redicecionaEnvio = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    voltarParaPedidoCorrente() ;
                }
            }
    );

    private void enviou(){
        if (ControlePedidosAnteriores.getControlePedidosAnteriores().getNumeros().size() > 0) {
            if (ConfiguracoesOperacionais.getConfOperacional(getContext(), ConfiguracoesOperacionais.operacao.env_imp))
                irparaImpressao();
            else if (ConfiguracoesOperacionais.getConfOperacional(getContext(), ConfiguracoesOperacionais.operacao.env_pag))
                irparaPagamento();
        } else  requireActivity().onBackPressed();
    }

    private void irparaImpressao(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_carrinho_to_nav_conta) ;
    }

    private void irparaPagamento(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_carrinho_to_nav_pagamento) ;
    }

    private void voltarParaPedidoCorrente() {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage("Pedido Enviado!")
                .setCancelable(false)
                .setPositiveButton("OK", (d, i)->{
                    requireActivity().onBackPressed();

                })
                .show();
    }

    private void displayPedido(){
        String s = String.valueOf(pedidoView.getPedido().getNUMERO()) ;
        if (!s.equals(pedidoView.getPedido().getREFERENCIA()))
            s += "["+pedidoView.getPedido().getREFERENCIA()+"]" ;
        binding.numeroPedido.setText(s);
    }

    private void grade(){
        displayPedido() ;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        binding.gradeItemVenda.setLayoutManager(layoutManager);
        binding.gradeItemVenda.setAdapter(new AdapterGradeCarrinho(getContext(), pedidoView.getPedidoItemView()) {
            @Override
            public void onChange(PedidoItemView it) {
                alterarItem(it);
            }

            @Override
            public void excluirItem(PedidoItemView it) {
                excluirItemView(it);
            }
        });
    }

    private void excluirItemView(PedidoItemView it) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Exclusão")
                .setMessage("Deseja excluir o Item?")
                .setCancelable(false)
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", (dialogInterface, i) -> {
                    excluirItemCommit(it);
                })
                .show();
    }

    private void excluirItemCommit(PedidoItemView it) {
        new ControleVendaViewEdit(getActivity()) {
            @Override
            public void ok() {
                atualizaPedido() ;
            }

            @Override
            public void erro(String msg) {
                alert(msg);

            }
        }.cancelar(it);
    }

    private void alterarItem(PedidoItemView it) {
        new ControleVendaViewEdit(getActivity()) {
            @Override
            public void ok() {
                atualizaPedido() ;
            }

            @Override
            public void erro(String msg) {
                alert(msg);
            }
        }.alterarQuantidade(it);
    }

    private void alert(String msg) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                   // irParaVenda();
                })
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}