package com.example.estevesklein.estudosimagesdynamically;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class VisualizarImagemActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_imagem);

        Intent i = getIntent();
        String pathThumbs = i.getExtras().getString("pathThumbs");

        String pathImgLarge = pathThumbs.replace("/thumbs", "/");

        Bitmap imgLargeBmp = BitmapFactory.decodeFile(pathImgLarge);

        ImageView imageView = (ImageView) findViewById(R.id.image);

        imageView.setImageBitmap(imgLargeBmp);
    }

}
