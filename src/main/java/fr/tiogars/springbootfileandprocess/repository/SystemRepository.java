package fr.tiogars.springbootfileandprocess.repository;


/**
 * Repository interface for system information access.
 */
public interface SystemRepository {
    /**
     * Gets the computer name.
     *
     * @return The computer name as a String.
     */
    String getComputerName();
}
