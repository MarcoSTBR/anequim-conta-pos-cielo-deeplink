package br.com.st.anequimconta_stand_dl1.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.activity.ActivityEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.activity.ActivityProdutoAdd;
import br.com.st.anequimconta_stand_dl1.adapter.listeners.ListenerAdapterProduto;
import br.com.st.anequimconta_stand_dl1.controle.ControleGradeView;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoView;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidosAnteriores;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentPedidoVendasBinding;
import br.com.st.anequimconta_stand_dl1.entidade.GradeView;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;
import br.com.st.anequimconta_stand_dl1.util.ConfiguracoesOperacionais;

import java.util.List;

public class FragmentPedidoVendas extends Fragment implements ListenerAdapterProduto {

    private FragmentPedidoVendasBinding binding;

    public FragmentPedidoVendas(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPedidoVendasBinding.inflate(inflater, container, false);
        binding.BtIrCarrinho.setOnClickListener(view -> irCarrinho());
        binding.BtVoltar.setOnClickListener(view -> voltar());
        binding.BtEnviar.setOnClickListener((view -> enviar()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carregarTela();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragmentCarrinho", "ooo") ;

        if ((ControlePedidosAnteriores.getControlePedidosAnteriores().getNumeros().size() == 0) &&
                (new ControlePedidoView(getActivity()).getPedidos().size() == 0))
            new AlertDialog.Builder(getContext())
                    .setIcon(R.drawable.ic_notification)
                    .setTitle("Atenção")
                    .setMessage("Nenhum Pedido Aberto!")
                    .setCancelable(false)
                    .setPositiveButton("OK", (i,e)->voltar())
                    .show();

    }

    private void voltar() {
        requireActivity().onBackPressed();
    }


    private void irCarrinho() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_pedido_vendas_to_nav_carrinho) ;
    }


    private void carregarTela(){
        Log.i("carregarTela", "carregarTela") ;
        displayPedido() ;
        List<GradeView> listGrade = ControleGradeView.getControleGradeView(getActivity()).getList();
        binding.fragViewPagerCarrinho.setAdapter(new FragAdapter(FragmentPedidoVendas.this, listGrade));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayoutCarrinho, binding.fragViewPagerCarrinho, (tab, position) ->{
            tab.setText(listGrade.get(position).getGrade().getDescricao()) ;
        }) ;
        tabLayoutMediator.attach();
        binding.BtEnviar.setOnClickListener(view1 -> enviar());
    }

    private void enviar() {
        redicecionaEnvio.launch(new Intent(getContext(), ActivityEnvioPedidos.class));
    }

    ActivityResultLauncher<Intent> redicecionaEnvio = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                  enviou();
                }
            }
    );

    private void enviou(){
        if (ControlePedidosAnteriores.getControlePedidosAnteriores().getNumeros().size() > 0) {
            if (ConfiguracoesOperacionais.getConfOperacional(getContext(), ConfiguracoesOperacionais.operacao.env_imp))
                irparaImpressao();
            else if (ConfiguracoesOperacionais.getConfOperacional(getContext(), ConfiguracoesOperacionais.operacao.env_pag))
                irparaPagamento();
            else enviouVoltar();
        } else voltar();
    }

    private void enviouVoltar(){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage("Pedido Enviado!")
                .setCancelable(false)
                .setPositiveButton("OK", (i,e)->voltar())
                .show();

    }

    private void irparaImpressao(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_pedido_vendas_to_nav_conta) ;
    }

    private void irparaPagamento(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_pedido_vendas_to_nav_pagamento) ;
    }

    private void displayPedido(){
        binding.numeroPedido.setText("Nenhum Pedido Aberto!");
        PedidoView pedidoView = new ControlePedidoView(getActivity()).getCorrente();
        if (pedidoView != null){
            String s = pedidoView.getPedido().getNUMERO() ;
            if (!s.equals(pedidoView.getPedido().getREFERENCIA()))
                s += " ["+pedidoView.getPedido().getREFERENCIA()+"]" ;
            binding.numeroPedido.setText(s);
        }
    }

    @Override
    public void OnAddProduto(Produto produto) {
        Toast.makeText(getContext(), "OnAddProduto", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), ActivityProdutoAdd.class);
        i.putExtra("PRODUTO_ID", produto.getId());
        redicecionaParaCarrinho.launch(i);
    }

    ActivityResultLauncher<Intent> redicecionaParaCarrinho = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                   irParaPedido() ;
                }
            }
    );

    private void irParaPedido(){


    }


    @Override
    public void OnChange(Produto produto) {
     //   ControleBadgeView.getControleBadgeView(getActivity()).executar();
    }

    @Override
    public void OnProdutoErro(String msg) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
    }


    private class FragAdapter extends FragmentStateAdapter {

        private List<GradeView> list ;

        public FragAdapter(@NonNull Fragment fragment, List<GradeView> list) {
            super(fragment);
            this.list = list ;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return FragmentGradeProduto.newInstance(list.get(position).getGrade().getId());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}