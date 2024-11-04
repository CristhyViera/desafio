package com.aluracursos.desafio.Principal;

import com.aluracursos.desafio.ConvierteDatos;
import com.aluracursos.desafio.model.*;
import com.aluracursos.desafio.repository.IAutoresRepository;
import com.aluracursos.desafio.repository.ILibrosRepository;
import com.aluracursos.desafio.service.ConsumoAPI;

import java.util.*;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private static String API_BASE = "https://gutendex.com/books/?search=";
    private List<DatosLibros> datosLibros = new ArrayList<>();

    //Variables para las interfaces de inyeccion de dependencia
    private ILibrosRepository librosRepositorio;
    private IAutoresRepository autoresRepositorio;

    public Principal(ILibrosRepository librosRepositorio, IAutoresRepository autoresRepositorio) {
        this.librosRepositorio = librosRepositorio;
        this.autoresRepositorio = autoresRepositorio;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Agregar un libro por Nombre.
                    2 - Ver los libros registrados.
                    3 - Ver los autores registrados.
                    4 - Ver los autores vivos en un año determinado.
                    5 - Ver los libros por un idioma determinado.
                    6 - Top 10 libros más descargados (Gutendex ó Base de datos).
                    0- Salir

                    ************   Ingresa una opción   ************
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    agregarLibros();
                    break;
                case 2:
                    librosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresPorYear();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 6:
                    topDiezLibros();
                case 0:
                    System.out.println("Cerrando la aplicación");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private Datos getDatosLibros() {
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(API_BASE + nombreLibro.replace(" ", "+"));
        Datos datosLibros = conversor.obtenerDatos(json, Datos.class);
        return datosLibros;

    }

    private Libros crearLibros(DatosLibros datosLibros, Autores autor) {
        if (autor != null) {
            return new Libros(datosLibros, autor);
        } else {
            System.out.println("El autor no existe, vamos a crear el libro");
            return null;
        }
    }

    private void agregarLibros() {
        System.out.println("Ingresa el libro que deseas buscar: ");
        Datos datos = getDatosLibros();
        if (!datos.resultados().isEmpty()) {
            DatosLibros datoslibro = datos.resultados().get(0);
            DatosAutor datosAutor = datoslibro.autor().get(0);
            Libros libro = null;
            Libros libroRepositorio = librosRepositorio.findByTitulo(datoslibro.titulo());
            if (libroRepositorio != null) {
                System.out.println("Este libro ya se encuentra registrado");
                System.out.println(libroRepositorio.toString());
            } else {
                Autores autorRepositorio = autoresRepositorio.findByNameIgnoreCase(datoslibro.titulo());

                if (autorRepositorio != null) {
                    libro = crearLibros(datoslibro, autorRepositorio);
                    librosRepositorio.save(libro);
                    System.out.println("***** Libro agregado *****\n");
                    System.out.println(libro);
                } else {
                    Autores autor = new Autores(datosAutor);
                    autor = autoresRepositorio.save(autor);
                    libro = crearLibros(datoslibro, autor);
                    librosRepositorio.save(libro);
                    System.out.println("***** Libro agregado *****\n");
                    System.out.println(libro);
                }
            }
        } else {
            System.out.println("El libro buscado no se enncuentra registrado en la API, puedes ingresar otro!");
        }

    }

    public void librosRegistrados(){
        List<Libros> libros= librosRepositorio.findAll();
        if (libros.isEmpty()){
            System.out.println("Por ahora no tenemos libros registrados");
            return;
        }
        System.out.println("********* Listado de libros registrados *********");
        libros.stream().sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    public void autoresRegistrados(){
        List<Autores> autores = autoresRepositorio.findAll();
        if (autores.isEmpty()){
            System.out.println("Por ahora no existen autores registrados");
            return;
        }
        System.out.println("********* Listado de Autores registrados*********");
        autores.stream().sorted(Comparator.comparing(Autores::getName))
                .forEach(System.out::println);
    }

    private void autoresPorYear(){
        System.out.println("Ingresa el nombre del autor que deseas buscar");
        var year = teclado.nextInt();
        teclado.nextLine();
        if (year<0){
            System.out.println("Ingrese un año mayor a 0");
            return;
        }
        List<Autores> autoresPorYear = autoresRepositorio.findByYearNacimientoLessThanEqualAndYearMuerteGreaterThanEqual(year, year);
        if(autoresPorYear.isEmpty()){
            System.out.println("Aún no se encuentran Autores registrados para ese año");
            return;

        }
        System.out.println("***** Los autores registrados para el año: " + year+ "son los siguiente: *****\n");
        autoresPorYear.stream().sorted(Comparator.comparing(Autores::getName))
                .forEach(System.out::println);


    }

    public void librosPorIdioma(){
        System.out.println("Selecciona el idioma de tu preferencia:");
        String menu = """
                es - Para Español.
                en - Para Inglés.
                fr - Frances.
                pt - Portugués.
                """;
        System.out.println(menu);
        var idioma = teclado.nextLine();
        if (!idioma.equals("es") && !idioma.equals("en")&& !idioma.equals("fr") && !idioma.equals("pt")){
            System.out.println("Ups! No se encuentran libros en este idioma");
            return;
        }
        List<Libros> librosPorIdioma =  librosRepositorio.findByLenguajesContaining(idioma);
        if (librosPorIdioma.isEmpty()){
            System.out.println("Por ahora no tenemos libros registrados en ese idioma");
            return;
        }
        System.out.println("***** Los libros registrados en el idimoa:" + idioma + " son los siguientes*****\n");
        librosPorIdioma.stream().sorted(Comparator.comparing(Libros::getTitulo)).forEach(System.out::println);
    }

    public void topDiezLibros(){
        System.out.println("Selecciona la opción de donde quieres ver el top 10 de libros descargados");
        String menu = """ 
                1 - Gutendex
                2 - Base de datos
                """;
        System.out.println(menu);
        var opcion = teclado.nextInt();
        teclado.nextLine();

        if(opcion==1) {
            System.out.println("***** El top 10 de libros descargados en Gutendex es: *****\n");
            var json = consumoApi.obtenerDatos(API_BASE);
            Datos datos = conversor.obtenerDatos(json, Datos.class);
            List<Libros> libros = new ArrayList<>();
            if (libros.isEmpty()){
                System.out.println("No se encuentran libros registrados");
                return;
            };
            for (DatosLibros datosLibros : datos.resultados()) {
                Autores autor = new Autores(datosLibros.autor().get(0));
                Libros libro = new Libros(datosLibros, autor);
                libros.add(libro);
            }
            libros.stream()
                    .sorted(Comparator.comparing(Libros::getNumeroDescargas)
                    .reversed())
                    .limit(10)
                    .forEach(System.out::println);
        }else if (opcion == 2) {
            System.out.println("***** El top 10 de libros descargados en la base de datos es: *****\n");
            List<Libros> libros = librosRepositorio.findAll();
            if (libros.isEmpty()) {
                System.out.println("No se encuentran libros registrados");
                return;
            }
            libros.stream()
                    .sorted(Comparator.comparing(Libros::getNumeroDescargas)
                            .reversed())
                    .limit(10)
                    .forEach(System.out::println);


        }else {
            System.out.println("Opción no válida, intenta de nuevo");
        }

    }


}



