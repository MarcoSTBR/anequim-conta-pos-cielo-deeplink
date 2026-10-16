package br.com.st.anequimconta_stand_dl1.ui;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.controle.ControleCaixa;
import br.com.st.anequimconta_stand_dl1.controle.ControleFecharCaixa;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerImpRelatorioFechamento;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentCaixaBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.impressao.ImpressaoBitmapBuilder;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControleImpRelatorioFechamento;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class FragmentCaixa extends Fragment implements ListenerImpressaoBipMap {

    private FragmentCaixaBinding binding ;
    private Caixa caixa ;
    private Bitmap bitmap ;
    private OnImpressaoBitMap onImpressaoBitMap ;

    public FragmentCaixa() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCaixaBinding.inflate(inflater, container, false) ;
        binding.btVoltar.setOnClickListener(view-> voltar());
      //  onImpressaoBitMap = new ImpressaoBitmapBuilder(getActivity(), this).getImpressao(FragmentCaixa.this) ;
        onImpressaoBitMap = new ImpressaoBitmapBuilder(getActivity(), this).getImpressao(FragmentCaixa.this) ;
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        configurarTela() ;
    }

    private void configurarTela() {
        new ControleCaixa(getActivity()) {
            @Override
            public void caixaAberto(Caixa c) {
                caixa = c;
                displayCaixaAberto();
            }

            @Override
            public void caixaFechado() {
                displayCaixaFechado();
            }

            @Override
            public void caixaErro(String msg) {
                alert(msg);
            }
        }.getCaixaAberto(UtilSet.getUsuarioId(getContext()));
    }

    private void displayCaixaAberto() {
        binding.btAbrirFechar.setText("Fechar o Caixa");
        binding.btAbrirFechar.setOnClickListener(v -> fecharCaixa());
        binding.painelCabecalho.setText("* Demonstrativo do Caixa *");
        binding.painelCorpo.setVisibility(View.GONE);

        new ControleImpRelatorioFechamento(getActivity(), new ListenerImpRelatorioFechamento() {
            @Override
            public void ImpRelatorioFechamentoOk(Bitmap bit) {
                bitmap = bit ;
                binding.imagemDemonstrativo.setImageBitmap(bitmap);
                binding.btImprimir.setOnClickListener((View) -> imprimir());
                binding.btImprimir.setVisibility(View.VISIBLE);
            }

            @Override
            public void ImpRelatorioFechamentoErro(String msg) {
                alert(msg);
            }
        }).getBitmap(caixa) ;
    }

    private void displayCaixaFechado() {
        binding.btAbrirFechar.setText("Abrir o Caixa");
        binding.btAbrirFechar.setOnClickListener((View) -> abrirCaixaValor());
        binding.imagemDemonstrativo.setVisibility(View.GONE);
        binding.btImprimir.setOnClickListener((View) -> alert("Nenhuma impressão disponivel!"));
        binding.btImprimir.setVisibility(View.GONE);
        binding.painelCorpo.setVisibility(View.GONE);
        binding.painelCorpo.setText("");
    }

    private void imprimir() {
        new ControleImpRelatorioFechamento(getActivity(), new ListenerImpRelatorioFechamento() {
            @Override
            public void ImpRelatorioFechamentoOk(Bitmap bit) {
                bitmap = bit ;
                onImpressaoBitMap.imprimirBitMap(bitmap);
            }

            @Override
            public void ImpRelatorioFechamentoErro(String msg) {
                alert(msg);
            }
        }).getBitmap(caixa, 350, 20) ;
       // onImpressaoBitMap.imprimirBitMap(bitmap);
    }


    private void abrirCaixaValor() {
        new ControleCaixa(getActivity()) {
            @Override
            public void caixaAberto(Caixa c) {
                caixaOK(c);
            }

            @Override
            public void caixaFechado() {
                alert("Caixa Fechado!");
            }

            @Override
            public void caixaErro(String msg) {
                alert(msg);
            }
        }.abrirCaixa(UtilSet.getUsuarioId(getContext()));
    }

    private void caixaOK(Caixa c){
        new ControleImpRelatorioFechamento(getActivity(), new ListenerImpRelatorioFechamento() {
            @Override
            public void ImpRelatorioFechamentoOk(Bitmap bit) {
                bitmap = bit ;
                onImpressaoBitMap.imprimirBitMap(bitmap);
            }

            @Override
            public void ImpRelatorioFechamentoErro(String msg) {
                alert(msg);
            }
        }).getBitmap(c, 350, 20) ;
    }

    private void fecharCaixa() {
        new ControleFecharCaixa(getActivity(), caixa) {
            @Override
            public void fechamentoOK() {
                voltar("Caixa Fechado!");
            }

            @Override
            public void vendaAberta() {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_notification)
                        .setTitle("Atenção")
                        .setMessage("Venda Aberta!")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) -> voltar())
                        .show();
            }

            @Override
            public void fechamentoErro(String msg) {
                alert(msg);
            }
        }.executar();

    }

    private void voltar(String msg){
      //  requireActivity().onBackPressed();
      //  requireActivity().getOnBackPressedDispatcher() ;
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialogInterface, i) -> voltar())
                .show();

    }

    private void voltar(){
        requireActivity().onBackPressed();
    }

    private void alert(String msg) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
        //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void imprimirBitMapOk() {
        voltar("Impressão OK");
    }

    @Override
    public void imprimirBitMapErro(String msg) {
        alert(msg) ;
    }
}