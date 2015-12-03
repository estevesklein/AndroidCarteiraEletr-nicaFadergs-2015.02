package com.example.estevesklein.estudosimagesdynamically;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class ListaActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        listar();
    }

    public void listar(){
        Documento documento = new Documento();
        List lista = documento.listar(ListaActivity.this);

        TableLayout table = (TableLayout) findViewById(R.id.gridDocumentos);

        for (Object x : lista){
            Documento p = (Documento) x;

            TextView n = new TextView(this);
            n.setText(p.getTitulo());



            Button btView = new Button(this);
            btView.setText("visualizar");
            btView.setTag(p);

            Button btEdit = new Button(this);
            btEdit.setText("editar");
            btEdit.setTag(p);


            Button btDelete = new Button(this);
            btDelete.setText("deletar");
            btDelete.setTag(p);

            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    Intent i = new Intent(this, EditaActivity.class);
                    Pessoa pp = (Pessoa) v.getTag();
                    i.putExtra("pessoa", (Parcelable) pp);
                    */
                    //Bundle b = new Bundle();
                }
            });

            btView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(ListaActivity.this, VisualizarActivity.class);
                    Documento dd = (Documento) v.getTag();
                    //i.putExtra("documento", (Parcelable) dd);

                    Intent iV = new Intent(getApplicationContext(),VisualizarActivity.class);
                    //iV.putExtra("documento_id", documento_id);

                    Bundle params = new Bundle();
                    params.putInt("documento_id", dd.getId());
                    params.putString("documento_titulo", dd.getTitulo());
                    iV.putExtras(params);
                    startActivity(iV);
                }
            });

            TableRow linha = new TableRow(this);
            linha.addView(n);
            linha.addView(btView);
            //linha.addView(btEdit);
            //linha.addView(btDelete);
            table.addView(linha);


        } // for

    }
}
