package owpk.util;

import owpk.CacheManager;
import owpk.FileSystemFactory;
import owpk.exception.ApplicationError;
import owpk.exception.ResourceException;
import owpk.loader.ClassMeta;
import owpk.loader.CustomURLClassLoader;
import owpk.loader.Loader;
import owpk.loader.SimpleLoader;
import owpk.visitor.BaseVisitor;
import owpk.visitor.Visitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ClassPathScannerUtils {

    public static Map<String, ClassMeta> scanCachedJars(CacheManager cacheManager) throws ApplicationError, ResourceException {
        var cache = cacheManager.getCache();
        var allCached = new LinkedHashMap<String, ClassMeta>();
        for (String s : cache) {
            System.out.println("::Searching in cached jars: " + s);
            var cached = scanJar(s, cacheManager);
            allCached.putAll(cached);
        }
        return allCached;
    }

    public static Map<String, ClassMeta> scanJrt() throws ApplicationError {
        var path = getJrtPath();
        return defaultScan(new SimpleLoader(), path, getJrtVisitor());
    }

    public static BaseVisitor getJrtVisitor() {
        return new BaseVisitor() {
            @Override
            protected Function<String, String> substringClassPath() {
                return x -> x.substring("modules/java.base/".length(),
                        x.lastIndexOf(".")).replaceAll("/", "\\.");
            }
        };
    }

    public static Map<String, ClassMeta> scanJar(String jar, CacheManager cacheManager) throws ApplicationError, ResourceException {
        cacheManager.add(jar);
        try {
            cacheManager.validateAndSave();
            var path = getJarPath(jar);
            return defaultScan(new CustomURLClassLoader(jar),
                    path, new BaseVisitor());
        } catch (ResourceException e) {
            throw new ResourceException(e.getLocalizedMessage());
        }
    }

    public static Path getJarPath(String pathToJar) throws ApplicationError {
        var path = Paths.get(pathToJar);
        var fs = FileSystemFactory.getFileSystem("jar:file:" + path.toUri().getPath());
        return fs.getPath("/");
    }

    public static Path getJrtPath() throws ApplicationError {
        var fs = FileSystemFactory.getFileSystem("jrt:/");
        return fs.getPath("modules", "java.base", "java/");
    }

    public static Set<String> defaultScan(Path path, Visitor visitor) throws ApplicationError {
        try {
            Files.walkFileTree(path, visitor);
            return new LinkedHashSet<>(visitor.getClassNames());
        } catch (IOException e) {
            throw new ApplicationError(e.getLocalizedMessage());
        }
    }

    public static Map<String, ClassMeta> defaultScan(Loader loader,
                                              Path path,
                                              Visitor visitor) throws ApplicationError {
        try {
            Files.walkFileTree(path, visitor);
            var result = visitor.getClassNames();
            var map = new LinkedHashMap<String, ClassMeta>();
            for (String s : result) {
                try {
                    map.put(s, loader.load(s));
                } catch (Throwable e) {
                    // ignore
                }
            }
            return map;
        } catch (IOException e) {
            throw new ApplicationError(e.getLocalizedMessage());
        }
    }
}
