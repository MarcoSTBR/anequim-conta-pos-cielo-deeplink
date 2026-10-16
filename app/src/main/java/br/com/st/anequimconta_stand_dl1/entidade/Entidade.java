package br.com.st.anequimconta_stand_dl1.entidade;

import org.json.JSONException;
import org.json.JSONObject;

public interface Entidade {
    JSONObject geJSON() throws JSONException;
}
