package br.com.st.anequimconta_stand_dl1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.util.ConfiguracoesOperacionais;

public class AdapterOperacional extends RecyclerView.Adapter<AdapterOperacional.ViewHolder> {

    private Activity ctx ;
    private ConfiguracoesOperacionais.operacao[] list ;

    public AdapterOperacional(Activity ctx) {
        this.ctx = ctx;
        this.list = ConfiguracoesOperacionais.operacao.values();
    }

    @NonNull
    @Override
    public AdapterOperacional.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_operacional, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOperacional.ViewHolder holder, int position) {
        holder.bind(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox select ;
        private TextView descricao ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            select        = itemView.findViewById(R.id.select) ;
            descricao     = itemView.findViewById(R.id.descricao) ;
            select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                  ConfiguracoesOperacionais.setConfOperacional(ctx,list[getAdapterPosition()], b );
                }
            });
        }

        public void bind(ConfiguracoesOperacionais.operacao o){
            select.setChecked(ConfiguracoesOperacionais.getConfOperacional(ctx, o));
            descricao.setText(o.valor[1]);
        }
    }

}
