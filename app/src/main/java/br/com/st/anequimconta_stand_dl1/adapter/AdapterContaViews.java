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
import br.com.st.anequimconta_stand_dl1.entidade.ContaView;
import br.com.st.anequimconta_stand_dl1.impressao.ContaViewBitMap;

import java.util.List;

public abstract class AdapterContaViews extends RecyclerView.Adapter<AdapterContaViews.ViewHolder> {
    private Activity ctx ;
    private List<ContaView> list ;

    public AdapterContaViews(Activity ctx, List<ContaView> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterContaViews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_conta_bitmap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContaViews.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FloatingActionButton excluir ;
        private FloatingActionButton imprimir ;

        private ImageView image_conta_view ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_conta_view = itemView.findViewById(R.id.image_conta_view) ;
            excluir= itemView.findViewById(R.id.excluir) ;
            imprimir= itemView.findViewById(R.id.imprimir) ;
            excluir.setOnClickListener(view -> {excluir(list.get(getAdapterPosition()));});
            imprimir.setOnClickListener(view -> {imprimir(list.get(getAdapterPosition()));});
        }

        public void bind(ContaView cv){
            image_conta_view.setImageBitmap(new ContaViewBitMap(ctx, cv).getBitMap());
        }

    }

    public abstract void imprimir(ContaView contaView) ;
    public abstract void excluir(ContaView contaView) ;
}
