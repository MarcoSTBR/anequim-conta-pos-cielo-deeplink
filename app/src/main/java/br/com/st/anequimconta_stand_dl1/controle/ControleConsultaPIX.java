package br.com.st.anequimconta_stand_dl1.controle;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.conexao.ConexaoRecebimentoPixPos;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnConsultaPIX;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilter;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPosPix;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPix;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPixView;

public class ControleConsultaPIX {

    private Activity act ;
    private OnConsultaPIX onConsultaPIX ;
    private List<PosPagamentoPixView> list ;
    private  List<ControleContaMultPagList.PagamentoView> pagamentoView ;
    private int ponteiro ;

    public ControleConsultaPIX(Activity act, List<ControleContaMultPagList.PagamentoView> contaPags, OnConsultaPIX onConsultaPIX) {
        this.act = act;
        this.pagamentoView = contaPags ;
        this.onConsultaPIX = onConsultaPIX;
    }

    public void executar(){
        list = new ArrayList<PosPagamentoPixView>() ;
        if (pagamentoView.size() > 0) {
          ponteiro = 0 ;
          buscar(pagamentoView.get(ponteiro)) ;
        } else onConsultaPIX.ConsultaPix(list);


    }

    private void buscar(ControleContaMultPagList.PagamentoView pg){
        DaoFilters f = new DaoFilters() ;
        f.add(new DaoFilter("CONTA_PAG_UUID", "=", pg.getContaPag().getUUID()));
        try {
            new ConexaoRecebimentoPixPos(act, f, "") {
                @Override
                public void recebimentoPixPosOk(List<RecebimentoPosPix> l) {
                    if (l.size() > 0){
                        setRecebimentoPosPix(l.get(0)) ;
                    }

                    ++ponteiro ;
                    if (ponteiro < pagamentoView.size()) {
                       buscar(pagamentoView.get(ponteiro));
                    } else onConsultaPIX.ConsultaPix(list);
                }

                @Override
                public void recebimentoPixPosErro(String msg) {
                    onConsultaPIX.ConsultaPixErro(msg);

                }
            }.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onConsultaPIX.ConsultaPixErro(e.getMessage());
        }

    }

    private void setRecebimentoPosPix(RecebimentoPosPix recebimentoPosPix) {
        try {
            JSONObject j =  new JSONObject(recebimentoPosPix.getRECIBO()) ;
            String tx_id         = String.valueOf(recebimentoPosPix.getId()) ; //j.getString("instant_payment_id");
            Double cob_value     = j.getDouble("amount") / 100;
            String status        = String.valueOf(recebimentoPosPix.getSTATUS()) ;
            String pix_client_id = j.getString("instant_payment_id") ;
            list.add(new PosPagamentoPixView(tx_id, cob_value, status, pix_client_id)) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
