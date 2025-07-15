package dk.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class Ss2Application {

	public static void main(String[] args) {
		SpringApplication.run(Ss2Application.class, args);
	}
	@GetMapping("/")
	public String index() {
		return "test";
	}

}
