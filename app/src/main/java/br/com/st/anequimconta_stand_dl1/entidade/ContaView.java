package br.com.st.anequimconta_stand_dl1.entidade;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContaView {

    private Conta conta ;
    private List<ContaItemView> contaItemViews ;

    public ContaView(JSONObject j) throws JSONException {
        this.conta = new Conta(j);
        this.contaItemViews = new ArrayList<ContaItemView>() ;
        JSONArray jItens = j.getJSONArray("ITENS") ;
        for (int i = 0 ; i < jItens.length() ; i++) {
            if (jItens.getJSONObject(i).getString("UUID").equals(jItens.getJSONObject(i).getString("UUID_ACOMP"))){
                ContaItemView it = new ContaItemView(new ContaItem(jItens.getJSONObject(i)), new ArrayList<>()) ;
                 this.contaItemViews.add(it) ;
            }
        }
        for (int i = 0 ; i < jItens.length() ; i++) {
            if (!(jItens.getJSONObject(i).getString("UUID").equals(jItens.getJSONObject(i).getString("UUID_ACOMP")))){
                Log.i("contaItemViews", "add "+jItens.getJSONObject(i).getString("UUID")+" "+jItens.getJSONObject(i).getString("UUID_ACOMP")) ;
                for (ContaItemView itv : this.contaItemViews) {
                    if (itv.getContaItem().getUUID().equals(jItens.getJSONObject(i).getString("UUID_ACOMP"))) {
                        Log.i("contaItemViews", "add foi "+jItens.getJSONObject(i).getString("UUID")) ;
                        itv.getContaItemAdds().add(new ContaItemAdd(jItens.getJSONObject(i))) ;
                    }
                }
            }
        }
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public List<ContaItemView> getContaItemViews() {
        return contaItemViews;
    }

    public void setContaItemViews(List<ContaItemView> contaItemViews) {
        this.contaItemViews = contaItemViews;
    }
}
