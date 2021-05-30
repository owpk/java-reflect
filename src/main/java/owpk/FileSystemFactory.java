package owpk;

import owpk.exception.ApplicationError;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

public class FileSystemFactory {
    private static final Map<String, FileSystem> fsStorage = new HashMap<>();
    static {
        fsStorage.put("jrt:/", FileSystems.getFileSystem(URI.create("jrt:/")));
    }

    private FileSystemFactory() {
    }

    public static FileSystem getFileSystem(String name) throws ApplicationError {
        try {
            var fs = fsStorage.get(name);
            if (fs == null) {
                fs = FileSystems.newFileSystem(URI.create(name), new HashMap<>());
                fsStorage.put(name, fs);
            }
            return fs;
        } catch (IOException e) {
            throw new ApplicationError(e.getLocalizedMessage());
        }
    }
}
