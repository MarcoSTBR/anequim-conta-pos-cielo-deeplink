package br.com.st.anequimconta_stand_dl1.pospag.paystone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.st.anequimconta_stand_dl1.R;

public class AdapterOpcaoStone extends RecyclerView.Adapter<AdapterOpcaoStone.ItensHold> {

    private Context ctx;
    private List<OpcaoPagamentoStone> list ;
    private OnSelectOpcaoStone onSelectOpcaoStone ;

    public AdapterOpcaoStone(Context ctx, List<OpcaoPagamentoStone> list, OnSelectOpcaoStone onSelectOpcaoStone) {
        this.ctx = ctx;
        this.list = list;
        this.onSelectOpcaoStone = onSelectOpcaoStone;
    }

    @NonNull
    @Override
    public ItensHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_grade_cartao_credito, parent, false) ;
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

    public class ItensHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView descricao ;

        public ItensHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            descricao = itemView.findViewById(R.id.textOp) ;
        }

        public void bind(OpcaoPagamentoStone op){
            descricao.setText(op.getDescricao()) ;
        }

        @Override
        public void onClick(View v) {
            onSelectOpcaoStone.selectionado(list.get(getAdapterPosition()));
        }
    }


}