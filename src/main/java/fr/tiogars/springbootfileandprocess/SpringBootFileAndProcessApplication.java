package fr.tiogars.springbootfileandprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Spring Boot File And Process.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class SpringBootFileAndProcessApplication {

  /**
   * Default constructor.
   */
  protected SpringBootFileAndProcessApplication() {
    // Default constructor
  }

  /**
   * Main method to start the Spring Boot application.
   * @param args Command line arguments.
   */
  public static void main(final String[] args) {
    SpringApplication.run(SpringBootFileAndProcessApplication.class, args);
  }
}
