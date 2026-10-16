package br.com.st.anequimconta_stand_dl1.ui;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterContasPagas;
import br.com.st.anequimconta_stand_dl1.controle.ControleContasPagas;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleContasPagas;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentContasPagasBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControleImpContaPagNFCeView;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControleImpContaPagView;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControleImpContaPagView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentContasPagas extends Fragment implements OnControleContasPagas, OnControleImpContaPagView {

    private FragmentContasPagasBinding binding ;
    private Caixa caixa ;
    private ControleImpContaPagView controleImpContaPagView ;
    private ControleImpContaPagNFCeView controleImpContaPagNFCeView ;

    public FragmentContasPagas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContasPagasBinding.inflate(inflater, container, false) ;
        controleImpContaPagView = new ControleImpContaPagView(FragmentContasPagas.this, this) ;
        controleImpContaPagNFCeView = new ControleImpContaPagNFCeView(FragmentContasPagas.this, this);
        return binding.getRoot() ;//inflater.inflate(R.layout.fragment_contas_pagas, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        new ControleContasPagas(getActivity(), this).filtro();
    }

    private void displayData(Date data){
        binding.dataInicial.setText(new SimpleDateFormat("dd/MM/yy HH:mm").format(data));
    }


    @Override
    public void onControleContasPagasOk(List<ContaViewPagView> l) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1) ;
        binding.gradeContasPagas.setLayoutManager(layoutManager);
        binding.gradeContasPagas.setAdapter(new AdapterContasPagas(getActivity(), l) {
            @Override
            public void selecionado(ContaViewPagView it) {
                setContaNfce(it) ;
            }
        });
    }

    @Override
    public void onControleContasPagasErro(String msg) {
        alertSair(msg); ;
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
        controleImpContaPagView.imprimir(contaViewPagView);
    }


    private void contaNFCe(ContaViewPagView contaViewPagView){
        controleImpContaPagNFCeView.imprimir(contaViewPagView);

    }

    private void alert(String msg){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg+", Deseja Sair?")
                .setCancelable(false)
                .setPositiveButton("Sim",(i, e) -> voltar())
                .setNegativeButton("Não", null)
                .show();
        //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void alertSair(String msg){
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",(d, i) -> voltar())
                .show();
        //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void voltar() {
        requireActivity().onBackPressed();
    }

    @Override
    public void controleImpContaPagViewOk() {
        alert("Impressão OK");
    }

    @Override
    public void controleImpContaPagViewErro(String msg) {
        alertSair(msg);

    }
}