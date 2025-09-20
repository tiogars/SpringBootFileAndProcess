package fr.tiogars.springbootfileandprocess.repository;

import org.springframework.stereotype.Component;

/**
 * Implementation of {@link SystemRepository} for accessing system environment
 * variables.
 */
@Component
public class SystemRepositoryImpl implements SystemRepository {

    /**
     * Default constructor.
     */
    public SystemRepositoryImpl() {
        // Default constructor
    }

    /**
     * Gets the computer name from the environment variable.
     *
     * @return The computer name as a String.
     */
    @Override
    public String getComputerName() {
        return System.getenv("COMPUTERNAME");
    }
}
