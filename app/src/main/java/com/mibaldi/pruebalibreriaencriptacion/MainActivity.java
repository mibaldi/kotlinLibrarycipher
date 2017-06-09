package com.mibaldi.pruebalibreriaencriptacion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.mibaldi.ciferlibrary.CiferManagerKt;

import java.lang.reflect.Field;

import static com.mibaldi.ciferlibrary.CiferManagerKt.ciferObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MiObjeto miObjeto = new MiObjeto();
        miObjeto.name = "mikel";
        miObjeto.apellido = "balduciel";
        miObjeto.edad = "14";
        miObjeto.numeros.add("numero1");
        miObjeto.numeros.add("numero2");
        miObjeto.numeros.add("numero3");

        MiObjeto miObjetoCifrado = CiferManagerKt.ciferObject("Santander11Token", miObjeto, MiObjeto.class);
        MiObjeto miObjetoDescifrado = CiferManagerKt.deciferObject("Santander11Token", miObjetoCifrado, MiObjeto.class);
        Log.d("objetoOriginal",miObjeto.toString());
        Log.d("objetoCIFRADO",miObjetoCifrado.toString());
        Log.d("objetoCLARO",miObjetoDescifrado.toString());

    }
}
