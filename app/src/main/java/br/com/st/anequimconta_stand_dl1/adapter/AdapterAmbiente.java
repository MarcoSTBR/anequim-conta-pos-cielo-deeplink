package br.com.st.anequimconta_stand_dl1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.listeners.ListenerAdapterAmbiente;
import br.com.st.anequimconta_stand_dl1.entidade.Ambiente;

public class AdapterAmbiente extends RecyclerView.Adapter<AdapterAmbiente.AmbienteHold>{

    private Context ctx ;
    private List<Ambiente> list ;
    private ListenerAdapterAmbiente listenerAdapterAmbiente ;

    public AdapterAmbiente(Context ctx, List<Ambiente> list, ListenerAdapterAmbiente listenerAdapterAmbiente) {
        this.ctx = ctx;
        this.list = list;
        this.listenerAdapterAmbiente = listenerAdapterAmbiente ;
    }

    @NonNull
    @Override
    public AmbienteHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_grade_ambiente, parent, false) ;
        return new AmbienteHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmbienteHold holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AmbienteHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text ;

        public AmbienteHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            text = itemView.findViewById(R.id.ambiente_desc) ;
        }

        public void bind(Ambiente a){
            text.setText(a.getDESCRICAO());
        }

        @Override
        public void onClick(View v) {
            listenerAdapterAmbiente.selected(list.get(getAdapterPosition()));
        }
    }
}
