package br.com.st.anequimconta_stand_dl1.entidade;

import java.util.List;

public class ContaItemView {

    private ContaItem contaItem ;
    private List<ContaItemAdd> contaItemAdds ;

    public ContaItemView(ContaItem contaItem, List<ContaItemAdd> contaItemAdds) {
        this.contaItem = contaItem;
        this.contaItemAdds = contaItemAdds;
    }

    public ContaItem getContaItem() {
        return contaItem;
    }

    public void setContaItem(ContaItem contaItem) {
        this.contaItem = contaItem;
    }

    public List<ContaItemAdd> getContaItemAdds() {
        return contaItemAdds;
    }

    public void setContaItemAdds(List<ContaItemAdd> contaItemAdds) {
        this.contaItemAdds = contaItemAdds;
    }
}
