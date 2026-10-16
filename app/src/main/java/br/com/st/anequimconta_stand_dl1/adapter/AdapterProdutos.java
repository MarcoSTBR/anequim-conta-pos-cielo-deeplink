package br.com.st.anequimconta_stand_dl1.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;


import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.listeners.ListenerAdapterProduto;
import br.com.st.anequimconta_stand_dl1.controle.ControleAcompanhamento;
import br.com.st.anequimconta_stand_dl1.controle.ControleGradeProduto;
import br.com.st.anequimconta_stand_dl1.controle.ControleRecebeValor;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleGradeProduto;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.ProdutoHoder> {
    private Activity ctx ;
    private List<Produto> list ;
    private ControleGradeProduto controleGradeProduto ;
    private ListenerAdapterProduto listenerAdapterProduto ;

    public AdapterProdutos(Activity ctx, List<Produto> list, ListenerAdapterProduto listenerAdapterProduto) {
        this.ctx = ctx;
        this.list = list;
        this.listenerAdapterProduto = listenerAdapterProduto ;
        controleGradeProduto = new ControleGradeProduto(ctx) ;
        controleGradeProduto.refresh();
    }

    @NonNull
    @Override
    public ProdutoHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_grade_produto, parent, false) ;
        return new ProdutoHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoHoder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProdutoHoder extends RecyclerView.ViewHolder implements View.OnClickListener, ListenerControleGradeProduto {
        private ImageView imagem_produto ;
        private TextView descricao ;
        private TextView preco ;
        private TextView quantidade ;
        public ProdutoHoder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.descricao) ;
            imagem_produto = itemView.findViewById(R.id.imagem_produto) ;
            preco = itemView.findViewById(R.id.preco) ;
            quantidade = itemView.findViewById(R.id.quantidade) ;
            itemView.setOnClickListener(this);
        }
        public void bind(Produto p){
            DecimalFormat fm = new DecimalFormat("R$ #0.00") ;
            preco.setText(fm.format(p.getPreco()));
            descricao.setText(p.getDescricao());
            try {
                if (p.getImagem() != null) {
                    String im = p.getImagem();
                    byte[] bt = Base64.decode(im, 0);
                    Bitmap myBitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                    if (myBitmap != null) {
                        imagem_produto.setImageDrawable(getBitmapArredondado(imagem_produto, myBitmap, 4));
                    }
                }
            } catch (OutOfMemoryError e){
                e.printStackTrace();
            }
            setQuantidade(p) ;
        }

        private void setQuantidade(Produto p){
            Double q = controleGradeProduto.getQuantidade(p) ;
            if (q > 0) {
                quantidade.setVisibility(View.VISIBLE);
                if (p.getUnidade().substring(0,1).equals("K") || p.getUnidade().substring(0,1).equals("L"))
                    quantidade.setText(new DecimalFormat("#0.000").format(q));
                  else  quantidade.setText(new DecimalFormat("#0").format(q));
            } else {
                quantidade.setVisibility(View.GONE);
            }
        }

        private RoundedBitmapDrawable getBitmapArredondado(ImageView img, Bitmap bmp, int cornerScale){
            RoundedBitmapDrawable rdbd = RoundedBitmapDrawableFactory.create(img.getResources(), bmp) ;
            rdbd.setCornerRadius((float)bmp.getWidth() / cornerScale);
            return rdbd ;
        }

        @Override
        public void onClick(View view) {
            Produto p = list.get(getAdapterPosition());
            if(controleGradeProduto.caixaAberto()){
                if (ControleAcompanhamento.getControleAcompanhamento(ctx).list(p).size() > 0) {
                    listenerAdapterProduto.OnAddProduto(p);
                } else {
                    if (p.getUnidade().substring(0,1).equals("K") || p.getUnidade().substring(0,1).equals("L"))
                        setQuantidadeK(p);
                     else
                      controleGradeProduto.addProduto(p, 1.0, this);
                }
            } else listenerAdapterProduto.OnProdutoErro("Caixa Fechado!");
        }

        private void setQuantidadeK(Produto prd){
            new ControleRecebeValor(ctx, "Quantidade ", "#.000", 3) {
                @Override
                public void valorDigitado(Double v) {
                    if (v > 0){
                        setQuantidadekq(prd, v) ;
                    } else listenerAdapterProduto.OnProdutoErro("Valor Inválido!");
                }

                @Override
                public void erro(String msg) {
                    listenerAdapterProduto.OnProdutoErro(msg);
                }
            };
       }

       private void setQuantidadekq(Produto p, Double q){
           controleGradeProduto.addProduto(p, q,this);
       }

        @Override
        public void OnProdutoAdd(Produto p) {
            notifyDataSetChanged();
            listenerAdapterProduto.OnChange(p) ;
        }

        @Override
        public void OnErro(String msg) {
            listenerAdapterProduto.OnProdutoErro(msg);

        }
    }
}
