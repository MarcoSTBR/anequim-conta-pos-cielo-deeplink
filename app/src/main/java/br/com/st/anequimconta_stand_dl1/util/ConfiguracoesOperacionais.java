package br.com.st.anequimconta_stand_dl1.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ConfiguracoesOperacionais {

    public static void setConfOperacional(Context ctx, operacao item, Boolean valor){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(item.valor[0], valor) ;
        editor.commit() ;
    }

    public static Boolean getConfOperacional(Context ctx, operacao item){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getBoolean(item.valor[0], Boolean.valueOf(item.valor[2]));
    }

    public enum operacao{
        env_clc(new String[]{"env_clc", "Ativar cálculo no envio do cartão personalizado!","false"}),
        lib_dig(new String[]{"lib_dig", "Habilita digitação de Cartão/Mesa!","true"}),
        trn_itn(new String[]{"trn_itn", "Permite transferência de itens entre contas!","false"}),
        num_prd(new String[]{"num_prd", "Vai direto para os produtos depois do pedido!","false"}),
        env_imp(new String[]{"env_imp", "Vai direto para a impressão depois de enviar o pedido!","false"}),
        env_pag(new String[]{"env_pag", "Vai direto para o pagamento depois de enviar o pedido!","false"}),
        pag_per(new String[]{"pag_per", "Perguntar se quer imprimir conta ou NFCe depois do pagamento!","false"}),
        pag_cnt(new String[]{"pag_cnt", "Emitir simples conferência depois do pagamento!","false"}),
        pag_nfc(new String[]{"pag_nfc", "Emitir nota fiscal depois do pagamento!","false"}) ;
        public String[] valor ;
        operacao(String[] valor) {
            this.valor = valor ;
        }
    }

}
