package br.com.st.anequimconta_stand_dl1.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterPosPixPagamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleCaixa;
import br.com.st.anequimconta_stand_dl1.controle.ControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentListPixBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.pospag.OnListPixPagamentos;
import br.com.st.anequimconta_stand_dl1.pospag.PosApi;
import br.com.st.anequimconta_stand_dl1.pospag.PosApiBuilder;
import br.com.st.anequimconta_stand_dl1.pospag.PosFilter;
import br.com.st.anequimconta_stand_dl1.pospag.PosPixClient;
import br.com.st.anequimconta_stand_dl1.pospag.controle.ControleListPixCancelamento;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPixView;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;
import kotlinx.coroutines.debug.internal.AgentInstallationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentListPix extends Fragment {


    private FragmentListPixBinding binding ;
    private static final String TAG = "PIX_CLIENT";
    private PosApi posApi;
    private Caixa caixa;
    private ControleListPixCancelamento controleListPixCancelamento ;


    public FragmentListPix() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListPixBinding.inflate(inflater, container, false) ;
        controleListPixCancelamento = new ControleListPixCancelamento(FragmentListPix.this) {
            @Override
            public void cancelamentoPixOk () {
                getPixPagamentos();
            }

            @Override
            public void cancelamentoPixErro (String msg){
                onErro(msg);
            }
        };
        posApi = PosApiBuilder.getApi(getActivity()) ;
        return binding.getRoot() ; // inflater.inflate(R.layout.fragment_list_pix, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPixPagamentos();
    }

    private void getPixPagamentos() {
        new ControleCaixa(getActivity()) {
            @Override
            public void caixaAberto(Caixa c) {
                caixa = c;
                preparar();
            }

            @Override
            public void caixaFechado() {
                onErro("Caixa Fechado!");
            }

            @Override
            public void caixaErro(String msg) {
                onErro(msg);
            }
        }.getCaixaAberto(UtilSet.getUsuarioId(getContext()));

    }

    private void preparar() {
        posApi.getPixList(new PosFilter(), new OnListPixPagamentos() {
            @Override
            public void OnPixSucess(List<PosPagamentoPixView> l) {
                if (l.size() > 0) {
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
                    binding.gradePospagamentosPix.setLayoutManager(layoutManager);
                    binding.gradePospagamentosPix.setAdapter(new AdapterPosPixPagamento(getContext(), l) {
                        @Override
                        public void selecionarOpcao(PosPagamentoPixView pix) {
                            selecionado(pix);
                        }
                    });
                } else alertSair("Nenhum Pagamento Encotrado!") ;

            }

            @Override
            public void OnPixError(String msg) {
                alert(msg);

            }
        });
    }

    private void grade(String s) {
        List<PosPagamentoPixView> l = getList(s);
        if (l.size() > 0) {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
            binding.gradePospagamentosPix.setLayoutManager(layoutManager);
            binding.gradePospagamentosPix.setAdapter(new AdapterPosPixPagamento(getContext(), l) {
                @Override
                public void selecionarOpcao(PosPagamentoPixView pix) {
                    selecionado(pix);
                }
            });
        } else alertSair("Nenhum Pagamento Encotrado!") ;

    }

    private void alertSair(String msg) {
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Aterção:")
                .setCancelable(true)
                .setMessage(msg)
                .setPositiveButton("Ok", (dialogInterface, i) -> voltar())
                .show();
    }

    private void voltar(){
        requireActivity().onBackPressed();
    }

    private void selecionado(final PosPagamentoPixView pix) {
        CharSequence options[] = new CharSequence[]{Html.fromHtml("<b>Cancelamento</b>"), Html.fromHtml("<b>Sair</b>")};
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Selecione a Opção:")
                .setCancelable(true)
                .setItems(options, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            cancelamentoPix(pix);
                            break;
                        default:
                            voltar();
                    }
                })
                .show();
    }

    private void cancelamentoPix(PosPagamentoPixView pix){
        new ControleLiberacaoAcesso(getActivity(), new ListenerControleLiberacaoAcesso() {
            @Override
            public void acessoOk(int usuario_id) {
                controleListPixCancelamento.executar(pix);
            }
            @Override
            public void acessoErro(String msg) {
                alert(msg);

            }
        }).acesso(20);// '20', '1.1.1.00020', 'Cancelar Pagamento do Pedido', '1', '1'
    }

    private void alert(String msg) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção:")
                .setCancelable(true)
                .setMessage(msg)
                .setPositiveButton("Ok", null)
                .show();
    }

    private List<PosPagamentoPixView> getList(String s){
        List<PosPagamentoPixView> l = new ArrayList<>() ;
        JSONArray arr = null;
        try {
            arr = new JSONArray(s);
            for (int i=0 ; i < arr.length() ; ++i){
                l.add(new PosPagamentoPixView(arr.getJSONObject(i))) ;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return l ;
    }

    private String getRequest() {
        JSONObject j = new JSONObject();
/*
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"	2001-07-04T12:08:56.235-0700
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"	2001-07-04T12:08:56.235-07:00
*/
        SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Log.i(TAG, "data inicial "+fd.format(caixa.getDATA())) ;
        Log.i(TAG, "data final   "+fd.format(new Date())) ;
        try {
            j.put("start_date", fd.format(caixa.getDATA()));
            j.put("end_date", fd.format(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j.toString() ;
    }

    private void onErro(String msg) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção:")
                .setCancelable(true)
                .setMessage(msg)
                .setPositiveButton("Ok", (dialog, i) -> voltar())
                .show();
    }
}