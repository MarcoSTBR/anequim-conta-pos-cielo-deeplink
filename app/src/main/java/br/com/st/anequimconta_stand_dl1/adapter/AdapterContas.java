package br.com.st.anequimconta_stand_dl1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.entidade.Conta;

import java.util.List;

public abstract class AdapterContas extends RecyclerView.Adapter<AdapterContas.ViewHolder> {

    private Activity ctx ;
    private List<Conta> list ;

    public AdapterContas(Activity ctx, List<Conta> list) {
        this.ctx = ctx;
        this.list = list;
    }


    @NonNull
    @Override
    public AdapterContas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_painel_conta, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContas.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView situacao ;
        private TextView numero ;
        private TextView num_impressao ;
        private TextView num_pessoas ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            situacao        = itemView.findViewById(R.id.situacao) ;
            numero        = itemView.findViewById(R.id.numero) ;
            num_impressao = itemView.findViewById(R.id.num_impressao) ;
            num_pessoas   = itemView.findViewById(R.id.num_pessoas) ;
        }

        public void bind(Conta c){
            situacao.setText((c.getSTATUS() == 1) ? "Aberto" : "Fechado");
            if (c.getNUMERO().equals(c.getREFERENCIA()))
                numero.setText(c.getNUMERO());
             else numero.setText(c.getNUMERO()+" ["+c.getREFERENCIA()+"]");

            num_impressao.setText(String.valueOf(c.getNUM_IMPRESSAO()));
            num_pessoas.setText(String.valueOf(c.getNUM_PESSOAS()));

        }


        @Override
        public void onClick(View view) {
            selecionado(list.get(getAdapterPosition())) ;
        }
    }

    public abstract void selecionado(Conta conta) ;
}
