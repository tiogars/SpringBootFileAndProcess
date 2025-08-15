package fr.tiogars.springbootfileandprocess.services;

import org.springframework.stereotype.Service;

import fr.tiogars.springbootfileandprocess.repository.SystemRepository;


/**
 * Implementation of {@link SystemService} for system operations.
 */
@Service
public class SystemServiceImpl implements SystemService {

    /**
     * Repository for system information.
     */
    private final SystemRepository systemRepository;

    /**
     * Constructor for SystemServiceImpl.
     *
     * @param systemRepositoryParam The system repository interface.
     */
    public SystemServiceImpl(final SystemRepository systemRepositoryParam) {
        this.systemRepository = systemRepositoryParam;
    }

    /**
     * Gets the computer name from the system repository.
     *
     * @return The computer name as a String.
     */
    @Override
    public String getComputerName() {
        return systemRepository.getComputerName();
    }
}
