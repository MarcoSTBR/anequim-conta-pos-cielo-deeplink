package br.com.st.anequimconta_stand_dl1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.entidade.AcompanhamentoView;

import java.util.List;

public abstract class AdapterAcompanhamento extends RecyclerView.Adapter<AdapterAcompanhamento.AdapterAcHolder> {
    private Context ctx ;
    private List<AcompanhamentoView> list ;

    public AdapterAcompanhamento(Context ctx, List<AcompanhamentoView> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterAcHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_grade_acompanhamemto, parent, false) ;
        return new AdapterAcHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAcHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterAcHolder extends RecyclerView.ViewHolder{

        private TextView text_descricao ;
        private TextView text_obs ;
        private RecyclerView grade_acompanhamento_item ;
        public AdapterAcHolder(@NonNull View itemView) {
            super(itemView);
            text_descricao = itemView.findViewById(R.id.text_descricao) ;
            text_obs = itemView.findViewById(R.id.text_obs) ;
            grade_acompanhamento_item = itemView.findViewById(R.id.grade_acompanhamento_item) ;
        }

        private void setGradeItens(final AcompanhamentoView ac) {
            GridLayoutManager layoutManager = new GridLayoutManager(ctx, 1) ;
            grade_acompanhamento_item.setLayoutManager(layoutManager);
            grade_acompanhamento_item.setAdapter(new AdapterAcompItens(ctx, ac) {
                @Override
                public void changeIten() {
                    change() ;
                }
            });
        }

        public void bind(AcompanhamentoView ac){
            String descricao = ac.getAcompanhamento().getDescricao() ;
            descricao += "\n Min. "+ac.getAcompanhamento().getMin()+" Max "+ ac.getAcompanhamento().getMax();
            text_descricao.setText(descricao);
            text_obs.setText(ac.getAcompanhamento().getObs());
            setGradeItens(ac) ;
        }
    }

    public abstract void change() ;
}
