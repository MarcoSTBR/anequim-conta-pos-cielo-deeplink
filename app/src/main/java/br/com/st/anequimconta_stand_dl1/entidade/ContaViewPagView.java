package br.com.st.anequimconta_stand_dl1.entidade;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContaViewPagView {

    private ContaMult contaMult ;
    private List<ContaView> contaViews ;
    private List<ContaPag> contaPags ;

    public ContaViewPagView(JSONObject j) throws JSONException {
        contaMult = new ContaMult(j) ;
        contaViews = new ArrayList<>();
        contaPags = new ArrayList<>() ;
        JSONArray jContas = j.getJSONArray("CONTAS") ;
        for (int i = 0 ; i < jContas.length() ; i++){
            contaViews.add(new ContaView(jContas.getJSONObject(i))) ;
        }
        JSONArray jPags = j.getJSONArray("PAGAMENTOS") ;
        for (int i = 0 ; i < jPags.length() ; i++){
            contaPags.add(new ContaPag(jPags.getJSONObject(i))) ;
        }
    }

    public ContaViewPagView(ContaMult contaMult, List<ContaView> contaViews, List<ContaPag> contaPags) {
        this.contaMult = contaMult;
        this.contaViews = contaViews;
        this.contaPags = contaPags;
    }


    public ContaMult getContaMult() {
        return contaMult;
    }

    public void setContaMult(ContaMult contaMult) {
        this.contaMult = contaMult;
    }

    public List<ContaView> getContaViews() {
        return contaViews;
    }

    public void setContaViews(List<ContaView> contaViews) {
        this.contaViews = contaViews;
    }

    public List<ContaPag> getContaPags() {
        return contaPags;
    }

    public void setContaPags(List<ContaPag> contaPags) {
        this.contaPags = contaPags;
    }

    public static class DataDesc implements Comparator<ContaViewPagView> {

        @Override
        public int compare(ContaViewPagView conta1, ContaViewPagView conta2) {
            int r = (conta1.getContaMult().getDATA().compareTo(conta2.getContaMult().getDATA())) ;
            Log.i("compare", "r "+r+" conta1 "+conta1.getContaMult().getDATA()+" conta2 "+conta2.getContaMult().getDATA()) ;
            return r ;
        }
    }
}
