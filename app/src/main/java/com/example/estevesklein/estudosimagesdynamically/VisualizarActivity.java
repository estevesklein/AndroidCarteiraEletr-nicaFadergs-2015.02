package com.example.estevesklein.estudosimagesdynamically;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisualizarActivity extends MainActivity  {

    private GridView grid;
    private List<String> listOfImagesPath;
    private int documentoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        grid = ( GridView) findViewById(R.id.gridviewimg);

        Intent intent = getIntent();
        Bundle parametros = intent.getExtras();
        this.documentoId = parametros.getInt("documento_id");
        String documento_titulo = parametros.getString("documento_titulo");

        TextView txtDocTitulo = (TextView) findViewById(R.id.DocumentoTitulo);
        txtDocTitulo.setText(documento_titulo);

        listarThumbs();


        // Capture GridView item click
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent i = new Intent(getApplicationContext(), VisualizarImagemActivity.class);

                Toast.makeText(getApplicationContext(),
                        "Posição: " + id, Toast.LENGTH_SHORT).show();

                i.putExtra("pathThumbs", listOfImagesPath.get(position));
                startActivity(i);
            }
        });

    }

    public void listarThumbs(){

        listOfImagesPath = null;
        listOfImagesPath = RetriveDbImagePath();
        if(listOfImagesPath!=null){
            grid.setAdapter(new ImageListAdapter(this,listOfImagesPath));
        }
    }


    private List<String> RetriveDbImagePath() {
        Imagem imagem = new Imagem();
        List lista = imagem.listar(VisualizarActivity.this, this.documentoId);

        List<String> tFileList = new ArrayList<String>();

        for (Object x : lista) {
            Imagem i = (Imagem) x;

            tFileList.add(i.getUri_thumbs());
        }

        return tFileList;
    }



    public class ImageListAdapter extends BaseAdapter
    {
        private Context context;
        private List<String> imgPic;
        public ImageListAdapter(Context c, List<String> thePic)
        {
            context = c;
            imgPic = thePic;
        }
        public int getCount() {
            if(imgPic != null)
                return imgPic.size();
            else
                return 0;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            BitmapFactory.Options bfOptions=new BitmapFactory.Options();
            bfOptions.inDither=false;                     //Disable Dithering mode
            bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
            bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
            bfOptions.inTempStorage=new byte[32 * 1024];
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }
            FileInputStream fs = null;
            Bitmap bm;
            try {
                fs = new FileInputStream(new File(imgPic.get(position).toString()));

                if(fs!=null) {
                    bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                    imageView.setImageBitmap(bm);
                    imageView.setId(position);
                    //imageView.setLayoutParams(new GridView.LayoutParams(200, 160));
                    imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(fs!=null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return imageView;
        }
    }




}
