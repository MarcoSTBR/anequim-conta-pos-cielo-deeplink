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
import br.com.st.anequimconta_stand_dl1.adapter.listeners.ListenerAdapterPedidoCorrente;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterPedidoCorrente extends RecyclerView.Adapter<AdapterPedidoCorrente.ViewHolder> {

    private Activity ctx ;
    private List<PedidoView> list ;
    private ListenerAdapterPedidoCorrente listenerAdapterPedidoCorrente ;

    public AdapterPedidoCorrente(Activity ctx, List<PedidoView> list, ListenerAdapterPedidoCorrente listenerAdapterPedidoCorrente) {
        this.ctx = ctx;
        this.list = list;
        this.listenerAdapterPedidoCorrente = listenerAdapterPedidoCorrente;
    }

    @NonNull
    @Override
    public AdapterPedidoCorrente.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_pedido_corrente, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPedidoCorrente.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageButton bt_delete ;
        private ImageButton bt_ir ;
        private TextView num_pedido ;
        private TextView num_itens ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            num_pedido= itemView.findViewById(R.id.num_pedido) ;
            num_itens = itemView.findViewById(R.id.num_itens) ;
            bt_delete = itemView.findViewById(R.id.bt_delete) ;
            bt_ir     = itemView.findViewById(R.id.bt_ir) ;
            bt_delete.setOnClickListener(view -> listenerAdapterPedidoCorrente.apagarPedido(list.get(getAdapterPosition())));
            bt_ir.setOnClickListener(view-> listenerAdapterPedidoCorrente.pedidoSelecionado(list.get(getAdapterPosition())));
        }

        public void bind(PedidoView pw){
            DecimalFormat f = new DecimalFormat("#0") ;
            if (pw.getPedido().getCORRENTE() == 1)
                num_pedido.setText("* "+pw.getPedido().getNUMERO());
            else  num_pedido.setText(pw.getPedido().getNUMERO());
            num_itens.setText("Iten(s) "+f.format(pw.getQuantidades()));
        }
    }
}
