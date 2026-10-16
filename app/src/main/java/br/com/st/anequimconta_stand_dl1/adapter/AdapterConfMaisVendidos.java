package br.com.st.anequimconta_stand_dl1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.controle.ControleMaisVendidos;
import br.com.st.anequimconta_stand_dl1.entidade.GradeView;

import java.util.List;


public abstract class AdapterConfMaisVendidos extends RecyclerView.Adapter<AdapterConfMaisVendidos.ViewHolder> {

    private Context ctx ;
    private List<GradeView> list ;

    public AdapterConfMaisVendidos(Context ctx, List<GradeView> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_conf_grade_mais_vendidos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView descricao;
        private Button selecionar ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.descricao) ;
            selecionar = itemView.findViewById(R.id.selecionar) ;
            selecionar.setOnClickListener(this);
        }

        public void bind(GradeView g){
            descricao.setText(g.getGrade().getDescricao());
            if (ControleMaisVendidos.getGradeID(ctx) == g.getGrade().getId())
                selecionar.setVisibility(View.GONE);
             else selecionar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
            selecionado(list.get(getAdapterPosition())) ;
        }
    }

    public abstract void selecionado(GradeView g) ;
}
