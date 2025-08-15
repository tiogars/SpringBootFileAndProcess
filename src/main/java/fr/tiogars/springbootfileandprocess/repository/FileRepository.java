package fr.tiogars.springbootfileandprocess.repository;

import java.io.File;
import java.util.List;

/**
 * Repository interface for file operations.
 */
public interface FileRepository {

    /**
     * List all files in a directory.
     *
     * @param directory The directory to list files from.
     * @return A list of files.
     */
    List<File> listFiles(String directory);
}
