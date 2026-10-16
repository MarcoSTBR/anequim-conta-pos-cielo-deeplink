package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPosPix;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoRecebimentoPixEditPos extends ConexaoServer{


    public ConexaoRecebimentoPixEditPos(Activity ctx, RecebimentoPosPix recebimentoPixPos) throws MalformedURLException, JSONException {
        super(ctx);
        msg = "Atualizando Modalidade...";
        maps.put("class","AfoodRecebimentoPixPos") ;
        maps.put("method","store2") ; //methodo resinscrito loaadAll
        if (recebimentoPixPos.getId() == 0)
           maps.put("data", recebimentoPixPos.geJSON()) ;
         else maps.put("data", recebimentoPixPos.geAltJSON()) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaProduto).getUrl() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Recebimento_pix_pos", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                recebimentoEditPixPosOk(new RecebimentoPosPix(j.getJSONObject("data"))) ;
            } else {
                recebimentoEditPixPosErro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            recebimentoEditPixPosErro("COD "+codInt+" "+s);
        }
    }


    public abstract void recebimentoEditPixPosOk(RecebimentoPosPix r);
    public abstract void recebimentoEditPixPosErro(String msg);
}
