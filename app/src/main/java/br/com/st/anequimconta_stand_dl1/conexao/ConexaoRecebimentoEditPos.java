package br.com.st.anequimconta_stand_dl1.conexao;

import android.app.Activity;
import android.util.Log;

import br.com.st.anequimconta_stand_dl1.entidade.RecebimentoPos;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ConexaoRecebimentoEditPos extends ConexaoServer{

    private RecebimentoPos recebimentoPos ;

    public ConexaoRecebimentoEditPos(Activity ctx, RecebimentoPos recebimentoPos) throws MalformedURLException, JSONException {
        super(ctx);
        this.recebimentoPos = recebimentoPos ;
        msg = "Atualizando Modalidade...";
        maps.put("class","AfoodRecebimentoPos") ;
        maps.put("method","store2") ; //methodo resinscrito loaadAll
        maps.put("data",recebimentoPos.geJSON()) ;
        url =  new URL(UtilSet.getServidorMaster(ctx)) ; //Dao.getLinkAcessoADO(ctx).getLinkAcesso(Link.fConsultaProduto).getUrl() ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Recebimento_pos", codInt +" "+ s) ;
        try {
            JSONObject j = new JSONObject(s) ;
            if (j.getString("status").equals("success")) {
                  recebimentoEditPosOk(new RecebimentoPos(j.getJSONObject("data"))) ;
            } else {
                recebimentoEditPosErro(j.getString("data"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            recebimentoEditPosErro("COD "+codInt+" "+s);
        }
    }


    public abstract void recebimentoEditPosOk(RecebimentoPos r);
    public abstract void recebimentoEditPosErro(String msg);
}
