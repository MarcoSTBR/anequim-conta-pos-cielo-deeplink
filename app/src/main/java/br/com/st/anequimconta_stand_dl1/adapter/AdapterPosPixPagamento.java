package br.com.st.anequimconta_stand_dl1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoPixView;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AdapterPosPixPagamento extends RecyclerView.Adapter<AdapterPosPixPagamento.AdapterHolder> {

    private Context ctx ;
    private List<PosPagamentoPixView> list ;

    public AdapterPosPixPagamento(Context ctx, List<PosPagamentoPixView> list) {
        this.ctx = ctx;
        this.list = list;
    }


    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_pos_pix_pagamento, parent, false);
        return new AdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView pag_id ;
        private TextView pag_valor ;
        private TextView pag_status ;

        public AdapterHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            pag_id = itemView.findViewById(R.id.pag_id) ;
            pag_valor = itemView.findViewById(R.id.pag_valor) ;
            pag_status = itemView.findViewById(R.id.pag_status) ;
        }

        public void bind(PosPagamentoPixView it){
            DecimalFormat frv  = new DecimalFormat("R$ #0.00");
            pag_id.setText("Identificação: "+it.getTx_id().substring(1,5)) ;
            pag_valor.setText("Valor: "+frv.format(it.getCob_value()));
            pag_status.setText(" "+it.getStatus());
        }

        @Override
        public void onClick(View view) {
            selecionarOpcao(list.get(getAdapterPosition())) ;
        }
    }

    public abstract void selecionarOpcao(PosPagamentoPixView it) ;
}
