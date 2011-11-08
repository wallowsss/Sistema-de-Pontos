package com.mack.prototipo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Principal extends Activity {

    private SQLiteDatabase banco = null;
    private String nomeBanco = "DB_ANDROID_PONTO";
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //startActivity(new Intent( this, listaRelat.class));
        criarBanco();
    }
    
    public boolean criarBanco() {
        boolean aux = true;
        try {
            // cria o banco de dados caso ele não exista e abre a conexao
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            //SQL que cria o banco de dados
            banco.execSQL("CREATE TABLE IF NOT EXISTS horario (data INTEGER PRIMARY KEY, horario_inicio TEXT, horario_final TEXT);"); 
            Toast.makeText(this, "Sucesso na criação do BD", 
                    Toast.LENGTH_LONG).show();
            banco.close();
        } catch (Exception e) {
            aux = false;
            Toast.makeText(this, "Falha criação BD: "+e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }
        return aux;
    }
    
    public boolean insert(String data, String data_inicio, String data_final) {
    	boolean aux = true;
    	try {
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            //Executa o insert no banco de dados
            banco.execSQL("INSERT INTO horario (data, horario_inicio, horario_final) VALUES ('"+ 
            		data + "','" + data_inicio + "','" + data_final + "')");
            /*String[] colunas = { "nome", "telefone", "data" };
            Cursor c = banco.query(true, "lista", colunas, null, null, null,null, null,null);
            while (c.next()) {
            	Toast.makeText(this, String.valueOf(c.getColumnIndex("nome"))+"   "+ c.getString(c.getColumnIndex("nome")), 
                		Toast.LENGTH_LONG).show();
            }*/
            banco.close();
        } catch (Exception e) {
        	aux=false;
            Toast.makeText(this, "Exception "+ e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }
        return aux;
    }
    
    private boolean update(String data, String data_inicio, String data_final){
        boolean aux = true;
        try{
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            banco.execSQL("UPDATE horario SET data_inicio = '"+data_inicio+
            		"', data_final = '"+data_final+"' WHERE data = '"+data+"'");
            banco.close();
        }catch(Exception e){
            aux=false;
            Toast.makeText(this, "Excecao "+e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }
        return aux;
    }    

    private void CreateMenu(Menu menu)
    {
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0, 0, 0, "Editar hor‡rio do dia");
        {
            mnu1.setAlphabeticShortcut('a');
            //mnu1.setIcon(R.drawable.alert_dialog_icon);            
        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Relat—rio Mensal");
        {
            mnu2.setAlphabeticShortcut('b');
            //mnu2.setIcon(R.drawable.ic_popup_reminder);            
        }
    }
 
    private boolean MenuChoice(MenuItem item)
    {        
        switch (item.getItemId()) {
        case 0:
        	startActivity(new Intent( this, editaHora.class));
        	/*Toast.makeText(this, "You clicked on Item 1", 
                Toast.LENGTH_LONG).show();*/
            return true;
        case 1:
        	//startActivity(new Intent( this, listaRelat.class));
        	Toast.makeText(this, "Relat—rio ainda indispon’vel por falta de dados!", 
                Toast.LENGTH_LONG).show();
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