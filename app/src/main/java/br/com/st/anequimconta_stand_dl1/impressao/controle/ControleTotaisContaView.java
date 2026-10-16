package br.com.st.anequimconta_stand_dl1.impressao.controle;

import android.app.Activity;
import android.widget.Toast;

import br.com.st.anequimconta_stand_dl1.entidade.ContaItem;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemAdd;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemView;
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ControleTotaisContaView {

    private Activity ctx ;
    private ContaView contaView ;
    private List<ContaItem> listGrupo;


    public ControleTotaisContaView(Activity ctx, ContaView contaView) {
        this.ctx = ctx;
        this.contaView = contaView;
        getContaItens() ;
    }

    public ContaView getContaView() {
        return contaView;
    }

    public List<ContaItem> getListGrupo() {
        return listGrupo;
    }

    public Double getQuantidade(){
        Double q = 0.0 ;
        for (ContaItemView it : contaView.getContaItemViews()) {
            if (it.getContaItem().getSTATUS() == 1) {
                q += it.getContaItem().getQUANTIDADE();
                for (ContaItemAdd itAdd : it.getContaItemAdds()) {
                    q += itAdd.getQUANTIDADE();
                }
            }
        }
        return q ;
    }

    public Double getTotaItens(){
        Double v = 0.0 ;
        for (ContaItemView it : contaView.getContaItemViews()) {
            if (it.getContaItem().getSTATUS() == 1) {
                v += it.getContaItem().getVALOR() - it.getContaItem().getDESCONTO();
                for (ContaItemAdd itAdd : it.getContaItemAdds()) {
                    v += itAdd.getVALOR() - itAdd.getDESCONTO();
                }
            }
        }
        return v ;
    }

    public Double getDesconto(){
      return contaView.getConta().getDESCONTO() ;
    }

    public Double getTotal(){
        Double v = 0.0 ;
        for (ContaItemView it : contaView.getContaItemViews()) {
            if (it.getContaItem().getSTATUS() == 1)
            {
                v += it.getContaItem().getVALOR() - it.getContaItem().getDESCONTO();
                if (contaView.getConta().getSTATUS_COMISSAO() == 1)
                    v += it.getContaItem().getCOMISSAO() ;
                for (ContaItemAdd itAdd : it.getContaItemAdds())
                {
                    v += itAdd.getVALOR() - itAdd.getDESCONTO();
                    if (contaView.getConta().getSTATUS_COMISSAO() == 1)
                        v += itAdd.getCOMISSAO() ;
                }
            }
        }
        v -= contaView.getConta().getDESCONTO() ;
        return v ;
    }

    public Double getComissao(){
        Double v = 0.0 ;
        if (contaView.getConta().getSTATUS_COMISSAO() == 1)
        {
            for (ContaItemView it : contaView.getContaItemViews())
            {
                if (it.getContaItem().getSTATUS() == 1)
                {
                    v += it.getContaItem().getCOMISSAO();
                    for (ContaItemAdd itAdd : it.getContaItemAdds())
                        v += itAdd.getCOMISSAO();

                }
            }
        }
        return v ;
    }

    private void getContaItens() {
        listGrupo = new ArrayList<>();
        try {
            for (ContaItemView it : contaView.getContaItemViews()) {
                if (it.getContaItem().getSTATUS() == 1) {
                    addItem(it.getContaItem());
                    for (ContaItemAdd itAdd : it.getContaItemAdds()) {
                        addItem(itAdd);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, "getContasItens "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addItem(ContaItem  contaItem) throws JSONException {
        ContaItem it =  seExist(contaItem) ;
        if (it == null) {
            it = new ContaItem(contaItem.getJSON());
            listGrupo.add(it);
        } else {
             it.setQUANTIDADE(it.getQUANTIDADE() + contaItem.getQUANTIDADE()) ;
             it.setVALOR(it.getVALOR() + contaItem.getVALOR()) ;
             it.setCOMISSAO(it.getCOMISSAO() + contaItem.getCOMISSAO()) ;
        }
    }

    private void addItem(ContaItemAdd  contaItemAdd) throws JSONException {
        ContaItem c = null ;
       // Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(contaItemAdd.getPRODUTO_ID()) ;
        for (ContaItem it : listGrupo){
            if (it.getPRODUTO_ID() == contaItemAdd.getPRODUTO_ID())
                c = it ;
        }
        if (c == null){
            c = new ContaItem(contaItemAdd.getJSON()) ;
            listGrupo.add(c) ;
        } else {
            c.setQUANTIDADE(c.getQUANTIDADE() + contaItemAdd.getQUANTIDADE()) ;
            c.setVALOR(c.getQUANTIDADE() + contaItemAdd.getVALOR()) ;
            c.setCOMISSAO(c.getCOMISSAO() + contaItemAdd.getCOMISSAO()) ;
        }
    }

    private ContaItem seExist(ContaItem item){
        ContaItem c = null ;
       // Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(prod_id) ;
        for (ContaItem it : listGrupo){
            if (it.getPRODUTO_ID() == item.getPRODUTO_ID())
                c = it ;
        }
        return c ;
    }

}
