package com.aluracursos.desafio;

import com.aluracursos.desafio.Principal.Principal;
import com.aluracursos.desafio.repository.IAutoresRepository;
import com.aluracursos.desafio.repository.ILibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioApplication implements CommandLineRunner {

	@Autowired
	private ILibrosRepository librosRepositorio;

	@Autowired
	private IAutoresRepository autoresRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(DesafioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(librosRepositorio, autoresRepositorio);
		principal.muestraElMenu();
	}
}
