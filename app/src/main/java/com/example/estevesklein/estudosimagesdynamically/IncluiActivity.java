package com.example.estevesklein.estudosimagesdynamically;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class IncluiActivity extends MainActivity implements OnClickListener {

    Button captureBtn = null;
    final int CAMERA_CAPTURE = 1;
    private Uri picUri;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid;
    private  List<String> listOfImagesPath;
    private String imgFileName;

    public static final String GridViewDemo_ImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CarteiraEletronica-Esteves/";
    public static final String GridViewDemo_ImagePathThumb = GridViewDemo_ImagePath + "thumbs/";
    public static final String GridViewDemo_ImagePathTmp = GridViewDemo_ImagePath + "tmp/";
    public static final String GridViewDemo_ImagePathThumbTmp = GridViewDemo_ImagePathTmp + "thumbs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inclui);

        captureBtn = (Button)findViewById(R.id.capture_btn1);
        captureBtn.setOnClickListener(this);
        grid = ( GridView) findViewById(R.id.gridviewimg);

        listOfImagesPath = null;
        listOfImagesPath = RetriveCapturedImagePath();
        if(listOfImagesPath!=null){
            grid.setAdapter(new ImageListAdapter(this,listOfImagesPath));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View arg0) {

        int id = arg0.getId();

        // TODO Auto-generated method stub
        if (arg0.getId() == R.id.capture_btn1) {
            try {
                /*
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
                */

                String imgcurTime = dateFormat.format(new Date());
                File imageDirectory = new File(GridViewDemo_ImagePathTmp);
                imageDirectory.mkdirs();
                //String _pathTumbs = GridViewDemo_ImagePathTmp + imgcurTime+".jpg";
                imgFileName = imgcurTime+".jpg";
                String _pathImg = GridViewDemo_ImagePathTmp + imgFileName;

                File image = new File(imageDirectory, imgFileName);
                Uri uriSavedImage = Uri.fromFile(image);

                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(captureIntent, CAMERA_CAPTURE);

            } catch(ActivityNotFoundException anfe){
                //display an error message
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void salvar(View v){
        EditText et =(EditText) findViewById(R.id.inputTitulo);
        Documento p = new Documento();
        p.setTitulo(et.getText().toString());
        p.salvar(this);


        listOfImagesPath = null;
        listOfImagesPath = RetriveCapturedImagePath();
        if(listOfImagesPath!=null){
            salvarImagens(p.getId(), listOfImagesPath);
        }

        Intent i = new Intent(this, ListaActivity.class);
        startActivity(i);
    }

    protected void salvarImagens(Integer documentoId, List<String> imgs){
        String filename;

        File imageDirectory = new File(GridViewDemo_ImagePathThumb);
        imageDirectory.mkdirs();

        //img.setUri(et.getText().toString());
        //img.salvar(this);
        for (String pathThumbTmp : imgs) {
            Imagem img = new Imagem();
            filename=pathThumbTmp.substring(pathThumbTmp.lastIndexOf("/")+1);
            img.setUri(GridViewDemo_ImagePath + filename);
            img.setUri_thumbs(GridViewDemo_ImagePathThumb + filename);
            img.setDocumentoId(documentoId);
            img.salvar(this);

            // copia Imgs: Thumbs Tmp para Thumbs
            copy(GridViewDemo_ImagePathThumbTmp + filename, GridViewDemo_ImagePathThumb + filename);

            // copia Imgs: Tmp para Thumbs
            copy(GridViewDemo_ImagePathTmp + filename, GridViewDemo_ImagePath + filename);
        }

        deleteFilesTmp();

        //System.out.println();
    }


    protected void deleteFilesTmp(){
        deleteFolderThumbTmp();
        deleteFolderTmp();
    }

    protected void deleteFolderTmp(){
        File dir = new File(GridViewDemo_ImagePathTmp);
        if (dir.isDirectory()){
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++){
                new File(dir, children[i]).delete();
            }
        }
    }

    protected void deleteFolderThumbTmp(){
        File dir = new File(GridViewDemo_ImagePathThumbTmp);
        if (dir.isDirectory()){
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++){
                new File(dir, children[i]).delete();
            }
        }
    }

    /*
    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    */

    public void copy(String src, String dst) {
        try
        {
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        catch(Exception ex) {

        }
    }


    protected void buildThumbTmp(String fileName){
        try
        {
            final int THUMBNAIL_SIZE = 128;

            String _pathTumbs = GridViewDemo_ImagePathThumbTmp + fileName;

            File imageDirectory = new File(GridViewDemo_ImagePathThumbTmp);
            imageDirectory.mkdirs();

            FileInputStream fis = new FileInputStream(GridViewDemo_ImagePathTmp + fileName);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            // tratamento para rotacionar thumbs, caso necessário
            Integer imgRotacionar = 0;

            //Integer wImgTmp = imageBitmap.getWidth();
            //if(imageBitmap.getWidth()>imageBitmap.getHeight()){
            //    imgRotacionar = 90;
            //}
            // corrigir a orientação para gerar o thumbs
            File imageFile = new File(GridViewDemo_ImagePathTmp + fileName);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    imgRotacionar = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    imgRotacionar = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    imgRotacionar = 90;
                    break;
            }

            // 02.12.2015 - crop square image
            Bitmap dstBmp;

            if (imageBitmap.getWidth() >= imageBitmap.getHeight()){

                dstBmp = Bitmap.createBitmap(
                        imageBitmap,
                        imageBitmap.getWidth()/2 - imageBitmap.getHeight()/2,
                        0,
                        imageBitmap.getHeight(),
                        imageBitmap.getHeight()
                );

            }else{

                dstBmp = Bitmap.createBitmap(
                        imageBitmap,
                        0,
                        imageBitmap.getHeight()/2 - imageBitmap.getWidth()/2,
                        imageBitmap.getWidth(),
                        imageBitmap.getWidth()
                );
            }


            dstBmp = BitmapUtils.resizeBitmap(dstBmp, THUMBNAIL_SIZE, THUMBNAIL_SIZE, imgRotacionar);
            //imageBitmap = BitmapUtils.resizeBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, imgRotacionar);

            //imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, true);
            //imageBitmap = Bitmap.createScaledBitmap(imageBitmap,(int)(imageBitmap.getWidth()*0.3), (int)(imageBitmap.getHeight()*0.3), true);

            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //imageData = baos.toByteArray();

            FileOutputStream out = new FileOutputStream(_pathTumbs);

            dstBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            //imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            fis.close();

        }
        catch(Exception ex) {

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){

                buildThumbTmp(imgFileName);
                /*
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                File imageDirectory = new File(GridViewDemo_ImagePathThumbTmp);
                imageDirectory.mkdirs();
                String _pathTumbs = GridViewDemo_ImagePathThumbTmp + imgFileName;

                try {
                    FileOutputStream out = new FileOutputStream(_pathTumbs);

                    thePic.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    //if(deleteFile(thePic.getPath()))
                    //    Log.i(TAG, "Image deleted.");

                } catch (FileNotFoundException e) {
                    e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */

                listOfImagesPath = null;
                listOfImagesPath = RetriveCapturedImagePath();
                if(listOfImagesPath!=null){
                    grid.setAdapter(new ImageListAdapter(this,listOfImagesPath));
                }
            }
        }
    }

    private List<String> RetriveCapturedImagePath() {
        List<String> tFileList = new ArrayList<String>();
        File f = new File(GridViewDemo_ImagePathThumbTmp);
        if (f.exists()) {
            File[] files=f.listFiles();
            Arrays.sort(files);

            for(int i=0; i<files.length; i++){
                File file = files[i];
                if(file.isDirectory())
                    continue;
                tFileList.add(file.getPath());
            }
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