package br.com.st.anequimconta_stand_dl1.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.controle.ControleComandaRemota;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoEnvio;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoView;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidosAnteriores;
import br.com.st.anequimconta_stand_dl1.controle.ControleTicket;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleComandaRemota;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleTicket;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityEnvioPedidosBinding;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

import org.json.JSONArray;

import java.util.List;

public class ActivityEnvioPedidos extends AppCompatActivity implements OnControleEnvioPedidos, OnControleTicket, OnControleComandaRemota {

    private ActivityEnvioPedidosBinding binding ;
    private List<PedidoView> list ;
    private JSONArray req ;
    private ControleTicket controleTicket ;
    private ControleComandaRemota controleComandaRemota ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnvioPedidosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        controleTicket =  new ControleTicket(ActivityEnvioPedidos.this, this) ;
        controleComandaRemota = new ControleComandaRemota(ActivityEnvioPedidos.this, this) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.txtmensagem.setText("Aguarde...");
        enviar() ;
    }

    private void enviar() {
        list = new ControlePedidoView(this).getPedidos() ;
        if (verifica())
          new ControlePedidoEnvio(this, list, this).enviar();
        else alert("Envio", "Não há itens para envio!");
    }
    private boolean verifica(){
        boolean b = false ;
        for (PedidoView p :list){
            if (p.getPedidoItemView().size() > 0)
                b = true;
        }
        return b ;
    }

    @Override
    public void envioChange(String msg) {
        binding.txtmensagem.setText(msg);
    }

    @Override
    public void envioErro(String msg) {
        alert("Envio", msg);
    }

    @Override
    public void envioOk(JSONArray req, List<PedidoView> l) {
        this.req = req ;
        ControlePedidosAnteriores.getControlePedidosAnteriores().setNumeros(l);
       // controleTicket.enviarRequisicao(req);
        controleComandaRemota.requisicao(req);
    }

    @Override
    public void comandaChange(String msg, Bitmap bitmap) {
        binding.txtmensagem.setText(msg);
        if (bitmap != null)
            binding.bitmensagem.setImageBitmap(bitmap);
    }

    @Override
    public void comandaErro(String msg) {
        alert("Comanda Remota", msg) ;
    }

    @Override
    public void comandaOk() {
        controleTicket.enviarRequisicao(req);
    }

    @Override
    public void impTicketOk() {
        setResult(RESULT_OK);
        finish();
/*
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Envie")
                .setMessage("Pedidos Enviados!")
                .setCancelable(false)
                .setPositiveButton("OK", ((dialogInterface, i) -> finish()))
                .show();
*/
    }

    @Override
    public void impTicketChange(String msg, Bitmap bitMap) {
       // if (bitMap != null)
        binding.txtmensagem.setText(msg);
        if (bitMap != null)
            binding.bitmensagem.setImageBitmap(bitMap);
    }

    @Override
    public void impTicketErro(String msg) {
        alert("Ticket", msg) ;
    }

    private void alert(String titulo, String msg) {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle(titulo)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", ((dialogInterface, i) -> finish()))
                .show();
    }
}