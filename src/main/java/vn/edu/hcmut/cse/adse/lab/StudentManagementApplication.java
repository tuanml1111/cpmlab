package vn.edu.hcmut.cse.adse.lab;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagementApplication {

	public static void main(String[] args) {
		// Doc file .env va set thanh System properties truoc khi Spring khoi dong
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

		SpringApplication.run(StudentManagementApplication.class, args);
	}

}
