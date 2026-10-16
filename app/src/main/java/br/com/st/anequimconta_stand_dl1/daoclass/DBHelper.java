package br.com.st.anequimconta_stand_dl1.daoclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    protected Context ctx ;
    private static DBHelper dbhelper = null ;

    public DBHelper(Context ctx) {
        super(ctx, "BbAnequimConta", null, 1);
        this.ctx = ctx ;
    }

/*
    public void testeDb(){
        Log.i("tabela_db", "criou as tabelas") ;

        SQLiteDatabase db = getDB(ctx).getWritableDatabase() ;

        db.execSQL("DROP TABLE IF EXISTS PEDIDO") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM_ADD") ;
        criarTabelas(db) ;
       // db.execSQL("DROP TABLE IF EXISTS PEDIDO_TICKET") ;
    }

*/

    public static DBHelper getDB(Context ctx){
        if (dbhelper == null) {
            dbhelper = new DBHelper(ctx) ;
           // dbhelper.testeDb() ;
        }
        return  dbhelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        criarTabelas(db) ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldv, int newv) {
        Log.i("onUpgrade", "i= "+oldv+" i1= "+newv) ;
        if ((oldv == 1) && (newv == 2)) {
        }

    }

    public void excluirTabelas(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS PEDIDO") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS GRADE_VENDAS") ;
        db.execSQL("DROP TABLE IF EXISTS GRADE_VENDAS_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS PRODUTO ") ;
        db.execSQL("DROP TABLE IF EXISTS MODALIDADE") ;
        db.execSQL("DROP TABLE IF EXISTS ACOMP_PROD") ;
        db.execSQL("DROP TABLE IF EXISTS ACOMP") ;
        db.execSQL("DROP TABLE IF EXISTS ACOMP_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS RECEBIMENTO_POS") ;
        db.execSQL("DROP TABLE IF EXISTS TICKET_ITEM") ;
        db.execSQL("DROP TABLE IF EXISTS VENDA_TICKET") ;
        db.execSQL("DROP TABLE IF EXISTS RECEBIMENTO_POS_PIX") ;
        db.execSQL("DROP TABLE IF EXISTS PEDIDO_TICKET") ;
    }


    public static void criarTabelas(SQLiteDatabase db){

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO ( "
                + "UUID TEXT PRIMARY KEY, "
                + "NUMERO TEXT, "
                + "REFERENCIA TEXT,"
                + "CORRENTE INTEGER,"
                + "STATUS INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM ( "
                + "UUID TEXT PRIMARY KEY, "
                + "INDICE INTEGER, "
                + "PEDIDO_UUID TEXT, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER, "
                + "OBS TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_ITEM_ADD ( "
                + "UUID TEXT PRIMARY KEY, "
                + "INDICE INTEGER, "
                + "PEDIDO_ITEM_UUID TEXT, "
                + "PRODUTO_ID INTEGER, "
                + "QUANTIDADE DOUBLE, "
                + "PRECO DOUBLE, "
                + "DESCONTO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "VALOR DOUBLE, "
                + "STATUS INTEGER, "
                + "OBS TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS GRADE_VENDAS ( "
                + "id INTEGER PRIMARY KEY, "
                + "DESCRICAO TEXT, "
                + "IMAGEM TEXT, "
                + "STATUS INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS GRADE_VENDAS_ITEM ( "
                + "id INTEGER PRIMARY KEY, "
                + "GRADE_VENDAS_ID INTEGER, "
                + "PRODUTO_ID INTEGER, "
                + "PRECO DOUBLE, "
                + "COMISSAO DOUBLE, "
                + "STATUS INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUTO ( "
                + "id INTEGER PRIMARY KEY, "
                + "CODIGO TEXT , "
                + "UNIDADE TEXT , "
                + "DESCRICAO TEXT , "
                + "STATUS INTEGER,"
                + "IMAGEM TEXT,"
                + "COMISSAO DOUBLE,"
                + "PRECO DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PEDIDO_TICKET ( "
                + "UUID TEXT PRIMARY KEY, "
                + "CONTA_ITEM_UUID TEXT, "
                + "TERMINAL_ID INTEGER, "
                + "USUARIO_ID INTEGER, "
                + "DATA DATETIME, "
                + "USUARIO_F_ID INTEGER, "
                + "DATA_FECHAMENTO DATETIME, "
                + "STATUS INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS TICKET_ITEM ( "
                + "ID INTEGER PRIMARY KEY, "
                + "PRODUTO_ID INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS MODALIDADE ( "
                + "id INTEGER PRIMARY KEY, "
                + "CODIGO TEXT, "
                + "DESCRICAO TEXT, "
                + "TIPOMODALIDADE TEXT, "
                + "COD_RECEBIMENTO TEXT, "
                + "FOTO TEXT, "
                + "STATUS INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS ACOMP_PROD ( "
                + "id INTEGER PRIMARY KEY, "
                + "AFOOD_PRODUTO_ID INTEGER, "
                + "AFOOD_ACOMP_ID INTEGER)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS ACOMP ( "
                + "id INTEGER PRIMARY KEY, "
                + "DESCRICAO TEXT, "
                + "OBS TEXT, "
                + "MIN INTEGER, "
                + "MAX INTEGER, "
                + "PRECO DOUBLE)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS ACOMP_ITEM ( "
                + "id INTEGER PRIMARY KEY, "
                + "AFOOD_ACOMP_ID INTEGER, "
                + "AFOOD_PRODUTO_ID INTEGER, "
                + "OBS TEXT, "
                + "MIN INTEGER, "
                + "MAX INTEGER, "
                + "PRECO DOUBLE)") ;

        db.execSQL("CREATE TABLE IF NOT EXISTS RECEBIMENTO_POS ( "
                + "UUID TEXT PRIMARY KEY, "
                + "PAGAMENTO_ID INTEGER, "
                + "PAGAMENTO_UUID TEXT, "
                + "DATA DATETIME, "
                + "VENDA_VISTA_PAG_UUID TEXT, "
                + "STATUS_REC TEXT, "
                + "STATUS INTEGER)");


        db.execSQL("CREATE TABLE IF NOT EXISTS RECEBIMENTO_POS_PIX ( "
                + "UUID TEXT PRIMARY KEY, "
                + "DATA DATETIME, "
                + "PAGAMENTO_UUID TEXT, "
                + "VENDA_VISTA_PAG_UUID TEXT, "
                + "STATUS INTEGER)");
    }

    public void limparTudo(Context ctx) {
        SQLiteDatabase db = getDB(ctx).getWritableDatabase() ;
        excluirTabelas(db);
        //excluirTodasTabelas(db) ;
        criarTabelas(db);

    }

    public void excluirTodasTabelas(SQLiteDatabase db){
//        SQLiteDatabase db = getDB(ctx).getWritableDatabase() ;
        Cursor res =  db.rawQuery( "SELECT name FROM sqlite_master", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            String tabela = res.getString(res.getColumnIndexOrThrow("name")) ;
            Log.i("tabelas", tabela) ;
            if (!((tabela.equals("android_metadata")) || (tabela.equals("sqlite_sequence"))))
              db.execSQL("DROP TABLE IF EXISTS "+tabela) ;
            res.moveToNext();
        }
    }

    public void listarTabelas(Context ctx){
        SQLiteDatabase db = getDB(ctx).getWritableDatabase() ;
        Cursor res =  db.rawQuery( "SELECT name FROM sqlite_master", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Log.i("tabelas", res.getString(res.getColumnIndexOrThrow("name"))) ;
            res.moveToNext();
        }
    }


}
