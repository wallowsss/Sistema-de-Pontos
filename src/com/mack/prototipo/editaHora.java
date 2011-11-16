package com.mack.prototipo;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class editaHora extends Activity {
	
	TimePicker EditaEntrada;
	TextView HoraEntrada;
	TimePicker EditaSaida;
	TextView HoraSaida;
	Button Atualizar;
	
	@Override  
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.edita_hora);
        
        String hora_inicio;
        String hora_final;
        final String data;
        Date dt = new Date( System.currentTimeMillis());
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
        HoraEntrada = (TextView) findViewById(R.id.entrada_edita);
        EditaEntrada = (TimePicker) findViewById(R.id.edita_tempo_entrada);
        HoraSaida = (TextView) findViewById(R.id.saida_edita);
        EditaSaida = (TimePicker) findViewById(R.id.edita_tempo_saida);
        Atualizar = (Button) findViewById(R.id.atualizar);
        
        EditaEntrada.setIs24HourView(true);
        EditaSaida.setIs24HourView(true);
        data =formatador.format(dt);
        hora_inicio = pesquisaHora(data, "inicio");
        hora_final = pesquisaHora(data, "final");
        
        HoraEntrada.setText(hora_inicio);
        HoraSaida.setText(hora_final);
        
        EditaEntrada.setCurrentHour(new Integer(hora_inicio.substring(0, hora_inicio.indexOf(":"))));
        EditaEntrada.setCurrentMinute(new Integer(hora_inicio.substring(hora_inicio.indexOf(":")+1)));
        
        EditaSaida.setCurrentHour(new Integer(hora_final.substring(0, hora_final.indexOf(":"))));
        EditaSaida.setCurrentMinute(new Integer(hora_final.substring(hora_final.indexOf(":")+1)));
        
        Atualizar.setOnClickListener(new Button.OnClickListener() {
        	@Override
            public void onClick(View arg0) {
        		Horario horario = new Horario(data, "", "");
        		String hora = formataData(EditaEntrada.getCurrentHour(), EditaEntrada.getCurrentMinute());
        		HoraEntrada.setText(hora);
        		horario.setHoraInicial(hora);
        		
        		hora = formataData(EditaSaida.getCurrentHour(), EditaSaida.getCurrentMinute());
        		HoraSaida.setText(hora);
        		horario.setHoraFinal(hora);
        		
        		update(horario);
            }
        });
        
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
	
	public boolean update(Horario horario){
    	SQLiteDatabase banco = null;
    	boolean aux = true;
        try{
            
            banco = openOrCreateDatabase(Principal.nomeBanco, 0, null);
            banco.execSQL("UPDATE horario SET hora_inicio = '" + horario.getHoraInicial()
            		+ "', hora_final = '" + horario.getHoraFinal() + "' WHERE data = '" + horario.getData() + "'");
        	banco.close();
        	HoraEntrada.setText(horario.getHoraInicial());
        	HoraSaida.setText(horario.getHoraFinal());
        	
            Toast.makeText(this, "Horario atualizado", Toast.LENGTH_LONG).show();
        }catch(SQLiteException e){
            aux=false;
            Toast.makeText(this, "Erro: " + e.getMessage(), 
            		Toast.LENGTH_LONG).show();
        }
        return aux;
    }
	
	public static String formataData(Integer hora, Integer minuto){
		String retorno = "";
		if (hora < 10){
			retorno = "0"+hora+":";
		} else {
			retorno = hora+":";
		}
		if (minuto < 10){
			retorno = retorno+"0"+minuto;
		} else {
			retorno = retorno+minuto;
		}
		return retorno;
	}

}
