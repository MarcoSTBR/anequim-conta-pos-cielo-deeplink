package br.com.st.anequimconta_stand_dl1.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.activity.ActivityImprimirContas;
import br.com.st.anequimconta_stand_dl1.activity.ActivityPainelContas;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterContaViews;
import br.com.st.anequimconta_stand_dl1.controle.ControleContasAbertas;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoConta;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidosAnteriores;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContasAbertas;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentContaBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import java.util.List;

public class FragmentConta extends Fragment implements OnControleContasAbertas {

    private FragmentContaBinding binding ;
    private ControleContasAbertas controleContasAbertas ;

    public FragmentConta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContaBinding.inflate(inflater, container, false) ;
        // Inflate the layout for this fragment
        binding.btAddPedido.setOnClickListener(view -> addPedido());
        controleContasAbertas = new ControleContasAbertas(getActivity(), this) ;
        Log.i("fragContas", "onCreateView") ;
        return binding.getRoot() ;// inflater.inflate(R.layout.fragment_conta, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ControlePedidosAnteriores.getControlePedidosAnteriores().getNumeros().size() > 0)
            contasEnviadas() ;
    }

    private void contasEnviadas() {
       new ControlePedidoConta(getActivity()) {
        @Override
        public void contas(List<Conta> l) {
          for (Conta c :l)
            controleContasAbertas.addContas(c.getUUID());
           ControlePedidosAnteriores.getControlePedidosAnteriores().getNumeros().clear();
          }
       }.getContas(ControlePedidosAnteriores.getControlePedidosAnteriores().getNumeros());
    }

    private void addPedido() {
        Intent intent = new Intent(getActivity(), ActivityPainelContas.class) ;
        addpedido.launch(intent);
        //  startActivity(new Intent(getActivity(), ActivityAddPedido.class));
    }

    ActivityResultLauncher addpedido = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                String uuid = result.getData().getStringExtra("CONTA_UUID");
                controleContasAbertas.addContas(uuid);
            } else Toast.makeText(getActivity(), "Nenhuma Conta Selecionada!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getActivity(), "Nenhuma Conta Selecionada!", Toast.LENGTH_SHORT).show();
    });


    public void contasView(List<ContaView> l) {
        String s = "" ;
        for (ContaView c: l){
            String ref =  (c.getConta().getNUMERO().equals(c.getConta().getREFERENCIA())) ? "" : "["+c.getConta().getREFERENCIA()+"]" ;
            s = "" ;
            if (s.length() == 0)
                s += c.getConta().getNUMERO() + ref;
             else s += "\n"+c.getConta().getNUMERO() + ref ;
        }
        if (s.length() > 0)
        binding.pedidos.setText(s);
        else binding.pedidos.setText("Conta->");
    }

    @Override
    public void contaViewOk(List<ContaView> l) {
        contasView(l) ;
        binding.gradeContasBitmap.setAdapter(new AdapterContaViews(getActivity(), l) {
            @Override
            public void imprimir(ContaView contaView) {
                chamarImpressao(contaView) ;
            }

            @Override
            public void excluir(ContaView contaView) {
                controleContasAbertas.remove(contaView) ;
            }
        });
    }

    private void chamarImpressao(ContaView contaView) {
        Intent intent = new Intent(getActivity(), ActivityImprimirContas.class) ;
        Bundle bundle = new Bundle() ;
        bundle.putString("CONTA_UUID", contaView.getConta().getUUID());
        intent.putExtras(bundle) ;
        addImpressao.launch(intent);
    }

    ActivityResultLauncher addImpressao = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result-> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                String uuid = result.getData().getStringExtra("CONTA_UUID");
                //controleContasAbertas.addContas(uuid);
                controleContasAbertas.refress();
            } else Toast.makeText(getActivity(), "Nenhuma Conta Selecionada!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getActivity(), "Nenhuma Conta Selecionada!", Toast.LENGTH_SHORT).show();
    });


    @Override
    public void contaViewErro(String msg) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Erro")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .show();
    }
}