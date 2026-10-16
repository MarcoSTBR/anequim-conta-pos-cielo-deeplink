package br.com.st.anequimconta_stand_dl1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.entidade.ContaViewPagView;
import br.com.st.anequimconta_stand_dl1.impressao.ContaViewPagBitMap;

import java.util.List;

public abstract class AdapterContasPagas extends RecyclerView.Adapter<AdapterContasPagas.ViewHolder> {

    private Activity ctx ;
    private List<ContaViewPagView> list ;

    public AdapterContasPagas(Activity ctx, List<ContaViewPagView> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterContasPagas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_contas_pagas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContasPagas.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private ImageView image_conta_view ;
    private FloatingActionButton imprimir_conta ;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_conta_view = itemView.findViewById(R.id.image_conta_view) ;
            imprimir_conta   = itemView.findViewById(R.id.imprimir_conta) ;
            imprimir_conta.setOnClickListener(view-> selecionado(list.get(getAdapterPosition())));
        }
        public void bind(ContaViewPagView it){
            ContaViewPagBitMap contaViewPagBitMap = new ContaViewPagBitMap(ctx, it) ;
            image_conta_view.setImageBitmap(contaViewPagBitMap.getBitMap());
        }
    }

    public abstract void selecionado(ContaViewPagView it) ;
}
