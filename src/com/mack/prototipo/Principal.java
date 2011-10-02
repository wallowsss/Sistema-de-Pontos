package com.mack.prototipo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Intent;

public class Principal extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //startActivity(new Intent( this, listaRelat.class));
        
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