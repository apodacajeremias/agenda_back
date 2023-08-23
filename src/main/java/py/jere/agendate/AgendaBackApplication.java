package py.jere.agendate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgendaBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaBackApplication.class, args);
		System.out.println(System.getProperty("os.name"));
	}

}