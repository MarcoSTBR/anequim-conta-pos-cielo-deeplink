package br.com.st.anequimconta_stand_dl1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.dao.ModalidadeDao;
import br.com.st.anequimconta_stand_dl1.entidade.ContaPag;
import br.com.st.anequimconta_stand_dl1.entidade.Modalidade;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AdapterContaPagamentos extends RecyclerView.Adapter<AdapterContaPagamentos.ViewHolder> {

    private Activity ctx ;
    private List<ContaPag> list ;

    public AdapterContaPagamentos(Activity ctx, List<ContaPag> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterContaPagamentos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_resumo_pagamento, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContaPagamentos.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageButton excluir_pagamento ;
        private TextView descricao ;
        private TextView valor ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            excluir_pagamento = itemView.findViewById(R.id.excluir_pagamento) ;
            descricao = itemView.findViewById(R.id.descricao) ;
            valor     = itemView.findViewById(R.id.valor) ;
            excluir_pagamento.setOnClickListener(view -> {
                cancelar(list.get(getAdapterPosition()));
            });
        }

        public void bind(ContaPag cp){
            Modalidade modalidade = ModalidadeDao.getModalidadeDao(ctx).getModalidade(String.valueOf(cp.getMODALIDADE_ID())) ;
            descricao.setText(modalidade.getDescricao());
            valor.setText(new DecimalFormat("R$ #0.00").format(cp.getVALOR()));
        }
    }

    public abstract void cancelar(ContaPag canPag) ;
}
