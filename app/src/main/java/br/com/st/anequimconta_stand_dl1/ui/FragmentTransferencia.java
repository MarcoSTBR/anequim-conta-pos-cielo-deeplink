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
import br.com.st.anequimconta_stand_dl1.activity.ActivityAddPedido;
import br.com.st.anequimconta_stand_dl1.activity.ActivityPainelContas;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterContaItemTransferir;
import br.com.st.anequimconta_stand_dl1.controle.ControleConta;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaView;
import br.com.st.anequimconta_stand_dl1.controle.ControleContaItemTransferencia;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnContaItemTransferencia;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleConta;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContaView;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentTransferenciaBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemTransferenciaView;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemView;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.util.ArrayList;
import java.util.List;

public class FragmentTransferencia extends Fragment implements OnControleContaView, OnContaItemTransferencia {


    private FragmentTransferenciaBinding binding ;
    private String uuid ;
    private ContaView contaView ;
    private String numero_origem = null ;

    public FragmentTransferencia() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransferenciaBinding.inflate(inflater, container, false) ;
        binding.BtVoltar.setOnClickListener(view -> voltar());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.selecionar.setOnClickListener(view1->selectionarConta());
        binding.selecionarTrans.setOnClickListener(view1->addPedido());
        binding.numero.setText("De Conta ->");
        binding.numeroTrans.setText("Para Conta ->");
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

    private void addPedido() {
        Intent intent = new Intent(getActivity(), ActivityAddPedido.class) ;
        addpedido.launch(intent);
        //  startActivity(new Intent(getActivity(), ActivityAddPedido.class));
    }

    ActivityResultLauncher addpedido = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                numero_origem = result.getData().getStringExtra("NUMERO");
                binding.numeroTrans.setText(numero_origem);
            } else alert("Numero sem retorno!") ;
        } else alert("Confirme o número da transferência!");
    });

    private void zerar(){
        numero_origem = null ;
        binding.numero.setText("Da Conta ->");
        binding.numeroTrans.setText("Para Conta ->");
        binding.gradeItemVenda.setVisibility(View.GONE);
    }

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
        List<ContaItemTransferenciaView> lc = new ArrayList<>() ;
        for (ContaItemView c : l)
            lc.add(new ContaItemTransferenciaView(c, 0.0)) ;
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

    private void voltar(){
        requireActivity().onBackPressed();
    }

    @Override
    public void controleContaViewErro(String msg) {
        alert(msg);
    }

    private void grade(List<ContaItemTransferenciaView> l){
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1) ;
        binding.gradeItemVenda.setVisibility(View.VISIBLE);
        binding.gradeItemVenda.setLayoutManager(layoutManager);
        binding.gradeItemVenda.setAdapter(new AdapterContaItemTransferir(getActivity(), l) {
            @Override
            public void transferencia(ContaItemTransferenciaView contaItemTransferenciaView) {
                transferir(contaItemTransferenciaView) ;
            }
        });
    }

    private void transferir(ContaItemTransferenciaView contaItemTransferenciaView) {
        new ControleContaItemTransferencia(getActivity(), this ).transferenciaItem(contaItemTransferenciaView, contaView.getConta().getNUMERO(), numero_origem);
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
    public void contaItemTransferenciaOk() {
        getContaView() ;
    }

    @Override
    public void contaItemTransferenciaErro(String msg) {
        alert(msg);
    }
}