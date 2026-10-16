package br.com.st.anequimconta_stand_dl1.controle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.com.st.anequimconta_stand_dl1.R;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoUsuarioAcesso;
import br.com.st.anequimconta_stand_dl1.conexao.ConexaoUsuarioLogin;
import br.com.st.anequimconta_stand_dl1.controle.listeners.ListenerControleLiberacaoAcesso;
import br.com.st.anequimconta_stand_dl1.entidade.Usuario;
import br.com.st.anequimconta_stand_dl1.util.UtilSet;

public class ControleLiberacaoAcesso {

    private Activity ctx;
    private int cod_acesso;
    private ListenerControleLiberacaoAcesso listenerControleLiberacaoAcesso;
    private List<DescAcesso> list ;

    public ControleLiberacaoAcesso(Activity ctx, ListenerControleLiberacaoAcesso listenerControleLiberacaoAcesso) {
        this.ctx = ctx;
        this.listenerControleLiberacaoAcesso = listenerControleLiberacaoAcesso;
        criarlista() ;
    }

    public void acesso(int cod_acesso) {
        this.cod_acesso = cod_acesso;
        try {
            new ConexaoUsuarioAcesso(ctx, UtilSet.getUsuarioId(ctx), cod_acesso) {
                @Override
                public void ok(boolean encontrado) {
                    if (encontrado)
                        listenerControleLiberacaoAcesso.acessoOk(UtilSet.getUsuarioId(ctx));
                    else acessoSecundario();
                }

                @Override
                public void erro(String msg) {
                    listenerControleLiberacaoAcesso.acessoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerControleLiberacaoAcesso.acessoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControleLiberacaoAcesso.acessoErro(e.getMessage());
        }
    }

    @SuppressLint("MissingInflatedId")
    private void acessoSecundario() {
        View view = ctx.getLayoutInflater().inflate(R.layout.alert_usuario_acesso, null);
        TextView descricao = view.findViewById(R.id.descricao) ;
        descricao.setText(indexOf(cod_acesso));
        EditText editTextUsuario = (EditText) view.findViewById(R.id.editTextUsuario);
        editTextUsuario.setText("");
        EditText editTextSenha = (EditText) view.findViewById(R.id.editTextSenha);
        editTextSenha.setText("");
        new AlertDialog.Builder(ctx)
                .setView(view)
                .setPositiveButton("Confirmar", (dial, i) -> {
                    verificarUsuarioSecondario(editTextUsuario.getText().toString(), editTextSenha.getText().toString());
                })
                .setNegativeButton("Sair", (dial, i) -> {
                    listenerControleLiberacaoAcesso.acessoErro("Encerrado!");
                })
                .show();
    }

    private void verificarUsuarioSecondario(String login, String senha) {
        try {
            new ConexaoUsuarioLogin(ctx, login, senha) {
                @Override
                public void oK(Usuario usu) {
                    acessarSec(usu.getId());
                }

                @Override
                public void erro(String msg) {
                    listenerControleLiberacaoAcesso.acessoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerControleLiberacaoAcesso.acessoErro(e.getMessage());
        }
/*
        try {
            Usuario usu = UsuarioDao.getUsuarioDao(ctx).getUsuario(login);
            if (usu != null) {
                if (usu.getSENHA().equals(senha))
                    acessarSec(usu.getId());
                else listenerControleLiberacaoAcesso.acessoErro("Senha Incorreta!");
            } else listenerControleLiberacaoAcesso.acessoErro("Usuário Iválido!");
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControleLiberacaoAcesso.acessoErro(e.getMessage());
        }
*/
    }

    private void acessarSec(int cod_usuario) {
        try {
            new ConexaoUsuarioAcesso(ctx, cod_usuario, cod_acesso) {
                @Override
                public void ok(boolean encontrado) {
                    if (encontrado)
                        listenerControleLiberacaoAcesso.acessoOk(UtilSet.getUsuarioId(ctx));
                    else acessoSecundario();
                }

                @Override
                public void erro(String msg) {
                    listenerControleLiberacaoAcesso.acessoErro(msg);
                }
            }.executar();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            listenerControleLiberacaoAcesso.acessoErro(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            listenerControleLiberacaoAcesso.acessoErro(e.getMessage());
        }
    }

    private String indexOf(int i){
        String s = "Desconhecido" ;
        for (DescAcesso a : list)
          if (list.get(i).indice == i)
              s = list.get(i).descricao ;
        return s ;
    }

    private void criarlista(){
        list = new ArrayList<>();
        list.add(new DescAcesso(1,"Abertura de caixa"));
        list.add(new DescAcesso(2,"Suprimento"));
        list.add(new DescAcesso(3,"Retirada"));
        list.add(new DescAcesso(4,"Configuração SAT/NFCE"));
        list.add(new DescAcesso(5,"Venda Vista"));
        list.add(new DescAcesso(6,"Desconto no Item"));
        list.add(new DescAcesso(7,"Acrescimo no Item"));
        list.add(new DescAcesso(8,"Desconto Rodapé"));
        list.add(new DescAcesso(9,"Cancelamento Nota"));
        list.add(new DescAcesso(10,"Cancelamento Item da Nota"));
        list.add(new DescAcesso(11,"Re-Impressão de Nota"));
        list.add(new DescAcesso(12,"Fechamento Caixa"));
        list.add(new DescAcesso(13,"Impressão do Borderô"));
        list.add(new DescAcesso(14,"Pré-Impressão do Fechamento"));
        list.add(new DescAcesso(15,"Conta Cliente"));
        list.add(new DescAcesso(16,"Transferência de Mesa/Pedidos"));
        list.add(new DescAcesso(17,"Emitir Conta Sem Taxa de Serviço"));
        list.add(new DescAcesso(18,"Cancelamento de Item do Pedido"));
        list.add(new DescAcesso(19,"Reemissão de Conta"));
        list.add(new DescAcesso(20,"Cancelar Pagamento do Pedido"));
        list.add(new DescAcesso(21,"Emitir Conta Com Desconto"));
        list.add(new DescAcesso(22,"Relação de Pedidos Fechados"));
        list.add(new DescAcesso(23,"Emitir Conta"));
        list.add(new DescAcesso(24,"Emitir Conta sem Taxa de Servico e Com Desconto"));

    }

    private class DescAcesso{
        private int indice ;
        private String descricao ;

        public DescAcesso(int indice, String descricao) {
            this.indice = indice;
            this.descricao = descricao;
        }

        public int getIndice() {
            return indice;
        }

        public void setIndice(int indice) {
            this.indice = indice;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }
    }


}
