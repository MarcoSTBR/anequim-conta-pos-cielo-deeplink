package br.com.st.anequimconta_stand_dl1.controle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import br.com.st.anequimconta_stand_dl1.R;

import java.text.DecimalFormat;


public abstract class ControleRecebeValor extends Dialog {

    private AlertDialog.Builder builder;
    private TextView valor ;
    private TextView titulo ;
    private DecimalFormat vdisplay ;
    private int numero = 0 ;
    private int decimal ;
    private Button b9 ;
    private Button b8 ;
    private Button b7 ;
    private Button b6 ;
    private Button b5 ;
    private Button b4 ;
    private Button b3 ;
    private Button b2 ;
    private Button b1 ;
    private Button b0;
    private ImageButton bApagar ;
    private ImageButton bEntrar ;

    public ControleRecebeValor(@NonNull Context ctx, String texto) {
        super(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_valor, null);
        setParam(view) ;
        decimal = 2 ;
        vdisplay = new DecimalFormat("R$ #0.00") ;
        titulo.setText(texto);
        displayNumero();
        builder = new AlertDialog.Builder(ctx);
        builder.setView(view)
                .setPositiveButton("Sim", (dialogInterface, i) -> validade())
                .setNegativeButton("Não", (dialogInterface, i) -> erro("Operação Cancelada!")
                );
        builder.show();
    }

    public ControleRecebeValor(@NonNull Context ctx, String texto, String mask, int decimal) {
        super(ctx);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_valor, null);
        setParam(view) ;
        this.decimal = decimal ;
        vdisplay = new DecimalFormat(mask) ;
        titulo.setText(texto);
        displayNumero();
        builder = new AlertDialog.Builder(ctx);
        builder.setView(view)
                .setPositiveButton("Sim", (dialogInterface, i) -> validade())
                .setNegativeButton("Não", (dialogInterface, i) -> erro("Operação Cancelada!")
                );
        builder.show();
    }

    private void setParam(View view){
        b9 = view.findViewById(R.id.b9);
        b8 = view.findViewById(R.id.b8);
        b7 = view.findViewById(R.id.b7);
        b6 = view.findViewById(R.id.b6);
        b5 = view.findViewById(R.id.b5);
        b4 = view.findViewById(R.id.b4);
        b3 = view.findViewById(R.id.b3);
        b2 = view.findViewById(R.id.b2);
        b1 = view.findViewById(R.id.b1);
        b0 = view.findViewById(R.id.b0);
        bApagar = view.findViewById(R.id.bApagar);
        bEntrar = view.findViewById(R.id.bEntrar);
        b0.setOnClickListener(v-> addNumero ("0"));
        b1.setOnClickListener(v-> addNumero ("1"));
        b2.setOnClickListener(v-> addNumero ("2"));
        b3.setOnClickListener(v-> addNumero ("3"));
        b4.setOnClickListener(v-> addNumero ("4"));
        b5.setOnClickListener(v-> addNumero ("5"));
        b6.setOnClickListener(v-> addNumero ("6"));
        b7.setOnClickListener(v-> addNumero ("7"));
        b8.setOnClickListener(v-> addNumero ("8"));
        b9.setOnClickListener(v-> addNumero ("9"));
        bEntrar.setOnClickListener(v-> {
           // this.dismiss();
          //  validade();
        });
        bApagar.setOnClickListener(v-> apagar());
        valor = view.findViewById(R.id.valor);
        titulo = view.findViewById(R.id.label_titulo_valor) ;
    }

    private String getMascara(int d){
        String ms = "#0" ;
        if (d > 0){
          String z = "0" ;
           ms += "."+ z.repeat(d) ;
        }
        Log.i("valor_", "mascara "+ms) ;
        return ms ;
    }

    private void addNumero(String t){
        try {
            String vnumero = String.valueOf(numero) ;
            numero = Integer.valueOf( vnumero + t);
            displayNumero();
        } catch (java.lang.NumberFormatException e){
            e.printStackTrace();
            displayNumero();
        }
    }

    private void apagar() {
        String s = String.valueOf(numero) ;
        if (s.length() > 1) {
            s = s.substring(0, s.length()-1) ;
        } else if (s.length() <= 1) {
            s = "0" ;
        }
        numero = Integer.valueOf(s) ;
        displayNumero();
    }

    private void displayNumero(){
        Double dc = Math.pow (10, decimal) ;
//        Log.i("valor_", "decimal "+dc) ;
//        Log.i("valor_", "numero "+numero) ;
//        Log.i("valor_", "valorfinal "+numero / dc) ;
        valor.setText(vdisplay.format(numero / dc));
    }

    private void validade() {
        Double dc = Math.pow (10, decimal) ;
//        Log.i("valor_", "valorfinal "+numero / dc) ;
        valorDigitado(numero / dc);
    }

    public abstract void valorDigitado(Double v) ;
    public abstract void erro(String msg) ;
}
