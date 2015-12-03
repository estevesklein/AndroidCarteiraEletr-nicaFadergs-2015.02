package com.example.estevesklein.estudosimagesdynamically;

import android.content.Context;

import java.util.List;

/**
 * Created by Esteves Klein on 17/11/2015.
 */
public class Imagem {

    private int id;
    private int documentoId;
    private String uri;
    private String uri_thumbs;

    public String getUri_thumbs() {
        return uri_thumbs;
    }

    public void setUri_thumbs(String uri_thumbs) {
        this.uri_thumbs = uri_thumbs;
    }

/*
    private String uriThumbs;
    public String getUriThumbs() {
        return uriThumbs;
    }

    public void setUriThumbs(String uriThumbs) {
        this.uriThumbs = uriThumbs;
    }
*/


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(int documentoId) {
        this.documentoId = documentoId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    @Override
    public String toString() {
        return "Imagem [id=" + id + ", uri=" + uri + ", documentoId="
                + documentoId + "]";
    }


    public void salvar(Context c) {
        SqliteController db = new SqliteController(c);
        db.inserirImagem(this);
    }

    public List<Imagem> listar(Context c, int documentoId){
        SqliteController db = new SqliteController(c);
        return db.listarImagens(documentoId);
    }
}
