package br.com.st.anequimconta_stand_dl1.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.adapter.AdapterAmbiente;
import br.com.st.anequimconta_stand_dl1.adapter.listeners.ListenerAdapterAmbiente;
import br.com.st.anequimconta_stand_dl1.controle.ControleComandaRemota;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoEnvio;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidoView;
import br.com.st.anequimconta_stand_dl1.controle.ControlePedidosAnteriores;
import br.com.st.anequimconta_stand_dl1.controle.ControleTicket;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleComandaRemota;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.controle.listeners.OnControleTicket;
import br.com.st.anequimconta_stand_dl1.dao.AmbienteDao;
import br.com.st.anequimconta_stand_dl1.daoclass.DaoFilters;
import br.com.st.anequimconta_stand_dl1.databinding.ActivityEnvioPedidosBinding;
import br.com.st.anequimconta_stand_dl1.entidade.Ambiente;
import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

import org.json.JSONArray;

import java.util.List;

public class ActivityEnvioPedidos extends AppCompatActivity implements OnControleEnvioPedidos, OnControleTicket, ListenerAdapterAmbiente {

    private final String TAG = "enviarPedido" ;
    private ActivityEnvioPedidosBinding binding ;
    private Ambiente ambiente ;
    private List<PedidoView> list ;
    private List<Ambiente> listAmbiente ;
    private JSONArray req ;
    private ControleTicket controleTicket ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnvioPedidosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        controleTicket =  new ControleTicket(ActivityEnvioPedidos.this, this) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.txtmensagem.setText("Selecione o Ambiente!");
        setAmbiente() ;
    }

    private void setAmbiente(){
        Log.i(TAG, "setAmbiente") ;
        DaoFilters f = new DaoFilters() ;
        listAmbiente = AmbienteDao.getAmbienteDao(this).getListAll().getAll(f, "id") ;
        Log.i(TAG, "setAmbiente "+listAmbiente.size()) ;
        binding.gradeAmbiente.setLayoutManager(new LinearLayoutManager(this));
        binding.gradeAmbiente.setAdapter(new AdapterAmbiente(this, listAmbiente, this));
        if (listAmbiente.size() == 0) {
            ambiente = new Ambiente(1, "00001", "ÚNICO");
            enviar();
        }
    }

    @Override
    public void selected(Ambiente a) {
        ambiente = a ;
        Log.i(TAG, "selected "+ambiente.getDESCRICAO()) ;
        enviar();
    }

    private void enviar() {
        list = new ControlePedidoView(this).getPedidos() ;
        if (verifica())
            new ControlePedidoEnvio(this, ambiente, list, this).enviar();
        else alert("Envio", "Não há itens para envio ou pedido vazio!");
    }


    private boolean verifica(){
        if (list.size() > 0){
            for (PedidoView p :list){
                if (p.getPedidoItemView().size() == 0)
                    return false;
            }
            return true ;
        } else return false ;
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
        controleTicket.enviarRequisicao(req);
//        controleComandaRemota.requisicao(req);
    }

    @Override
    public void impTicketOk() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_notification)
                .setTitle("Envie")
                .setMessage("Pedidos Enviados!")
                .setCancelable(false)
                .setPositiveButton("OK", ((dialogInterface, i) -> {
                    setResult(RESULT_OK);
                    finish() ;
                }))
                .show();
    }

    @Override
    public void impTicketChange(String msg, Bitmap bitMap) {
        binding.txtmensagem.setText(msg);
//        if (bitMap != null)
//            binding.bitmensagem.setImageBitmap(bitMap);
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