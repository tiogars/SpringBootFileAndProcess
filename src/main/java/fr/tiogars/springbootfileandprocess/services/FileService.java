package fr.tiogars.springbootfileandprocess.services;

import java.util.List;

/**
 * Service interface for managing files.
 */
public interface FileService {

    /**
     * Lists files in a directory.
     * @param directory the directory to list files from
     * @return a list of file information objects
     */
    List<FileInfo> listFiles(String directory);
}
