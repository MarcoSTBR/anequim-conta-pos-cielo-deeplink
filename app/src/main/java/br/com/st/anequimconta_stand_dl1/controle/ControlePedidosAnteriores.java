package br.com.st.anequimconta_stand_dl1.controle;

import br.com.st.anequimconta_stand_dl1.entidade.PedidoView;

import java.util.ArrayList;
import java.util.List;

public class ControlePedidosAnteriores {

    private static ControlePedidosAnteriores controlePedidosAnteriores ;

    public static ControlePedidosAnteriores getControlePedidosAnteriores(){
        if (controlePedidosAnteriores == null) {
            controlePedidosAnteriores = new ControlePedidosAnteriores();
            return controlePedidosAnteriores;
        } else return controlePedidosAnteriores;
    }

    private List<PedidoView> numeros ;

    public ControlePedidosAnteriores(){
        numeros = new ArrayList<>() ;
    }

    public List<PedidoView> getNumeros() {
        return numeros;
    }

    public void setNumeros(List<PedidoView> numeros) {
        this.numeros = numeros;
    }
}
