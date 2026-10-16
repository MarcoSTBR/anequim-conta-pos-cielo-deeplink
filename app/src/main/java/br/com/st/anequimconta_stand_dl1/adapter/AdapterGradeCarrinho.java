package br.com.st.anequimconta_stand_dl1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.dao.ProdutoDao;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemAdd;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoItemView;
import br.com.st.anequimconta_stand_dl1.entidade.Produto;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AdapterGradeCarrinho extends RecyclerView.Adapter<AdapterGradeCarrinho.AdapterHolder> {
    private Context ctx;
    private List<PedidoItemView> list;

    public AdapterGradeCarrinho(Context ctx, List<PedidoItemView> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_grade_carrinho, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder {
        private TextView descricao;
        private TextView produto_add;
        private TextView quantidade_preco;
        private ImageButton mais ;
        private ImageButton menos ;
        public AdapterHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.descricao);
            produto_add = itemView.findViewById(R.id.produto_add);
            quantidade_preco = itemView.findViewById(R.id.quantidade_preco);
            mais = itemView.findViewById(R.id.mais) ;
            mais.setOnClickListener((View) -> {
                setMais(list.get(getAdapterPosition()));
            });
            menos = itemView.findViewById(R.id.menos) ;
            menos.setOnClickListener((View) -> {
                setMenos(list.get(getAdapterPosition()));
            });
        }

        public void bind(PedidoItemView it) {
            Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(it.getPedidoItem().getPRODUTO_ID());
            descricao.setText(p.getDescricao());
            Log.i("produto_", p.getUnidade().substring(0,1)) ;
            if ((p.getUnidade().substring(0,1).equals("K")) || (p.getUnidade().substring(0,1).equals("L")))
                mais.setVisibility(View.GONE);
            else mais.setVisibility(View.VISIBLE);

            setValores(it, p);
            setProdutoAdd(it, p);
        }

        private void setMais(PedidoItemView it) {
            Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(it.getPedidoItem().getPRODUTO_ID());
            it.getPedidoItem().setQUANTIDADE(it.getPedidoItem().getQUANTIDADE() + 1);
            it.getPedidoItem().setVALOR(it.getPedidoItem().getQUANTIDADE() * it.getPedidoItem().getPRECO());
            Double comissao =  it.getPedidoItem().getVALOR() * (p.getComissao() / 100) ;
            it.getPedidoItem().setCOMISSAO(comissao);
            notifyDataSetChanged();
            onChange(it) ;
        }
        private void setMenos(PedidoItemView it) {
            Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(it.getPedidoItem().getPRODUTO_ID());
            if (((it.getPedidoItem().getQUANTIDADE() -1) == 0) ||
               (p.getUnidade().substring(0,1).equals("K")) ||
               (p.getUnidade().substring(0,1).equals("L"))){
                 excluirItem(it) ;
            } else {
                Double comissao =  it.getPedidoItem().getVALOR() * (p.getComissao() / 100) ;
                it.getPedidoItem().setQUANTIDADE(it.getPedidoItem().getQUANTIDADE() - 1);
                it.getPedidoItem().setVALOR(it.getPedidoItem().getQUANTIDADE() * it.getPedidoItem().getPRECO());
                it.getPedidoItem().setCOMISSAO(comissao);
                notifyDataSetChanged();
                onChange(it);
            }
        }

        private void setValores(PedidoItemView it, Produto p) {
            DecimalFormat frv = new DecimalFormat("R$ #0.00");
            String q = new DecimalFormat("#0").format(it.getPedidoItem().getQUANTIDADE());
            if (p.getUnidade().substring(0,1).equals("K") || p.getUnidade().substring(0,1).equals("L"))
                q = new DecimalFormat("#0.000").format(it.getPedidoItem().getQUANTIDADE());
            String valor = frv.format(it.getPedidoItem().getPRECO()) + " = " + frv.format(it.getPedidoItem().getVALOR());
            String txt = q + " X " + valor;
            Double vlAdd = valorAdd(it) ;
            if (vlAdd > 0) {
                String aux = "+ "+frv.format(vlAdd)+ " = "+frv.format(vlAdd + it.getPedidoItem().getVALOR()) ;
                txt += "\n" + aux ;
            }
            quantidade_preco.setText(txt);
        }

        private Double valorAdd(PedidoItemView it){
            Double vl = 0.00 ;
            for (PedidoItemAdd itemAdd : it.getPedidoItemAdd()){
               vl += it.getPedidoItem().getQUANTIDADE() * itemAdd.getVALOR() ;
            }
            return vl ;
        }

        private void setProdutoAdd(PedidoItemView it, Produto prd) {
            DecimalFormat frv = new DecimalFormat("R$ #0.00");
            DecimalFormat frq = null ;
            if (prd.getUnidade().substring(0,1).equals("K") || prd.getUnidade().substring(0,1).equals("L"))
                frq = new DecimalFormat("#0.000");
              else
               frq = new DecimalFormat("#0");
            if (it.getPedidoItemAdd().size() > 0) {
                String txt = "" ;
                for (PedidoItemAdd itAdd : it.getPedidoItemAdd()) {
                    Produto p = ProdutoDao.getProdutoDao(ctx).getProduto(itAdd.getPRODUTO_ID()) ;
                    String aux = "+ "+frq.format(itAdd.getQUANTIDADE() * it.getPedidoItem().getQUANTIDADE()) + "  " + p.getDescricao() ;
                    if (itAdd.getVALOR() > 0) {
                        aux += " "+ frv.format(it.getPedidoItem().getQUANTIDADE() * itAdd.getVALOR()) ;
                    }
                    if (txt.length() == 0)
                        txt += aux ;
                     else txt += "\n"+aux ;
                }
                produto_add.setVisibility(View.VISIBLE);
                produto_add.setText(txt);
            } else produto_add.setVisibility(View.GONE);

        }
    }
    public abstract void onChange(PedidoItemView it) ;
    public abstract void excluirItem(PedidoItemView it) ;
}
