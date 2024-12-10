package studia.animalshelterapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private final DataSource dataSource;

    public Application(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			// Sprawdzamy, czy połączenie z bazą działa
			System.out.println("Sprawdzanie połączenia z bazą danych...");
			var connection = dataSource.getConnection();
			if (connection != null) {
				System.out.println("Połączenie z bazą danych zostało pomyślnie nawiązane!");
			} else {
				System.out.println("Nie udało się nawiązać połączenia z bazą danych.");
			}
		} catch (Exception e) {
			System.out.println("Błąd podczas połączenia z bazą danych: " + e.getMessage());
		}
	}
}
