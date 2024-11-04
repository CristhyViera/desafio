package com.aluracursos.desafio.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "libros")

public class Libros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable=false)
    private Autores autor;

    @Column(name = "nombre_autor")
    private String nombreAutor;

    @Column(name = "lenguajes")
    private String lenguajes;
    private double numeroDescargas;

    public Libros(){}

    public Libros(DatosLibros datosLibros, Autores autor){
        this.titulo = datosLibros.titulo();
        setLenguajes(datosLibros.idiomas());
        this.autor = autor;
        this.nombreAutor = autor.getName();
        this.numeroDescargas = datosLibros.numeroDeDescargas();

    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autores getAutor() {
        return autor;
    }

    public void setAutor(Autores autor) {
        this.autor = autor;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public List<String> getLenguajes() {
        return Arrays.asList(lenguajes.split(","));
    }

    public void setLenguajes(List<String> lenguajes) {
        this.lenguajes = String.join(",", lenguajes);
    }

    public double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        return "********* Datos del libro *********" + "\n" +
                "Título: " + titulo + "\n" +
                "Autor: " + nombreAutor + "\n" +
                "Idioma: " + lenguajes + "\n" +
                "Número de descargas: " + numeroDescargas + "\n";
    }
}
