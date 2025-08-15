package fr.tiogars.springbootfileandprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Spring Boot File And Process.
 * <p>
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
public class SpringBootFileAndProcessApplication {

  /**
   * Constructor for SpringBootFileAndProcessApplication.
   */
  public SpringBootFileAndProcessApplication()
  {
    // Constructor
  }

  /**
   * Main method to start the Spring Boot application.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(SpringBootFileAndProcessApplication.class, args);
  }

}