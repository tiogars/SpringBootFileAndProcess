package fr.tiogars.springbootfileandprocess.services;

public class FileInfo {

    /**
     * The name of the file or directory.
     */
    private String name;

    /**
     * The size of the file in bytes.
     */
    private long size;

    /**
     * The last modified timestamp of the file in milliseconds.
     */
    private long lastModified;

    /**
     * Indicates whether the file is a directory.
     */
    private boolean directory;

    /**
     * Indicates whether the file is a regular file.
     */
    private boolean file;

    /**
     * Indicates whether the file is hidden.
     */
    private boolean hidden;

    /**
     * Default constructor.
     */
    public FileInfo() {
        // Default constructor
    }

    /**
     * Gets the name of the file or directory.
     *
     * @return the name of the file or directory
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the file or directory.
     *
     * @param nameParam the name to set
     */
    public void setName(final String nameParam) {
        this.name = nameParam;
    }

    /**
     * Gets the size of the file in bytes.
     *
     * @return the size of the file in bytes
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of the file in bytes.
     *
     * @param sizeParam the size to set
     */
    public void setSize(final long sizeParam) {
        this.size = sizeParam;
    }

    /**
     * Gets the last modified timestamp of the file in milliseconds.
     *
     * @return the last modified timestamp of the file in milliseconds
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Sets the last modified timestamp of the file in milliseconds.
     *
     * @param lastModifiedParam the last modified timestamp to set
     */
    public void setLastModified(final long lastModifiedParam) {
        this.lastModified = lastModifiedParam;
    }

    /**
     * Gets the directory status of the file.
     *
     * @return true if the file is a directory, false otherwise
     */
    public boolean isDirectory() {
        return directory;
    }

    /**
     * Sets the directory status of the file.
     *
     * @param directoryParam true if the file is a directory, false otherwise
     */
    public void setDirectory(final boolean directoryParam) {
        this.directory = directoryParam;
    }

    /**
     * Gets the file status of the file.
     *
     * @return true if the file is a regular file, false otherwise
     */
    public boolean isFile() {
        return file;
    }

    /**
     * Sets the file status of the file.
     *
     * @param fileParam true if the file is a regular file, false otherwise
     */
    public void setFile(final boolean fileParam) {
        this.file = fileParam;
    }

    /**
     * Gets the hidden status of the file.
     *
     * @return true if the file is hidden, false otherwise
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets the hidden status of the file.
     *
     * @param hiddenParam true if the file is hidden, false otherwise
     */
    public void setHidden(final boolean hiddenParam) {
        this.hidden = hiddenParam;
    }
}
