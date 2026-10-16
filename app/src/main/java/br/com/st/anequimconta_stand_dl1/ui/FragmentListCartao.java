package br.com.st.anequimconta_stand_dl1.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;
import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterPosPagamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleCaixa;
import br.com.st.anequimconta_stand_dl1.controle.ControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.databinding.FragmentListCartaoBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Caixa;
import br.com.st.anequimconta_stand_dl1.impressao.controle.ControlePosReimpressao;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnControlePosReimpressao;
import br.com.st.anequimconta_stand_dl1.pospag.ControlePosConected;
import br.com.st.anequimconta_stand_dl1.pospag.OnListPagamentos;
import br.com.st.anequimconta_stand_dl1.pospag.OnPosPagConnect;
import br.com.st.anequimconta_stand_dl1.pospag.PosApi;
import br.com.st.anequimconta_stand_dl1.pospag.PosApiBuilder;
import br.com.st.anequimconta_stand_dl1.pospag.PosFilter;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagType;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;
import br.com.st.anequimconta_stand_dl1.pospag.TipoRecebimentoPos;
import br.com.st.anequimconta_stand_dl1.pospag.controle.ControleListCancelamento;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentListCartao extends Fragment implements OnListPagamentos, OnControlePosReimpressao {

    private FragmentListCartaoBinding binding ;
    private PosApi posApi;
    private ControleListCancelamento controleListCancelamento ;
    private ControlePosReimpressao controlePosReimpressao ;

    public FragmentListCartao() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListCartaoBinding.inflate(inflater, container, false) ;// Inflate the layout for this fragment
        posApi = PosApiBuilder.getApi(FragmentListCartao.this);
        controleListCancelamento = new ControleListCancelamento(FragmentListCartao.this) {
            @Override
            public void cancelamentoOk() {
                getPagamentos();
            }

            @Override
            public void cancelamentoErro(String msg) {
                alert(msg);

            }
        } ;
        controlePosReimpressao = new ControlePosReimpressao(FragmentListCartao.this) ;
        AndroidThreeTen.init(getActivity()) ;
        return binding.getRoot();// inflater.inflate(R.layout.fragment_list_cartao, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        conexaoPOS() ;
    }

    private void conexaoPOS(){
        ControlePosConected.getControlePagConected(getActivity()).executar(new OnPosPagConnect() {
            @Override
            public void connected(PosPagType tipo) {
                getPagamentos();
            }

            @Override
            public void erro(String msg) {
                alertSair(msg) ;
            }
        });
    }

    private void getPagamentos() {
        new ControleCaixa(getActivity()) {
            @Override
            public void caixaAberto(Caixa c) {
                PosFilter f = new PosFilter();
                f.setDtaInicial(c.getDATA());
                for (TipoRecebimentoPos t : TipoRecebimentoPos.values())
                    f.getTipoRecebimentoPos().add(t);
                posApi.getList(f, FragmentListCartao.this);
            }

            @Override
            public void caixaFechado() {
                PosFilter f = new PosFilter();
                String ds = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" 00:00:01" ;
                Date d = UtilSet.getData(ds) ;
                f.setDtaInicial(d);
                for (TipoRecebimentoPos t : TipoRecebimentoPos.values())
                    f.getTipoRecebimentoPos().add(t);
                posApi.getList(f, FragmentListCartao.this);

                // OnErro("Caixa Fechado!");
            }

            @Override
            public void caixaErro(String msg) {
                OnErro(msg);
            }
        }.getCaixaAberto(UtilSet.getUsuarioId(getContext()));

    }

    @Override
    public void OnSucess(List<PosPagamentoView> l) {
        if (l.size() > 0) {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
            binding.gradePospagamentos.setLayoutManager(layoutManager);
            binding.gradePospagamentos.setAdapter(new AdapterPosPagamento(getActivity(), l) {
                @Override
                public void selecionado(PosPagamentoView pag) {
                    selecionarOpcao(pag);
                }
            });
        } else alertSair("Nenhum Pagamento Encontrado!");
    }

    private void selecionarOpcao(final PosPagamentoView pag) {
        CharSequence options[] = new CharSequence[]{Html.fromHtml("<b>Reimpressão</b>"), Html.fromHtml("<b>Cancelamento</b>")};
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Selecione a Opção:")
                .setCancelable(true)
                .setItems(options, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            reimpressao(pag);
                            break;
                        default:
                            cancelamento(pag);
                    }
                })
                .show();

    }

    private void cancelamento(PosPagamentoView pag) {
        new ControleLiberacaoAcesso(getActivity(), new ListenerControleLiberacaoAcesso() {
            @Override
            public void acessoOk(int usuario_id) {
                controleListCancelamento.executar(pag);
            }
            @Override
            public void acessoErro(String msg) {
                alert(msg);
            }
        }).acesso(20); // '20', '1.1.1.00020', 'Cancelar Pagamento do Pedido', '1', '1'
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

    private void alertSair(String msg) {

        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_notification)
                .setTitle("Atenção:")
                .setCancelable(true)
                .setMessage(msg)
                .setPositiveButton("Ok", (dialogInterface, i) -> voltar())
                .show();
    }
    private void voltar(){
        requireActivity().onBackPressed();
    }

    private void reimpressao(PosPagamentoView pag) {
       controlePosReimpressao.reimpressao(pag, this);
    }

    @Override
    public void OnErro(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void impressaoOk() {
        Toast.makeText(getContext(), "Impressao OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void impressaoErro(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}