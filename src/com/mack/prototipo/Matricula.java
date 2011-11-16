package com.mack.prototipo;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Matricula extends Activity {
	
	Button btnGravarID;
	EditText matricula;
	
	@Override  
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matricula);
        
        btnGravarID = (Button) findViewById(R.id.btnGravarID);
        matricula = (EditText) findViewById(R.id.edita_matricula);
        matricula.hasFocus();
        
		
        btnGravarID.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				insertMatricula(matricula.getText());
				finish();
			}
		});
        
    }
	
	public void insertMatricula(Editable ID){
		SQLiteDatabase banco = null;
    	try {
    		banco = openOrCreateDatabase(Principal.nomeBanco, 0, null);
    		banco.execSQL("INSERT INTO matricula (id) VALUES ('" + ID + "')");
    		banco.close();
    		Toast.makeText(this, "Matricula cadastrada",Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {
        	Toast.makeText(this, "Exception "+ e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }
	}

}
