package com.mack.prototipo;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity; 

public class listaRelat extends ListActivity 
{
    String[] presidents = {
            "Dwight D. Eisenhower",
            "John F. Kennedy",
            "Lyndon B. Johnson",
            "Richard Nixon",
            "Gerald Ford",
            "Jimmy Carter",
            "Ronald Reagan",
            "George H. W. Bush",
            "Bill Clinton",
            "George W. Bush",
            "Barack Obama"
    };
 
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.relatorio);
        
        setListAdapter(new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, presidents));
    }    
 
    public void onListItemClick(
    ListView parent, View v,
    int position, long id) {   
        Toast.makeText(this, 
            "Entrada: " + presidents[position], 
            Toast.LENGTH_SHORT).show();
       } 
}
