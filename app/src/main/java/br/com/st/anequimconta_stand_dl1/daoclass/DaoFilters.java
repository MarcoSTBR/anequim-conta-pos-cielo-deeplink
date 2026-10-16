package br.com.st.anequimconta_stand_dl1.daoclass;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DaoFilters {

    private List<DaoFilter> list;

    public DaoFilters() {
        clear();
    }

    public void clear() {
        list = new ArrayList<DaoFilter>();

    }

    public void add(DaoFilter f) {
        list.add(f);
    }

    public List<DaoFilter> getList() {
        return list;
    }

    public JSONArray getJSON() {
        JSONArray j = new JSONArray();
        for (DaoFilter d : list) {
            j.put(d.getJSON());
        }
        return j;
    }

    public String getJSONCx() {
        String s = "[";
        for (DaoFilter d : list) {
            if (s.length() == 1)
                s += d.getJSONCx();
            else
                s += ","+d.getJSONCx();
        }
        s += "]";
        return s;
    }
}
