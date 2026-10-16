package br.com.st.anequimconta_stand_dl1.emissao_nfce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.emissao_nfce.listener.ListenerClienteDestinatario;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultDest;
import br.com.st.anequimconta_stand_dl1.entidade.ContaMultNFCe;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class Cliente_Destinatario_NFce {

    private Context ctx ;
    private EditText cpf_cnpj_dest ;
    private EditText nome_dest ;
    private Button cancelar_dest ;
    private Button ok_dest ;
    private ContaMultDest contaMultDest;
    private ContaMultNFCe contaMultNFCe;
    private ListenerClienteDestinatario listenerClienteDestinatario ;
    private AlertDialog.Builder myBuiler ;
    private AlertDialog janela ;

    public Cliente_Destinatario_NFce(Context ctx, ContaMultNFCe contaMultNFCe, ContaMultDest contaMultDest, ListenerClienteDestinatario listenerClienteDestinatario){
        this.ctx = ctx ;
        this.contaMultNFCe = contaMultNFCe;
        this.contaMultDest = contaMultDest;
        this.listenerClienteDestinatario = listenerClienteDestinatario ;
        bind() ;
    }

    private void bind(){
        myBuiler = new AlertDialog.Builder(ctx) ;
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_cliente_destinatario, null);
        cpf_cnpj_dest = view.findViewById(R.id.cpf_cnpj_dest) ;
        nome_dest     = view.findViewById(R.id.nome_dest) ;
        cancelar_dest = view.findViewById(R.id.cancelar_dest) ;
        cancelar_dest.setOnClickListener(v -> {
            janela.dismiss();
            listenerClienteDestinatario.cancelar("A Nota Não Foi Emitida!");
        });
        ok_dest = view.findViewById(R.id.ok_dest) ;
        ok_dest.setOnClickListener(v -> {
            janela.dismiss();
            if (cpf_cnpj_dest.getText().length() > 0){
                contaMultDest = new ContaMultDest(0, UtilSet.getUUID(), contaMultNFCe.getUUID(),cpf_cnpj_dest.getText().toString(), nome_dest.getText().toString(), 1) ;
            }
            listenerClienteDestinatario.ok(contaMultNFCe);
        });
        myBuiler.setView(view) ;

    }

    public void executar(){
        janela = myBuiler.create() ;
        janela.show();
    }



}
