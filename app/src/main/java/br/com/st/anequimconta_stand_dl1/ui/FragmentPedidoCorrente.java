package br.com.st.anequimconta_stand_dl1.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.activity.ActivityAddPedido;
import br.com.st.anequimconta_stand_dl1.activity.ActivityEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.activity.ActivityPainelContas;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterPedidoCorrente;
import br.com.st.anequimconta_stand_dl1.adapter.listeners.ListenerAdapterPedidoCorrente;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoView;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidos;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControlePedidoAdd;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControlePedidoApagar;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControlePedidoCorrente;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentPedidoCorrenteBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Pedido;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;
import br.com.st.anequimconta_stand_dl1.util.ConfiguracoesOperacionais;

public class FragmentPedidoCorrente extends Fragment implements ListenerAdapterPedidoCorrente, ListenerControlePedidoAdd {

    private FragmentPedidoCorrenteBinding binding;

    public FragmentPedidoCorrente(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPedidoCorrenteBinding.inflate(inflater, container, false);
        binding.btAddPedido.setOnClickListener(view -> addPedido());
        binding.BtEnviar.setOnClickListener(view -> enviar());
        binding.btAddConta.setOnClickListener(view -> addPedidoAberto());
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        grade() ;
    }

    private void grade() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1) ;
        binding.gradePedidos.setLayoutManager(layoutManager);
        binding.gradePedidos.setAdapter(new AdapterPedidoCorrente(getActivity(), new ControlePedidoView(getActivity()).getPedidos(), this));
    }

    private void addPedido() {
        Intent intent = new Intent(getActivity(), ActivityAddPedido.class) ;
        addpedido.launch(intent);
      //  startActivity(new Intent(getActivity(), ActivityAddPedido.class));
    }

    ActivityResultLauncher addpedido = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                String num = result.getData().getStringExtra("NUMERO");
                setAddNumeroConta(num) ;
            } else alert("Número sem retorno!") ;
        } else grade();
    });

    private void setAddNumeroConta(String numero){
        new ControlePedidos(getActivity()).addPedido(numero, numero, this);
    }


    private void addPedidoAberto() {
        Intent intent = new Intent(getActivity(), ActivityPainelContas.class) ;
        addpedido.launch(intent);
        //  startActivity(new Intent(getActivity(), ActivityAddPedido.class));
    }

    private void enviar() {
        addEnv.launch (new Intent(getActivity(), ActivityEnvioPedidos.class));
    }

    ActivityResultLauncher addEnv = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            enviou() ;

        }
    });

    private void enviou(){
        if (ConfiguracoesOperacionais.getConfOperacional(getContext(), ConfiguracoesOperacionais.operacao.env_imp))
            irparaImpressao() ;
        else if (ConfiguracoesOperacionais.getConfOperacional(getContext(), ConfiguracoesOperacionais.operacao.env_pag))
            irparaPagamento() ;
    }


    private void irparaVenda(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_pedido_corrente_to_nav_pedido_vendas) ;
    }

    private void irparaImpressao(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_pedido_corrente_to_nav_conta) ;
    }

    private void irparaPagamento(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_pedido_corrente_to_nav_pagamento) ;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void pedidoSelecionado(PedidoView pedidoView) {
        new ControlePedidos(getActivity()).setCorrente(pedidoView.getPedido(), new ListenerControlePedidoCorrente() {
            @Override
            public void pedidoCorrenteOk(Pedido pedido) {
                irparaVenda() ;
            }

            @Override
            public void pedidoErro(String msg) {
                alert(msg);
            }
        });
    }

    @Override
    public void apagarPedido(PedidoView pedidoView) {
        new ControlePedidos(getActivity()).apagarPedido(pedidoView.getPedido().getUUID(), new ListenerControlePedidoApagar() {
            @Override
            public void pedidoApagado(int quantidadeRestante) {
                grade();
            }

            @Override
            public void pedidoApagarErro(String msg) {
                alert(msg);
            }
        });

    }

    private void alert(String msg){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Erro")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
    }

    @Override
    public void pedidoOk(Pedido pedido) {
        if (ConfiguracoesOperacionais.getConfOperacional(getContext(), ConfiguracoesOperacionais.operacao.num_prd))
           irparaVenda() ;
    }

    @Override
    public void pedidoErro(String msg) {
        alert(msg);
    }
}