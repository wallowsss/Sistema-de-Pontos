package com.mack.prototipo;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class Principal extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startActivity(new Intent( this, listaRelat.class));
        
    }
}