package br.com.st.anequimconta_stand_dl1.controle.listeners;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

import org.json.JSONArray;

import java.util.List;

public interface OnControleEnvioPedidos {
    void envioOk(JSONArray req, List<PedidoView> l) ;
    void envioChange(String msg) ;
    void envioErro(String msg) ;
}
