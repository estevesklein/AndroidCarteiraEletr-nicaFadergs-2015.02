package com.example.estevesklein.estudosimagesdynamically;

import android.content.Context;

import java.util.List;

/**
 * Created by Esteves Klein on 17/11/2015.
 */
public class Documento {

    private int id;
    private String titulo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void salvar(Context c){
        SqliteController db = new SqliteController(c);
        db.inserirDocumento(this);
    }

    public List<Documento> listar(Context c){
        SqliteController db = new SqliteController(c);
        return db.listarDocumentos();
    }
}
