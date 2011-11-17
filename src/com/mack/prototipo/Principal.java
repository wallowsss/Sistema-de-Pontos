package com.mack.prototipo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.*;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Principal extends Activity {

	public static String nomeBanco = "DB_ANDROID_PONTO";
    private Button btnSalvar;
    private String data;
    private String hora_inicio;
    private String hora_final;
    ListView lista ;
    TextView Entradatxt;
    TextView Saidatxt;
    Button fechar;
    DigitalClock horarioCelular;
    String matricula;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        btnSalvar = (Button)  findViewById(R.id.btnSalvar);
        fechar = (Button) findViewById(R.id.fechar);
        horarioCelular = (DigitalClock) findViewById(R.id.horarioCelular);
    	Entradatxt = (TextView)  findViewById(R.id.entrada);
        Saidatxt = (TextView)  findViewById(R.id.saida);
        
        Date dt = new Date( System.currentTimeMillis());
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
        data =formatador.format(dt);
        
        criarBanco();
        
        matricula = pesquisaMatricula();
        if (matricula.equals("")){
        	startActivityForResult(new Intent(this, Matricula.class), 1);
        }
        
        Entradatxt.setText(pesquisaHora(data, "inicio"));
        Saidatxt.setText(pesquisaHora(data, "final"));
        //Toast.makeText(this, data+"", Toast.LENGTH_LONG).show();

        if (Entradatxt.getText().equals("00:00") || Saidatxt.getText().equals("00:00")){
        	horarioCelular.setVisibility(View.VISIBLE);
        }
        if (!Entradatxt.getText().equals("00:00") && !Saidatxt.getText().equals("00:00")){
        	btnSalvar.setEnabled(false);
        }
        
        btnSalvar.setOnClickListener(new Button.OnClickListener() {
        	@Override
            public void onClick(View arg0) {
        		Date hr = new Date();
                hora_inicio = editaHora.formataData(hr.getHours(), hr.getMinutes());
                hora_final = editaHora.formataData(hr.getHours(), hr.getMinutes());
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
    
    @Override
    public void onResume(){
    	super.onResume();
    	Entradatxt.setText(pesquisaHora(data, "inicio"));
        Saidatxt.setText(pesquisaHora(data, "final"));
    }
    
    public boolean criarBanco() {
    	SQLiteDatabase banco = null;
        boolean aux = true;
        try {
            // cria o banco de dados caso ele nao exista e abre a conexao
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            //banco.execSQL("drop table horario;  ");
            //banco.execSQL("drop table matricula;");
            banco.execSQL("CREATE TABLE IF NOT EXISTS matricula (id TEXT PRIMARY KEY);");
            banco.execSQL("CREATE TABLE IF NOT EXISTS horario (data TEXT PRIMARY KEY, hora_inicio TEXT, hora_final TEXT, status TEXT);"); 
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
    	SQLiteDatabase banco = null;
        boolean aux = true;
    	try {
    		banco = openOrCreateDatabase(nomeBanco, 0, null);
    		banco.execSQL("INSERT INTO horario (data, hora_inicio, hora_final, status) VALUES ('"+ 
            		horario.getData() + "','" + horario.getHoraInicial() + "','" + horario.getHoraFinal() + "', 'P')");
    		banco.close();
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
        }
        return aux;
    }
    
    public boolean update(Horario horario){
    	SQLiteDatabase banco = null;
    	boolean aux = true;
        try{
            banco = openOrCreateDatabase(nomeBanco, 0, null);
            banco.execSQL("UPDATE horario SET hora_final = '" + horario.getHoraFinal()
            		+ "' WHERE data = '" + horario.getData() + "'");
        	banco.close();
        	Saidatxt.setText(horario.getHoraFinal());
        	if (horarioCelular.getVisibility() == View.VISIBLE){ 
            	horarioCelular.setVisibility(View.INVISIBLE);
            }
            Toast.makeText(this, "Horario final atualizado",Toast.LENGTH_LONG).show();
        }catch(SQLiteException e){
            aux=false;
            Toast.makeText(this, "Erro: " + e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }
        return aux;
    }
    
    private String pesquisaMatricula(){
    	SQLiteDatabase banco = null;
    	Cursor c = null;
    	String matricula = "";
        try {
            banco = openOrCreateDatabase(Principal.nomeBanco, 0, null);
            c = banco.query(true, "matricula", new String[]{"id"}, null, null, null, null, null, null);
            if (c.moveToNext() && matricula.equals("")){
            	matricula = c.getString(c.getColumnIndex("id"));
            }
        } catch (SQLiteException e) {        
     	   Toast.makeText(this, "Falha na pesquisa no BD: "+e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        } finally {
            c.close();
            banco.close();
        }
        return matricula; 
    }
    
    private String pesquisaHora(String data, String tipo){
    	SQLiteDatabase banco = null;
    	Cursor c = null;
    	String hora = "00:00";
        try {
            banco = openOrCreateDatabase(Principal.nomeBanco, 0, null);
            String where = "data = \"" + data + "\"";
            if (tipo.equals("inicio")){
              c = banco.query(true, "horario", new String[]{"data","hora_inicio"}, where, null, null, null, null, null);
            } else {
            	c = banco.query(true, "horario", new String[]{"data","hora_final"}, where, null, null, null, null, null);
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
            banco.close();
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
             result.add(c.getString(c.getColumnIndex("data")) + ":\t\t" + 
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

    public void sync(){
    	SQLiteDatabase banco = null;
    	Cursor c = null;
    	String where = "status = 'P'";
        
    	final String NAMESPACE = "http://zaboroski.no-ip.info:9080/PontoMobile/services/Transmissao";
    	String URL = "http://zaboroski.no-ip.info:9080/PontoMobile/services/Transmissao";
    	final String METHOD_NAME = "gravarRegistroXML";
    	final String SOAP_ACTION = "http://zaboroski.no-ip.info:9080/PontoMobile/services/Transmissao#gravarRegistroXML";
    	
    	
    	Toast.makeText(this, "Aguarde a sincroniação", Toast.LENGTH_LONG).show();
    	try {
            banco = openOrCreateDatabase(Principal.nomeBanco, 0, null);
            c = banco.query(true, "horario", new String[]{"data","hora_inicio","hora_final"}, where, null, null, null, null, null);
            while (c.moveToNext()){
            	//Criando os parâmetros de entrada
    	    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    	    	request.addProperty("matricula", matricula);
    	    	request.addProperty("data", c.getString(c.getColumnIndex("data")));
    	    	request.addProperty("horario_entrada", c.getString(c.getColumnIndex("hora_inicio"))+":00");
    	    	request.addProperty("horario_saida", c.getString(c.getColumnIndex("hora_final"))+":00");
    	    	//Envelope SOAP
    	    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	    	envelope.setOutputSoapObject(request);
    	    	HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
    	    	try {
    	    	   //Chamada ao WS
    	    	   androidHttpTransport.call(SOAP_ACTION, envelope);
    	    	   //String com o retorno
    	    	   SoapObject result=(SoapObject)envelope.bodyIn;
    	    	   //Toast.makeText(this, result.getProperty(0).toString(), Toast.LENGTH_LONG).show();
    	    	   if (result.getProperty(0).toString().equals("true")) {
    	    		   banco.execSQL("UPDATE horario SET status = 'A' WHERE data = '" + 
    	    				   c.getString(c.getColumnIndex("data")) + "'");
    	    	   }
    	    	} catch (Exception e) {
    	    		Toast.makeText(this, "Sincronização não Concluida" + e.getMessage(), 
    	    				Toast.LENGTH_LONG).show();
    	    	}
            }
        } catch (SQLiteException e) {        
     	   Toast.makeText(this, "Falha na pesquisa no BD: "+e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        } finally {
            c.close();
            banco.close();
            Toast.makeText(this, "Sincronização Concluida", Toast.LENGTH_LONG).show();
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
        MenuItem mnu3 = menu.add(0, 2, 2, "Sincronizar");
        {
            mnu3.setAlphabeticShortcut('c');
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
        case 2:
        	if (matricula.equals("")){
        		matricula = pesquisaMatricula();
        	}
        	sync();
        	/*Toast.makeText(this, "Sincronizacao ainda indisponivel por falta de dados!", 
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