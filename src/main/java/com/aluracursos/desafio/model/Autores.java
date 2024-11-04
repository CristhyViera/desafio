package com.aluracursos.desafio.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")

public class Autores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String name;
    private int yearNacimiento;
    private int yearMuerte;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libros> libros=new ArrayList<>();

    public Autores(DatosAutor datosAutor){
        this.name = datosAutor.nombre();
        this.yearNacimiento = datosAutor.fechaDeNacimiento();
        this.yearMuerte = datosAutor.fechaDeMuerte();

    }

    public Autores (){}

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearNacimiento() {
        return yearNacimiento;
    }

    public void setYearNacimiento(int yearNacimiento) {
        this.yearNacimiento = yearNacimiento;
    }

    public int getYearMuerte() {
        return yearMuerte;
    }

    public void setYearMuerte(int yearMuerte) {
        this.yearMuerte = yearMuerte;
    }

    public List<Libros> getLibros() {
        return libros;
    }

    public void setLibros(List<Libros> libros) {
        this.libros = libros;
    }

    @Override
    //Procedimiento para obtener el título de los libros
    public String toString() {
        StringBuilder librosTitulo = new StringBuilder();
        for (Libros libro : libros){
            librosTitulo.append(libro.getTitulo()).append(", ");
        }

        //Eliminar la última coma y espacio

        if(librosTitulo.length()>0){
            librosTitulo.setLength(librosTitulo.length()-2);

        }
        return "********* Datos del Autor *********" + "\n" +
                "Autor: " + name + "\n" +
                "Año de nacimiento: " + yearNacimiento + "\n" +
                "Año de fallecimiento: " + yearMuerte + "\n" +
                "Libros: " + libros + "\n";
    }


}
