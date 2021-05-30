package owpk.loader;

import owpk.exception.ApplicationError;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

public class CustomURLClassLoader implements Loader {
    private static final String DEFAULT_PROTOCOL = "file://";
    private URLClassLoader loader;
    private String protocol;

    public CustomURLClassLoader(URLClassLoader loader) {
        protocol = DEFAULT_PROTOCOL;
        this.loader = loader;
    }

    public CustomURLClassLoader(String path) throws ApplicationError {
        this(path, DEFAULT_PROTOCOL);
    }

    public CustomURLClassLoader(Set<String> paths) throws ApplicationError {
        this(paths, DEFAULT_PROTOCOL);
    }

    public CustomURLClassLoader(String path, String protocol) throws ApplicationError {
        this.protocol = protocol;
        loader = new URLClassLoader(createURLFromPath(path));
    }

    public CustomURLClassLoader(Set<String> paths, String protocol) throws ApplicationError {
        this.protocol = protocol;
        loader = new URLClassLoader(createURLFromPaths(paths));
    }

    public ClassMeta load(String className) throws Throwable {
        return new ClassMeta(className, loader.loadClass(className));
    }

    private URL[] createURLFromPath(String path) throws ApplicationError {
        try {
            path = protocol + path;
            return new URL[]{new URL(path)};
        } catch (MalformedURLException e) {
            throw new ApplicationError(e.getLocalizedMessage());
        }
    }

    private URL[] createURLFromPaths(Set<String> paths) throws ApplicationError {
        URL[] urls = new URL[paths.size()];
        int ind = 0;
        for (String path : paths) {
            try {
                urls[ind++] = new URL(protocol + path);
            } catch (MalformedURLException e) {
                throw new ApplicationError(e.getLocalizedMessage());
            }
        }
        return urls;
    }
}
