package com.example.estevesklein.estudosimagesdynamically;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alu201512810 on 22/10/2015.
 */
public class SqliteController extends SQLiteOpenHelper {

    public static final String DOCUMENTO_TABLE = "documento";
    public static final String IMAGEM_TABLE = "imagem";


    public static final String ID_COLUMN = "id";
    public static final String TITULO_COLUMN = "titulo";
    public static final String URI_COLUMN = "uri";
    public static final String URI_THUMBS_COLUMN = "uri_thumbs";
    public static final String DOCUMENTO_IMAGEM_ID = "documento_id";

    public static final String CREATE_DOCUMENTO_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DOCUMENTO_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITULO_COLUMN + " TEXT"
            + ")";

    public static final String CREATE_IMAGEM_TABLE = "CREATE TABLE IF NOT EXISTS "
            + IMAGEM_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + URI_COLUMN + " TEXT, "
            + URI_THUMBS_COLUMN + " TEXT, "
            + DOCUMENTO_IMAGEM_ID + " INT, "
            + "FOREIGN KEY(" + DOCUMENTO_IMAGEM_ID + ") REFERENCES "
            + DOCUMENTO_TABLE + "(id) " + ")";

    public SqliteController(Context applicationcontext){
        super(applicationcontext, "teste.db", null, 1);
        //super(applicationcontext, "carteiraEletronica.db", null, 2);
    }

    public void onCreate(SQLiteDatabase database){

        //String query = "CREATE TABLE IF NOT EXISTS " +
        //        "documento (Id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT)";
        //database.execSQL(query);

        database.execSQL(CREATE_DOCUMENTO_TABLE);
        database.execSQL(CREATE_IMAGEM_TABLE);
    }

    public void inserirDocumento(Documento d){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITULO_COLUMN, d.getTitulo());

        long id = database.insert(DOCUMENTO_TABLE, null, values);

        database.close();
        d.setId((int) id);
        //return id;
    }


    public List<Documento> listarDocumentos(){
        List<Documento> documentos = new ArrayList<Documento>();

        String query = "SELECT * FROM " + DOCUMENTO_TABLE;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                Documento p = new Documento();
                p.setId(cursor.getInt(0));
                p.setTitulo(cursor.getString(1));

                documentos.add(p);
            } while(cursor.moveToNext());
        }
        return documentos;
    }



    public void inserirImagem(Imagem i){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(URI_COLUMN, i.getUri());
        values.put(URI_THUMBS_COLUMN, i.getUri_thumbs());
        values.put(DOCUMENTO_IMAGEM_ID, i.getDocumentoId());

        long id = database.insert(IMAGEM_TABLE, null, values);

        database.close();
        i.setId((int) id);
        //return id;
    }

    public List<Imagem> listarImagens(int documentoId){
        List<Imagem> imagens = new ArrayList<Imagem>();

        String query = "SELECT * FROM " + IMAGEM_TABLE + " WHERE " + DOCUMENTO_IMAGEM_ID + "=" + documentoId;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                Imagem i = new Imagem();
                i.setId(cursor.getInt(0));
                i.setUri(cursor.getString(1));
                i.setUri_thumbs(cursor.getString(2));
                i.setDocumentoId(cursor.getInt(3));

                imagens.add(i);
            } while(cursor.moveToNext());
        }
        return imagens;
    }


    // update da estrutura do banco de dados
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

}
