package br.com.st.anequimconta_stand_dl1.emissao_nfce.listener;

import org.json.JSONObject;

public interface ListenerCloudNFCeJSON {

    void OnJSON(JSONObject j) ;
    void OnErroJSON(String msg) ;
}
