package br.com.precobaixo.camera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.precobaixo.camera.scraping.OrganizadorDados;

@SpringBootApplication
public class CameraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CameraApplication.class, args);
		OrganizadorDados dados = new OrganizadorDados();
	}

}
