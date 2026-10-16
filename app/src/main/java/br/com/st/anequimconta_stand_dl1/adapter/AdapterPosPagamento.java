package br.com.st.anequimconta_stand_dl1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.pospag.PosPagamentoView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class AdapterPosPagamento extends RecyclerView.Adapter<AdapterPosPagamento.AdapterHolder> {

    private Context ctx ;
    private List<PosPagamentoView> list ;

    public AdapterPosPagamento(Context ctx, List<PosPagamentoView> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_pos_pagamento, parent, false);
        return new AdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        holder.bind(list.get(position)) ;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mBrand;
        public TextView mStatus;
        public TextView mFourDigits;
        public TextView mValue;
        public TextView mDate;
        public AdapterHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mBrand = (TextView) itemView.findViewById(R.id.brand);
            mStatus = (TextView) itemView.findViewById(R.id.status);
            mFourDigits = (TextView) itemView.findViewById(R.id.four_digits);
            mValue = (TextView) itemView.findViewById(R.id.value);
            mDate = (TextView) itemView.findViewById(R.id.date);
        }

        public void bind(PosPagamentoView pag) {

            mBrand.setText(pag.getBandeiraNome() /*Card().getBrand()*/);
            mStatus.setText(pag.getDescricaoStatus() /* getPaymentStatus().name()*/);
            mFourDigits.setText(String.format("BIN **** %s", pag.getLast4Digits() /* getCard().getPanLast4Digits()*/));
            mDate.setText(new SimpleDateFormat("dd/MM hh'h'mm").format(pag.getData() /*getPaymentDate()*/));
            NumberFormat currFormat = NumberFormat.getCurrencyInstance();
            mValue.setText(currFormat.format(pag.getValor()    /*payment.getValue().doubleValue()*/));
        }

        @Override
        public void onClick(View view) {
            selecionado(list.get(getAdapterPosition()));
        }
    }

    public abstract void selecionado(PosPagamentoView pag) ;
}
