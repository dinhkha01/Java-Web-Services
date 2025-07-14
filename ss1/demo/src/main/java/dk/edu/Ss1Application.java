package dk.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class Ss1Application {

	public static void main(String[] args) {
		SpringApplication.run(Ss1Application.class, args);
	}
	@GetMapping("/")
	public String index() {
		return "home";
	}

}
