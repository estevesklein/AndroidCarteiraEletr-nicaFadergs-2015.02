package com.example.estevesklein.estudosimagesdynamically;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // definição do menu principal
        switch(id){
            case R.id.listar:
                Intent i = new Intent(this, ListaActivity.class);
                startActivity(i);
                return true;
            //break;

            case R.id.incluir:
                Intent a = new Intent(this, IncluiActivity.class);
                startActivity(a);
                return true;
            //break;
        }

        return super.onOptionsItemSelected(item);
    }
}
