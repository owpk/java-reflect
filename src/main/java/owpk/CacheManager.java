package owpk;

import owpk.exception.ResourceException;
import owpk.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CacheManager {
    private static CacheManager cacheManager;
    private Set<String> cache;
    private Path toCache;

    public static CacheManager getInstance() throws ResourceException {
        if (cacheManager == null) {
            cacheManager = new CacheManager(new ResourceHolder());
        }
        return cacheManager;
    }

    private CacheManager(ResourceHolder resourceHolder) throws ResourceException {
        cache = new HashSet<>();
        cache.addAll(FileUtils.readAllLines(resourceHolder.getCacheFilePath()));
        toCache = resourceHolder.getCacheFilePath();
    }

    public void validate() {
        cache.removeIf(cachedLine ->
                !Files.exists(Paths.get(cachedLine)) ||
                        !cachedLine.endsWith(".jar"));
    }

    public void add(String path) {
        cache.add(path);
    }

    public void clear() {
        cache.clear();
    }

    public Set<String> getCache() {
        return cache;
    }

    public void validateAndSave() throws ResourceException {
        validate();
        FileUtils.write(toCache, cache);
    }
}
