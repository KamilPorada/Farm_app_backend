package pl.farmapp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.farmapp.backend.config.KindeConfig;

@SpringBootApplication
public class FarmAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmAppBackendApplication.class, args);
	}

}

