package fr.tiogars.springbootfileandprocess.services;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.tiogars.springbootfileandprocess.repository.FileRepository;

/**
 * Implementation of the FileService interface.
 * @see FileService
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * The file repository for accessing file data.
     */
    private FileRepository fileRepository;

    /**
     * Default constructor.
     */
    public FileServiceImpl() {
        // Default constructor
    }

    /**
     * Constructor for FileServiceImpl.
     *
     * @param fileRepositoryParam the file repository to use
     */
    public FileServiceImpl(final FileRepository fileRepositoryParam) {
        this.fileRepository = fileRepositoryParam;
    }

    /**
     * Lists files in a directory.
     *
     * @param directoryParam the directory to list files from
     * @return a list of file information objects
     */
    @Override
    public List<FileInfo> listFiles(final String directoryParam) {
        List<File> files = fileRepository.listFiles(directoryParam);
        return files.stream().map(file -> {
            FileInfo fileInfo = toFileInfo(file);
            return fileInfo;
        }).collect(Collectors.toList());
    }

    /**
     * Converts a File object to a FileInfo object.
     *
     * @param file the file to convert
     * @return the corresponding FileInfo object
     */
    private FileInfo toFileInfo(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(file.getName());
        fileInfo.setPath(file.getPath());
        fileInfo.setSize(file.length());
        fileInfo.setLastModified(file.lastModified());
        fileInfo.setDirectory(file.isDirectory());
        fileInfo.setFile(file.isFile());
        fileInfo.setHidden(file.isHidden());
        return fileInfo;
    }
}
