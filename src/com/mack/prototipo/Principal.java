package com.mack.prototipo;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


public class Principal extends Activity {

    private SQLiteDatabase banco = null;
    private String nomeBanco = "DB_ANDROID_PONTO";
    private Button btnSalvar;
    private String data;
    private String hora_inicial;
    private String hora_final;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        btnSalvar = (Button)  findViewById(R.id.btnSalvar);
        data = new Date().toString();
        
        criarBanco();
        
        btnSalvar.setOnClickListener(new Button.OnClickListener() {
        	@Override
            public void onClick(View arg0) {
        		hora_inicial = (Calendar.HOUR + ":" + Calendar.MINUTE).toString();
                hora_final = (Calendar.HOUR + ":" + Calendar.MINUTE).toString();
                final Horario horario = new Horario(data, hora_inicial, hora_final);
        		boolean vb = insert(horario);
        		if (vb)
        			vb = update(horario);
            }
        });
    }
    
    public boolean criarBanco() {
        boolean aux = true;
        try {
            // cria o banco de dados caso ele não exista e abre a conexao
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            //SQL que cria o banco de dados 
            banco.execSQL("CREATE TABLE IF NOT EXISTS horario (data TEXT PRIMARY KEY, hora_inicio TEXT, hora_final TEXT);"); 
            //Toast.makeText(this, "Sucesso na criação do BD", Toast.LENGTH_LONG).show();
            banco.close();
        } catch (Exception e) {
            aux = false;
            Toast.makeText(this, "Falha criação BD: "+e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }
        return aux;
    }
    
    public boolean insert(Horario horario) {
    	boolean aux = true;
    	try {
    		banco = openOrCreateDatabase(nomeBanco, 0, null);
    		banco.execSQL("INSERT INTO horario (data, hora_inicio, hora_final) VALUES ('"+ 
            		horario.getData() + "','" + horario.getHoraInicial() + "','" + horario.getHoraFinal() + "')");
            Toast.makeText(this, "Inseri o registro no BD",Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {
        	aux=false;
            Toast.makeText(this, "Exception "+ e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        } finally {
        	banco.close();
            Toast.makeText(this, "fechou",Toast.LENGTH_LONG).show();
        }
        return aux;
    }
    
    public boolean update(Horario horario){
        boolean aux = true;
        try{
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            banco.execSQL("UPDATE horario SET data_inicio = '"+horario.getHoraInicial()+"', data_final = '"
            		+horario.getHoraFinal()+"' WHERE data = '"+horario.getData()+"'");
            Toast.makeText(this, "Atualizei o dado",Toast.LENGTH_LONG).show();
        }catch(SQLiteException e){
            aux=false;
            Toast.makeText(this, "Excecao "+e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }finally{
        	banco.close();
        }
        return aux;
    }

    private void CreateMenu(Menu menu)
    {
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0, 0, 0, "Editar horário do dia");
        {
            mnu1.setAlphabeticShortcut('a');
            //mnu1.setIcon(R.drawable.alert_dialog_icon);            
        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Relatório Mensal");
        {
            mnu2.setAlphabeticShortcut('b');
            //mnu2.setIcon(R.drawable.ic_popup_reminder);            
        }
    }
 
    private boolean MenuChoice(MenuItem item)
    {        
        switch (item.getItemId()) {
        case 0:
        	startActivity(new Intent(this, editaHora.class));
        	/*Toast.makeText(this, "Edicao de horas indisponivel!", 
                Toast.LENGTH_LONG).show();*/
            return true;
        case 1:
        	startActivity(new Intent(this, listaRelat.class));
        	/*Toast.makeText(this, "Relatorio ainda indisponivel por falta de dados!", 
                Toast.LENGTH_LONG).show();*/
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {    
         return MenuChoice(item);    
    }

}