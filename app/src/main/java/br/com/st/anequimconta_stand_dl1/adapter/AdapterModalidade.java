package br.com.st.anequimconta_stand_dl1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;

import java.util.List;

public abstract class AdapterModalidade extends RecyclerView.Adapter<AdapterModalidade.ViewHolder> {

    private Activity ctx ;
    private List<Modalidade> list ;

    public AdapterModalidade(Activity ctx, List<Modalidade> list) {
        this.ctx = ctx;
        this.list = list;
    }


    @NonNull
    @Override
    public AdapterModalidade.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_modalidade, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterModalidade.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView descricao ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            descricao = itemView.findViewById(R.id.descricao) ;
        }

        public void bind(Modalidade m){
            descricao.setText(m.getDescricao());
        }

        @Override
        public void onClick(View view) {
            selecionado(list.get(getAdapterPosition()));
        }
    }

    public abstract void selecionado(Modalidade m) ;
}
