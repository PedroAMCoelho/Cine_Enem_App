package br.infnet.com.cineenem.Entidade;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by suiamcosta on 10/11/17.
 */

public class Item implements Serializable {

    private int foto;
    private String titulo;
    private String tipo;
    private String descricao;
    private String codigo_youtube;
    private String codigo_imdb;
    private String categoria;

    public Item(String titulo, String descricao, String codigo_youtube, String codigo_imdb,String categoria) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.codigo_youtube = codigo_youtube;
        this.codigo_imdb = codigo_imdb;
        this.categoria = categoria;
    }

    public String getCodigo_youtube() {
        return codigo_youtube;
    }

    public String getCodigo_imdb() {
        return codigo_imdb;
    }

    public int getFoto() { return foto;}

    public String getTitulo() {return titulo;}

    public String getTipo() { return tipo;}

    public String getDescricao() { return descricao; }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + titulo.length();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item) obj;
        if (!Objects.equals(titulo, other.titulo))
            return false;
        return true;
    }
}
