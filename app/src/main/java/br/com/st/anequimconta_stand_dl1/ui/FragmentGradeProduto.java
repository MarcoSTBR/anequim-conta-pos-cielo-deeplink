package br.com.st.anequimconta_stand_dl1.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.activity.ActivityProdutoAdd;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterProdutos;
import br.com.st.anequimconta_stand_dl1.adapter.listeners.ListenerAdapterProduto;
import br.com.st.anequimconta_stand_dl1.controle.ControleGradeView;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentGradeProdutoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.util.ArrayList;
import java.util.List;

public class FragmentGradeProduto extends Fragment implements ListenerAdapterProduto {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "GRADE_VENDA_ID";

    // TODO: Rename and change types of parameters
    private int gradeId;

    public FragmentGradeProduto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gradeId Parameter 1.
     * @return A new instance of fragment FragmentGradeProduto.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGradeProduto newInstance(int gradeId) {
        FragmentGradeProduto fragment = new FragmentGradeProduto();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, gradeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gradeId = getArguments().getInt(ARG_PARAM1);
        }
    }

    private FragmentGradeProdutoBinding binding ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGradeProdutoBinding.inflate(inflater, container, false) ;
        // Inflate the layout for this fragment
        return binding.getRoot()  ; //inflater.inflate(R.layout.fragment_grade_produto, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        gradeProdutos();
    }

    private void gradeProdutos() {
        List<Produto> l = getFiltro() ;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),  3);
        binding.gradeProdutos.setLayoutManager(layoutManager);
        binding.gradeProdutos.setAdapter(new AdapterProdutos(getActivity(), l, this));
    }

    private List<Produto> getFiltro(){
        try {
            return ControleGradeView.getControleGradeView(getActivity()).get(gradeId).getProdutoList();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("atualizar_", "getFiltro() "+ e.getMessage());
            return new ArrayList<>() ;
        }
    }

    @Override
    public void OnAddProduto(Produto produto) {
        Intent intent = new Intent(getActivity(), ActivityProdutoAdd.class);
        intent.putExtra("PRODUTO_ID", produto.getId());
        startActivity(intent);
       // redicecionaParaVenda.launch(intent);
    }

    @Override
    public void OnChange(Produto produto) {
      //  ControleBadgeView.getControleBadgeView(getActivity()).executar();
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
}