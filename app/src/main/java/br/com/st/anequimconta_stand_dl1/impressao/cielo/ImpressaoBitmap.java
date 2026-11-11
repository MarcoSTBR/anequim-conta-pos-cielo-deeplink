package br.com.st.anequimconta_stand_dl1.impressao.cielo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.ByteArrayOutputStream;

import br.com.st.anequimconta_stand_dl1.activity.ActivityEnvioPedidos;
import br.com.st.anequimconta_stand_dl1.activity.ActivityImpressao;
import br.com.st.anequimconta_stand_dl1.activity.ActivityImprimirContas;
import br.com.st.anequimconta_stand_dl1.impressao.listener.ListenerImpressaoBipMap;
import br.com.st.anequimconta_stand_dl1.impressao.listener.OnImpressaoBitMap;
import br.com.st.anequimconta_stand_dl1.ui.FragmentCaixa;
import br.com.st.anequimconta_stand_dl1.ui.FragmentContasPagas;
import br.com.st.anequimconta_stand_dl1.ui.FragmentPagamento;

public class ImpressaoBitmap implements OnImpressaoBitMap {

    private final String TAG = "bitmap" ;
    private ListenerImpressaoBipMap listenerImpressaoBipMap;
    private Intent intent ;
    private ActivityResultLauncher<Intent> respostaImpressao ;

    public ImpressaoBitmap(ActivityEnvioPedidos ctx, ListenerImpressaoBipMap listenerImpressaoBipMap) {
        this.listenerImpressaoBipMap = listenerImpressaoBipMap;
        intent = new Intent(ctx, ActivityImpressao.class) ;
        respostaImpressao = ctx.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result == null){
                        Log.i(TAG, "result nulo ") ;
                    } else {
                        if (result.getData() == null)
                            Log.i(TAG, "result.getData() nulo ") ;
                    }
                    Log.i(TAG, "ok "+result.getResultCode()) ;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.i(TAG, "ok "+result.getData().toString()) ;
                        Log.i(TAG, "ok s "+result.getData().getStringExtra("status")) ;
                        Log.i(TAG, "ok m "+result.getData().getStringExtra("mensagem")) ;
                        if (result.getData().getStringExtra("status").equals("success"))
                            setOK() ;
                        else setErro(result.getData().getStringExtra("mensagem")) ;
                    }
                }
        );
    }

    public ImpressaoBitmap(FragmentCaixa f, ListenerImpressaoBipMap listenerImpressaoBipMap) {
        this.listenerImpressaoBipMap = listenerImpressaoBipMap;
        intent = new Intent(f.getActivity(), ActivityImpressao.class) ;
        respostaImpressao = f.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result == null){
                        Log.i(TAG, "result nulo ") ;
                    } else {
                        if (result.getData() == null)
                            Log.i(TAG, "result.getData() nulo ") ;
                    }
                    Log.i(TAG, "ok "+result.getResultCode()) ;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.i(TAG, "ok "+result.getData().toString()) ;
                        Log.i(TAG, "ok s "+result.getData().getStringExtra("status")) ;
                        Log.i(TAG, "ok m "+result.getData().getStringExtra("mensagem")) ;
                        if (result.getData().getStringExtra("status").equals("success"))
                            setOK() ;
                        else setErro(result.getData().getStringExtra("mensagem")) ;
                    }
                }
        );
    }


    public ImpressaoBitmap(FragmentPagamento ctx, ListenerImpressaoBipMap listenerImpressaoBipMap) {
        this.listenerImpressaoBipMap = listenerImpressaoBipMap;
        intent = new Intent(ctx.getActivity(), ActivityImpressao.class) ;
        respostaImpressao = ctx.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result == null){
                        Log.i(TAG, "result nulo ") ;
                    } else {
                        if (result.getData() == null)
                            Log.i(TAG, "result.getData() nulo ") ;
                    }
                    Log.i(TAG, "ok "+result.getResultCode()) ;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.i(TAG, "ok "+result.getData().toString()) ;
                        Log.i(TAG, "ok s "+result.getData().getStringExtra("status")) ;
                        Log.i(TAG, "ok m "+result.getData().getStringExtra("mensagem")) ;
                        if (result.getData().getStringExtra("status").equals("success"))
                            setOK() ;
                        else setErro(result.getData().getStringExtra("mensagem")) ;
                    }
                }
        );
    }

    public ImpressaoBitmap(FragmentContasPagas ctx, ListenerImpressaoBipMap listenerImpressaoBipMap) {
        this.listenerImpressaoBipMap = listenerImpressaoBipMap;
        intent = new Intent(ctx.getActivity(), ActivityImpressao.class) ;
        respostaImpressao = ctx.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result == null){
                        Log.i(TAG, "result nulo ") ;
                    } else {
                        if (result.getData() == null)
                            Log.i(TAG, "result.getData() nulo ") ;
                    }
                    Log.i(TAG, "ok "+result.getResultCode()) ;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.i(TAG, "ok "+result.getData().toString()) ;
                        Log.i(TAG, "ok s "+result.getData().getStringExtra("status")) ;
                        Log.i(TAG, "ok m "+result.getData().getStringExtra("mensagem")) ;
                        if (result.getData().getStringExtra("status").equals("success"))
                            setOK() ;
                        else setErro(result.getData().getStringExtra("mensagem")) ;
                    }
                }
        );
    }

    private void setOK() {
        listenerImpressaoBipMap.imprimirBitMapOk();
    }

    public ImpressaoBitmap(ActivityImprimirContas ctx, ListenerImpressaoBipMap listenerImpressaoBipMap) {
        this.listenerImpressaoBipMap = listenerImpressaoBipMap;
        intent = new Intent(ctx, ActivityImpressao.class) ;
        respostaImpressao = ctx.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result == null){
                        Log.i(TAG, "result nulo ") ;
                    } else {
                        if (result.getData() == null)
                            Log.i(TAG, "result.getData() nulo ") ;
                    }
                    Log.i(TAG, "ok "+result.getResultCode()) ;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.i(TAG, "ok "+result.getData().toString()) ;
                        Log.i(TAG, "ok s "+result.getData().getStringExtra("status")) ;
                        Log.i(TAG, "ok m "+result.getData().getStringExtra("mensagem")) ;
                        if (result.getData().getStringExtra("status").equals("success"))
                            setOK() ;
                        else setErro(result.getData().getStringExtra("mensagem")) ;
                    }
                }
        );
    }



    private void setErro(String mensagem) {
        listenerImpressaoBipMap.imprimirBitMapErro(mensagem);
    }


    @Override
    public void imprimirBitMap(Bitmap bitmap) {
        intent.putExtra("DATA", getJson(bitmap)) ;
        respostaImpressao.launch(intent);
    }

    private String getJson(Bitmap bitMapTicket){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitMapTicket.compress(Bitmap.CompressFormat.PNG,100,stream);
        // Initialize byte array
        byte[] bytes=stream.toByteArray();
        // get base64 encoded  string
        String sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
        // String btm = bitmap.get
/*
        JSONObject j = new JSONObject();
        j.put("type", "image") ;
        j.put("imagePath", sImage) ;
        JSONArray ja = new JSONArray() ;
        ja.put(j) ;
*/
        return sImage;
    }
    private String getImage(Bitmap bitmap){
        return "iVBORw0KGgoAAAANSUhEUgAAAHcAAAAuCAAAAAA309lpAAACMklEQVRYw91YQXLDIAyUMj027Us606f6RL7lJP0Ise/bg7ERSLLdZkxnyiVGIK0AoRVh0J+0l2ZITCAmSus8tYNNv9wUl8Xn2A6XZec8tsK9lN0zEaFBCxMc0M3IoHawBAAxffLx9/frY1kkEV0/iYjC8bjjmSRuCrHjcXMoS9zD4/nqePNf10v2whrkDRjLR4t8BWPXbdyRmccDgBMZUXDiiv2DeSK4sKwWrfgIda8V/6L6blZvLMARTescAohCD7xlcsItjYXEXHn2LIESzO3mDARPYTJXwiQ/VgWFobsYGKRdRy5x6/1QuAPpKdq89MiTS1x9EBXuYJyVZd46p6ndXVwAqfwJpd4C20uLk/LsUIilQ5Q11A4tuIU8Ti4bi8oz6lNX8iD8rNUdXDm3iMs81le4pUOLOJrGatzBx1VqVRSU8qAdNRc855GwHxcFblQbYTvqx3M0ZxZnZeBq+UoayI0h3y7QPMhOyQA9JMkO9aMIqs6Rmrw73T6ey9anvDX5kbinvT2PW7yYzj8ogrcYqBOJjNxc21d5EjmH0e/iaqUV9dXj3YgYtkvCjbjaqs5O+85MxVvwTcZdhR5YuFbckCSfNkHUolTcE9Cq9iQfXtV62bo9nUBIm8AXedPidimVFIjZCdYlTw4W8RtsatKC7Bt7D4t5tMle9qPD+y4uyL81FS/UnnVu3eMzhuj3G7CqzkHF77ISsaoraSsqVnRhq3rSZ+F5Ur//b5zOOVoAwDc6szxdC+PYAAAAAABJRU5ErkJggg==" ;

    }

    private String getTeste(){
       return "[\n" +
               "  {\n" +
               "    \"type\":\"image\",\n" +
               "    \"imagePath\":\"iVBORw0KGgoAAAANSUhEUgAAAHcAAAAuCAAAAAA309lpAAACMklEQVRYw91YQXLDIAyUMj027Us606f6RL7lJP0Ise/bg7ERSLLdZkxnyiVGIK0AoRVh0J+0l2ZITCAmSus8tYNNv9wUl8Xn2A6XZec8tsK9lN0zEaFBCxMc0M3IoHawBAAxffLx9/frY1kkEV0/iYjC8bjjmSRuCrHjcXMoS9zD4/nqePNf10v2whrkDRjLR4t8BWPXbdyRmccDgBMZUXDiiv2DeSK4sKwWrfgIda8V/6L6blZvLMARTescAohCD7xlcsItjYXEXHn2LIESzO3mDARPYTJXwiQ/VgWFobsYGKRdRy5x6/1QuAPpKdq89MiTS1x9EBXuYJyVZd46p6ndXVwAqfwJpd4C20uLk/LsUIilQ5Q11A4tuIU8Ti4bi8oz6lNX8iD8rNUdXDm3iMs81le4pUOLOJrGatzBx1VqVRSU8qAdNRc855GwHxcFblQbYTvqx3M0ZxZnZeBq+UoayI0h3y7QPMhOyQA9JMkO9aMIqs6Rmrw73T6ey9anvDX5kbinvT2PW7yYzj8ogrcYqBOJjNxc21d5EjmH0e/iaqUV9dXj3YgYtkvCjbjaqs5O+85MxVvwTcZdhR5YuFbckCSfNkHUolTcE9Cq9iQfXtV62bo9nUBIm8AXedPidimVFIjZCdYlTw4W8RtsatKC7Bt7D4t5tMle9qPD+y4uyL81FS/UnnVu3eMzhuj3G7CqzkHF77ISsaoraSsqVnRhq3rSZ+F5Ur//b5zOOVoAwDc6szxdC+PYAAAAAABJRU5ErkJggg==\"\n" +
               "  }\n" +
               "]" ;
    }
}
