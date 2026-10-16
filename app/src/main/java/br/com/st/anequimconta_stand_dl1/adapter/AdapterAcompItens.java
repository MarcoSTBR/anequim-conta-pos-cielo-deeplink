package br.com.st.anequimconta_stand_dl1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AdapterAcompItens extends RecyclerView.Adapter<AdapterAcompItens.ItensHold> {

    private Context ctx ;
    private AcompanhamentoView ac ;
    private List<AcompanhamentoItemView> list ;


    public AdapterAcompItens(Context ctx, AcompanhamentoView ac) {
        this.ctx = ctx;
        this.ac = ac ;
        list = ac.getAcompanhamentoItems();
    }

    @NonNull
    @Override
    public ItensHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_grade_acompanhamemto_item, parent, false) ;
        return new ItensHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItensHold holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItensHold extends RecyclerView.ViewHolder{

        private TextView descricao ;
        private TextView quantidade ;

        private ImageButton mais ;
        private ImageButton menos ;
        public ItensHold(@NonNull View itemView) {
            super(itemView);
            descricao  = itemView.findViewById(R.id.descricao) ;
            quantidade = itemView.findViewById(R.id.quantidade) ;
            mais       = itemView.findViewById(R.id.mais) ;
            menos      = itemView.findViewById(R.id.menos) ;
            mais.setOnClickListener((View) -> setMais(list.get(getAdapterPosition()))) ;
            menos.setOnClickListener((View) -> setMenos(list.get(getAdapterPosition())));
        }

        private void setMais(AcompanhamentoItemView it) {
            if (((totalGeral() + 1) > ac.getAcompanhamento().getMax()) ||
                 ((it.getQuantidade() + 1) > it.getAcompanhamentoItem().getMax())) {
                Toast.makeText(ctx, "Quantidade Inválida!", Toast.LENGTH_SHORT).show();
             } else {
                it.setQuantidade(it.getQuantidade() + 1);
                notifyDataSetChanged();
                changeIten();
            }
        }

        private void setMenos(AcompanhamentoItemView it) {
            if (it.getQuantidade() - 1  < 0) {
                Toast.makeText(ctx, "Quantidade Inválida!", Toast.LENGTH_SHORT).show();
            } else {
                it.setQuantidade(it.getQuantidade() - 1);
                notifyDataSetChanged();
                changeIten();
            }
        }

        private int totalGeral() {
            int tot = 0 ;
            for (AcompanhamentoItemView it : list){
                tot += it.getQuantidade() ;
            }
            return tot ;
        }

        public void bind(AcompanhamentoItemView aci){
            Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(aci.getAcompanhamentoItem().getProduto_id());
            descricao.setText(p.getDescricao());

            String q = "#0" ;
            if (!p.seUnidade())
                q = "#0.###" ;
            quantidade.setText(new DecimalFormat(q).format(aci.getQuantidade()));
        }
    }
    public abstract void changeIten() ;
}
