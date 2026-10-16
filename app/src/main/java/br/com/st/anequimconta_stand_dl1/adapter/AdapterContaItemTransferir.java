package br.com.st.anequimconta_stand_dl1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemAdd;
import br.com.st.anequimconta_stand_dl1.entidade.ContaItemTransferenciaView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AdapterContaItemTransferir extends RecyclerView.Adapter<AdapterContaItemTransferir.ViewHolder> {

    private Activity ctx ;
    private List<ContaItemTransferenciaView> list ;

    public AdapterContaItemTransferir(Activity ctx, List<ContaItemTransferenciaView> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterContaItemTransferir.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_transferencia, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContaItemTransferir.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView descricao ;
        private ImageButton cancelar ;
        private TextView produto_add ;
        private TextView quantidade ;
        private TextView quantidade_item ;
        private ImageButton mais ;
        private ImageButton menos ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricao   = itemView.findViewById(R.id.descricao) ;
            cancelar    = itemView.findViewById(R.id.cancelar) ;
            produto_add = itemView.findViewById(R.id.produto_add) ;
            quantidade  = itemView.findViewById(R.id.quantidade) ;
            quantidade_item = itemView.findViewById(R.id.quantidade_item) ;
            mais = itemView.findViewById(R.id.mais_um) ;
            menos = itemView.findViewById(R.id.menos_um) ;
            cancelar.setOnClickListener(this);
            mais.setOnClickListener(view->{
                ContaItemTransferenciaView it = list.get(getAdapterPosition()) ;
                if ((it.getQuantidade()+1) > (it.getContaItemView().getContaItem().getQUANTIDADE()))
                    alert("Quantidade Excedente!");
                else {
                    it.setQuantidade(it.getQuantidade() +1) ;
                    quantidade_item.setText(new DecimalFormat("#0").format(it.getQuantidade()));
                }
            });
            menos.setOnClickListener(view->{
                ContaItemTransferenciaView it = list.get(getAdapterPosition()) ;
                if ((it.getQuantidade()-1) < 0)
                    alert("Quantidade Inválida!") ;
                else {
                    it.setQuantidade(it.getQuantidade() - 1) ;
                    quantidade_item.setText(new DecimalFormat("#0").format(it.getQuantidade()));
                }
            });
        }

        public void bind(ContaItemTransferenciaView it){
            Produto prd = ProdutoDao.getProdutoDao(ctx).getProduto(it.getContaItemView().getContaItem().getPRODUTO_ID()) ;
            descricao.setText(prd.getDescricao());
            String padd = "" ;
            for (ContaItemAdd itAdd : it.getContaItemView().getContaItemAdds()) {
                prd = ProdutoDao.getProdutoDao(ctx).getProduto(itAdd.getPRODUTO_ID()) ;
                padd += prd.getDescricao()+"\n" ;
            }
            produto_add.setText(padd);
            quantidade.setText(new DecimalFormat("#0").format(it.getContaItemView().getContaItem().getQUANTIDADE()));
            quantidade_item.setText(new DecimalFormat("#0").format(it.getQuantidade()));
        }

        private void alert(String msg){
            new AlertDialog.Builder(ctx)
                    .setIcon(R.drawable.ic_notification)
                    .setTitle("Atenção")
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        // getContaView() ;
                    })
                    .show();
        }



        @Override
        public void onClick(View view) {
            if (list.get(getAdapterPosition()).getQuantidade() > 0)
                transferencia(list.get(getAdapterPosition())) ;
            else alert("Sem Quantidade Para Transferência!") ;
        }
    }

    public abstract void transferencia(ContaItemTransferenciaView contaItemTransferenciaView) ;
}
