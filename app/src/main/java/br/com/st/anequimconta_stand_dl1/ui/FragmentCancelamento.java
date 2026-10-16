package br.com.st.anequimconta_stand_dl1.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.activity.ActivityPainelContas;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterContaCancelamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleConta;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaItemCancelamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaValidacao;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaView;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnContaItemCancelamento;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleConta;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaValidacao;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaView;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentCancelamentoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaCancelamentoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemView;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.util.ArrayList;
import java.util.List;

public class FragmentCancelamento extends Fragment implements OnControleContaView, OnContaItemCancelamento {

    private FragmentCancelamentoBinding binding ;
    private String uuid ;
    private ContaView contaView ;
    public FragmentCancelamento() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCancelamentoBinding.inflate(inflater, container, false) ;
        binding.BtVoltar.setOnClickListener(view->voltar());
        return binding.getRoot();
    }

    private void voltar() {
        requireActivity().onBackPressed();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.selecionar.setOnClickListener(view1->selectionarConta());
        binding.numero.setText("Selecione ->");
    }

    private void selectionarConta() {
        Intent intent = new Intent(getContext(), ActivityPainelContas.class) ;
        intent.putExtra("TITULO", "Contas Abertas!") ;
        painelConta.launch(intent);
    }

    ActivityResultLauncher<Intent> painelConta = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        uuid = result.getData().getStringExtra("CONTA_UUID");
                        getContaView() ;
                    } else {
                        zerar() ;
                    }
                    // direcionar para carrinho
                    // irParaCarrinho() ;
                }
            }
    );

    private void getContaView() {
        new ControleContaView(getActivity(), this).getContaView(uuid) ;
    }

    @Override
    public void controleContaViewOk(ContaView contaView) {
        this.contaView = contaView ;
        binding.numero.setText(contaView.getConta().getNUMERO());
        if (contaView.getContaItemViews().size() == 0)
            excluirConta(contaView);
          else gradeView(contaView.getContaItemViews());
    }

    private void gradeView(List<ContaItemView> l){
        List<ContaCancelamentoItemView> lc = new ArrayList<>() ;
        for(ContaItemView ci : l)
            lc.add(new ContaCancelamentoItemView(ci, 0.0)) ;
        grade(lc);
    }

    private void excluirConta(ContaView contaView){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage("Não há mais item nesta conta, deseja cancelar?")
                .setCancelable(false)
                .setPositiveButton("Sim", (dialogInterface, i) -> {
                    new ControleConta(getActivity(), new OnControleConta() {
                        @Override
                        public void contasOk(List<Conta> l) {
                            voltar();
                        }

                        @Override
                        public void contaErro(String msg) {
                            alert(msg);
                        }
                    }).cancelarConta(contaView.getConta()) ;
                })
                .setNegativeButton("Não", (dialogInterface, i) -> {
                    voltar();
                })
                .show();

    }


    private void grade(List<ContaCancelamentoItemView> l){
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1) ;
        binding.gradeItemVenda.setVisibility(View.VISIBLE);
        binding.gradeItemVenda.setLayoutManager(layoutManager);
        binding.gradeItemVenda.setAdapter(new AdapterContaCancelamento(getActivity(), l) {
            @Override
            public void cancelar(ContaCancelamentoItemView contaCancelamentoItemView) {
                cancelarItem(contaCancelamentoItemView) ;
            }
        });
        if (l.size() == 0) seCancela() ;
    }

    private void seCancela() {
        new ControleContaValidacao(getActivity(), new OnControleContaValidacao() {
            @Override
            public void contaValidOk() {

            }

            @Override
            public void contaValidFechada() {
                zerar();
            }

            @Override
            public void contaValidErro(String msg) {
                alert(msg);

            }
        }).validarConta(contaView);

    }

    private void cancelarItem(ContaCancelamentoItemView contaCancelamentoItemView) {
        new ControleContaItemCancelamento(getActivity(), this ).cancelarItem(contaView, contaCancelamentoItemView);
    }

    @Override
    public void controleContaViewErro(String msg) {
        alert(msg);
    }

    private void zerar(){
        binding.numero.setText("Selecione ->");
        binding.gradeItemVenda.setVisibility(View.GONE);
    }

    private void alert(String msg) {
        zerar() ;
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                   // getContaView() ;
                })
                .show();
    }

    @Override
    public void contaItemCancelamentoOk() {
        getContaView() ;
    }

    @Override
    public void contaItemCancelamentoErro(String msg) {
        alert(msg);

    }
}