package owpk;

import owpk.exception.ResourceException;

import java.io.IOException;
import java.nio.file.*;

public class ResourceHolder {

    public static final String USER_HOME = System.getProperty("user.home");
    public static final String DOT_FILE = USER_HOME + "/.reflect";
    private final Path dotFileDir;
    private final Path cacheFilePath;

    public ResourceHolder() throws ResourceException {
        dotFileDir = checkIfExistAndCreate(DOT_FILE, true);
        cacheFilePath = checkIfExistAndCreate("/cache", false);
    }

    public Path checkIfExistAndCreate(String path, boolean dir) throws ResourceException {
        try {
            Path p = Paths.get(DOT_FILE + path);
            if (!Files.exists(p)) {
                if (dir)
                    Files.createDirectories(p);
                else Files.createFile(p);
            }
            return p;
        } catch (IOException e) {
            throw new ResourceException("Cannot create file/directory with path: " + path);
        }
    }

    public Path getDotFileDir() {
        return dotFileDir;
    }

    public Path getCacheFilePath() {
        return cacheFilePath;
    }

}
