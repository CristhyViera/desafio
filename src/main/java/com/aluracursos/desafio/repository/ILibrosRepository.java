package com.aluracursos.desafio.repository;

import com.aluracursos.desafio.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ILibrosRepository extends JpaRepository<Libros, Long> {
    Libros findByTitulo(String titulo);
    List<Libros> findByLenguajesContaining(String lenguajes);
}
