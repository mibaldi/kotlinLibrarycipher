package com.mibaldi.pruebalibreriaencriptacion;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MiObjeto {
    public String name;
    public String apellido;
    public String edad;
    public List<String> numeros = new ArrayList<>();
    public String ciudad = null;
    public Date cumpleaños = new Date();

}
