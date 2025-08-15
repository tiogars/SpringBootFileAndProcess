package fr.tiogars.springbootfileandprocess.repository;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Implementation of {@link FileRepository} for file operations.
 */
@Component
public class FileRepositoryImpl implements FileRepository {

    /**
     * Lists all files in a directory.
     */
    @Override
    public List<File> listFiles(String directory) {
        File dir = new File(directory);
        return Arrays.asList(dir.listFiles());
    }
}
