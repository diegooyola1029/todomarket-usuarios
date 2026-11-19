package minimercado.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "minimercado")
@EntityScan("minimercado.repositorio")
public class TodoMarketBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoMarketBackendApplication.class, args);
    }
}
