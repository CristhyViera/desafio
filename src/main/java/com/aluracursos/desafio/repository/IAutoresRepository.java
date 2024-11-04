package com.aluracursos.desafio.repository;

import com.aluracursos.desafio.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAutoresRepository extends JpaRepository<Autores, Long> {
    Autores findByNameIgnoreCase(String nombre);

    List<Autores> findByYearNacimientoLessThanEqualAndYearMuerteGreaterThanEqual(int yearInicial, int yearFinal);
}
