package owpk;

import owpk.exception.ResourceException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "cache", aliases = "c", mixinStandardHelpOptions = true,
        description = "\tmanage current cache data")
public class CacheCommand {

    private CacheManager cacheManager;

    public CacheCommand() throws ResourceException {
        cacheManager = CacheManager.getInstance();
    }

    @Command(name = "add",
            description = "\tadd specific jar path to cache")
    public void add(@Parameters String pathToLib) throws ResourceException {
        cacheManager.add(pathToLib);
        cacheManager.validateAndSave();
    }

    @Command(name = "list", aliases = {"ls"},
            description = "\tprint cache list")
    public void print() throws ResourceException {
        for (String s : cacheManager.getCache()) {
            System.out.println(s);
        }
    }

    @Command(name = "clear", aliases = {"cl"},
            description = "\tclear cache")
    public void clear() throws ResourceException {
        cacheManager.clear();
        cacheManager.validateAndSave();
    }
}
