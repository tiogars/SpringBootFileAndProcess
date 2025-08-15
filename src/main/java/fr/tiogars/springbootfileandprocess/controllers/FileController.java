package fr.tiogars.springbootfileandprocess.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.springbootfileandprocess.services.FileInfo;
import fr.tiogars.springbootfileandprocess.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for managing files.
 */
@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/file")
@Tag(name = "File Management", description = "Operations related to file management")
public class FileController {

    /**
     * The file service for managing files.
     */
    private FileService fileService;

    /**
     * Constructor for FileController.
     *
     * @param fileServiceParam the file service to use
     */
    public FileController(final FileService fileServiceParam) {
        this.fileService = fileServiceParam;
    }

    /**
     * Lists files in a directory.
     *
     * @param directoryParam the directory to list files from
     * @return a list of file information objects
     */
    @Operation(summary = "List files in a directory", description = "Retrieves a list of files in the specified directory")
    @GetMapping("/list")
    public List<FileInfo> listFiles(final String directoryParam) {
        return fileService.listFiles(directoryParam);
    }
}
