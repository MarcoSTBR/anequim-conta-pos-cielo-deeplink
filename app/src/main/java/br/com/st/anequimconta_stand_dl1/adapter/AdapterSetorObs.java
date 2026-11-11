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
import br.com.st.anequimconta_stand_dl1.entidade.SetorObs;

public abstract class AdapterSetorObs extends RecyclerView.Adapter<AdapterSetorObs.SetorObsHold>{

    private Context ctx ;
    private List<SetorObs> list ;

    public AdapterSetorObs(Context ctx, List<SetorObs> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public SetorObsHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_grade_obs, parent, false) ;
        return new SetorObsHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetorObsHold holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SetorObsHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text ;

        public SetorObsHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            text = itemView.findViewById(R.id.obs) ;
        }

        public void bind(SetorObs a){
            text.setText(a.getOBSERVACAO());
        }

        @Override
        public void onClick(View v) {
            setorObsOk(list.get(getAdapterPosition()));
        }
    }

    public abstract void setorObsOk(SetorObs setorObs) ;
}
