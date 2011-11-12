package com.mack.prototipo;

import java.util.ArrayList;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity; 
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class listaRelat extends ListActivity 
{
	ListView lista ;
	
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.relatorio);
        
        lista = (ListView) findViewById(R.id.list);
        Toast.makeText(this, "achei a lista",Toast.LENGTH_LONG).show();
        limparLista();
        Toast.makeText(this, "limpei a lista",Toast.LENGTH_LONG).show();
        pesquisar();
        
    }
 
    private void limparLista(){   
        lista.setAdapter( new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
    }
    
    private void  pesquisar(){
        ArrayAdapter<String> fileList = null;
        SQLiteDatabase bd = null;
        Cursor c = null;
        
        try {
//           Toast.makeText(this, "Antes do BD",Toast.LENGTH_LONG).show();
           bd = openOrCreateDatabase(Principal.nomeBanco, 0, null);
//           Toast.makeText(this, "Depois do BD",Toast.LENGTH_LONG).show();
           // pesquisa o campos passandos no vetor de string no banco de dados
           // e adiciona a o resultado da consulta no objeto Cursor
           c = bd.query(true, "horario", new String[]{"data","hora_inicial","hora_final"}, null, null, null, null, null, null);   
//           Toast.makeText(this, "Depois da query",Toast.LENGTH_LONG).show();
           ArrayList<String> result = new ArrayList<String>();        
           while(c.moveToNext()){
             //adiciona a visao da lista
             result.add(c.getString(c.getColumnIndex("data")) +
                    ": "+c.getString(c.getColumnIndex("hora_inicial")) + " <-> " +
                    c.getString(c.getColumnIndex("hora_final")));      
           }
//           Toast.makeText(this, "Depois do loop",Toast.LENGTH_LONG).show();
           fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result); //cria o modelo
           lista.setAdapter(fileList); //instala a lista
//           Toast.makeText(this, "Instala a lista",Toast.LENGTH_LONG).show();
       } catch (Exception e) {        
    	   Toast.makeText(this, "Falha na pesquisa no BD: "+e.getMessage(), 
           		Toast.LENGTH_LONG).show();
       }finally{
           bd.close();
           c.close();
       }    
    }
}
