package com.mack.prototipo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Date;

public class Principal extends Activity {

	public static String nomeBanco = "DB_ANDROID_PONTO";
    private SQLiteDatabase banco = null;
    private Button btnSalvar;
    private String data;
    private String hora_inicio;
    private String hora_final;
    ListView lista ;
    TextView Entradatxt;
    TextView Saidatxt;
    Button fechar;
    DigitalClock horarioCelular;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnSalvar = (Button)  findViewById(R.id.btnSalvar);
        fechar = (Button) findViewById(R.id.fechar);
        Entradatxt = (TextView)  findViewById(R.id.entrada);
        Saidatxt = (TextView)  findViewById(R.id.saida);
        Date dt = new Date();
        //Preenche dados basicos
        data = dt.getDay() + "/" + dt.getMonth() + "/" + dt.getYear();
        
        criarBanco();
        
        Entradatxt.setText(pesquisaHora(data, "inicio"));
        Saidatxt.setText(pesquisaHora(data, "final"));
        //Toast.makeText(this, data, Toast.LENGTH_LONG).show();

        if (Entradatxt.getText().equals("") || Saidatxt.getText().equals("")){
        	horarioCelular = (DigitalClock) findViewById(R.id.horarioCelular);
        	horarioCelular.setVisibility(View.VISIBLE);
        }
        
        btnSalvar.setOnClickListener(new Button.OnClickListener() {
        	@Override
            public void onClick(View arg0) {
        		Date hr = new Date();
                hora_inicio = (hr.getHours() + ":" + hr.getMinutes()).toString();
                hora_final = (hr.getHours() + ":" + hr.getMinutes()).toString();
                final Horario horario = new Horario(data, hora_inicio, hora_final);
        		insert(horario);
            }
        });
        
        fechar.setOnClickListener(new Button.OnClickListener() {
        	@Override
            public void onClick(View arg0) {
        		fechar.setVisibility(View.INVISIBLE);
        		lista.setVisibility(View.INVISIBLE);
            }
        });
        
    }
    
    public boolean criarBanco() {
        boolean aux = true;
        try {
            // cria o banco de dados caso ele não exista e abre a conexao
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            //SQL que cria o banco de dados 
            //banco.execSQL("DROP TABLE IF EXISTS horario");
            banco.execSQL("CREATE TABLE IF NOT EXISTS horario (data TEXT PRIMARY KEY, hora_inicio TEXT, hora_final TEXT);"); 
            //Toast.makeText(this, "Sucesso na criacao do BD", Toast.LENGTH_LONG).show();
            banco.close();
        } catch (Exception e) {
            aux = false;
            Toast.makeText(this, "Falha criacao BD: "+e.getMessage(), 
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
    		Entradatxt.setText(horario.getHoraInicial());
            Toast.makeText(this, "Horario inicial atualizado",Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {
        	if (e.getMessage().contains("column data is not unique")){
        		aux=update(horario);
     	    } else {
     	        aux=false;
     	    	Toast.makeText(this, "Exception "+ e.getMessage(), 
            		Toast.LENGTH_LONG).show();
     	    }
        } finally {
        	banco.close();
        }
        return aux;
    }
    
    public boolean update(Horario horario){
        boolean aux = true;
        try{
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            banco.execSQL("UPDATE horario SET hora_final = '" + horario.getHoraFinal()
            		+ "' WHERE data = '" + horario.getData() + "'");
            Saidatxt.setText(horario.getHoraFinal());
            horarioCelular.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Horario final atualizado",Toast.LENGTH_LONG).show();
        }catch(SQLiteException e){
            aux=false;
            Toast.makeText(this, "Erro: " + e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        } finally {
        	banco.close();
        }
        return aux;
    }
    
    private String pesquisaHora(String data, String tipo){
    	SQLiteDatabase bd = null;
    	Cursor c = null;
    	String hora = "00:00";
        try {
            bd = openOrCreateDatabase(Principal.nomeBanco, 0, null);
            String where = "data = \"" + data + "\"";
            if (tipo.equals("inicio")){
              c = bd.query(true, "horario", new String[]{"data","hora_inicio"}, where, null, null, null, null, null);
            } else {
            	c = bd.query(true, "horario", new String[]{"data","hora_final"}, where, null, null, null, null, null);
            }
            if (c.moveToNext()){
            	if (tipo.equals("inicio")){
            		hora = c.getString(c.getColumnIndex("hora_inicio"));
            	} else{
            		hora = c.getString(c.getColumnIndex("hora_final"));
            	}
            }
        } catch (SQLiteException e) {        
     	   Toast.makeText(this, "Falha na pesquisa no BD: "+e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        } finally {
            c.close();
            bd.close();
        }
        return hora; 
    }
    
    private void pesquisar(){
        ArrayAdapter<String> fileList = null;
        SQLiteDatabase bd = null;
        Cursor c = null;
        
        lista = (ListView) findViewById(R.id.list);
        lista.setVisibility(View.VISIBLE);
        fechar.setVisibility(View.VISIBLE);
        try {
           bd = openOrCreateDatabase(Principal.nomeBanco, 0, null);
           c = bd.query(true, "horario", new String[]{"data","hora_inicio","hora_final"}, null, null, null, null, null, null);   
           ArrayList<String> result = new ArrayList<String>();        
           while(c.moveToNext()){
             result.add(c.getString(c.getColumnIndex("data")) + ": \t\t\t" + 
                    c.getString(c.getColumnIndex("hora_inicio")) + " <-> " +
                    c.getString(c.getColumnIndex("hora_final")));
           }
           fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result); //cria o modelo
           lista.setAdapter(fileList); //Instala a lista
       } catch (SQLiteException e) {        
    	   Toast.makeText(this, "Falha na pesquisa no BD: "+e.getMessage(), 
           		Toast.LENGTH_LONG).show();
       } finally {
           c.close();
           bd.close();
       }    
    }

    private void CreateMenu(Menu menu)
    {
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0, 0, 0, "Editar horario do dia");
        {
            mnu1.setAlphabeticShortcut('a');
            //mnu1.setIcon(R.drawable.alert_dialog_icon);            
        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Relatorio Mensal");
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
        	pesquisar();
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