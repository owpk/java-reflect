package owpk;

import owpk.dto.ClassInfo;
import owpk.dto.MethodInfo;
import owpk.exception.ApplicationError;
import owpk.exception.ResourceException;
import owpk.loader.ClassMeta;
import owpk.util.ClassPathScannerUtils;
import owpk.util.Color;
import owpk.util.PrettyConsole;
import owpk.visitor.BaseVisitor;
import picocli.CommandLine;

import java.util.Map;
import java.util.TreeMap;

import static owpk.util.ClassPathScannerUtils.*;

@CommandLine.Command(subcommands = { CacheCommand.class, Reflect.List.class
}, mixinStandardHelpOptions = true, version = "reflect 1.0",
        description = "prints information about class or jar library (e.g. methods signature, class names etc) to STDOUT" +
                "\n\tjava 11+ version required for usage")
public class Reflect implements Runnable {

    public static void main(String[] args) {
        new CommandLine(Reflect.class).execute(args);
    }

    @CommandLine.Parameters(defaultValue = "")
    private String className;

    private ResourceHolder resourceHolder;
    private PrettyConsole prettyConsole;

    public Reflect() throws ResourceException {
        resourceHolder = new ResourceHolder();
        prettyConsole = new PrettyConsole();
    }

    @CommandLine.Command(name = "jar", description = "\tprints information about specific jar library")
    public void printJarClasses(@CommandLine.Parameters String path) throws ResourceException, ApplicationError {
        var res = scanJar(path, CacheManager.getInstance());
        var keys = res.keySet();
        for (String key : keys) {
            System.out.println(key);
        }
    }

    @CommandLine.Command(name = "list", description = "\tprints list of classes (without any options - only default jvm classes)")
    public static class List implements Runnable {

        @CommandLine.Option(names = {"-a", "--all"},
                description = "\tprints all available classes")
        private boolean all;

        @CommandLine.Option(names = {"-j", "--jar"},
                description = "\tprints all classes in a specific jar file")
        private String jarPath;

        @Override
        public void run() {
            try {
                var jrt = defaultScan(getJrtPath(),
                        ClassPathScannerUtils.getJrtVisitor());
                System.out.println(new PrettyConsole().colorizeString("JRT - STANDARD LIBRARIES:", Color.GREEN));
                jrt.forEach(System.out::println);
                if (all) {
                    printCachedClasses();
                }
                if (jarPath != null && !jarPath.isBlank()) {
                    printCachedClasses();
                }
            } catch (ApplicationError | ResourceException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        private void printJarClasses(String jar) throws ResourceException, ApplicationError {
            var jars = defaultScan(getJarPath(jar), new BaseVisitor());
            var cacheMgr = CacheManager.getInstance();
            cacheMgr.add(jar);
            cacheMgr.validateCache();
            cacheMgr.saveCache();
            jars.forEach(System.out::println);
        }

        private void printCachedClasses() throws ResourceException, ApplicationError {
            var ansi = new PrettyConsole();
            System.out.println("CACHED JAR LIBRARIES: ");
            var cache = CacheManager.getInstance().getCache();
            for (String c : cache) {
                System.out.println(ansi.colorizeString(c, Color.GREEN));
                var cachedClasses = defaultScan(getJarPath(c), new BaseVisitor());
                cachedClasses.forEach(System.out::println);
                System.out.println("---------------------------");
            }
        }
    }

    @Override
    public void run() {
        if (className != null && !className.isBlank()) {
            try {
                var jrtClasses= ClassPathScannerUtils.scanJrt();
                var cachedClasses = ClassPathScannerUtils.scanCachedJars(
                        CacheManager.getInstance());
                var treeMap = new TreeMap<>(jrtClasses);
                treeMap.putAll(cachedClasses);
                Map.Entry<String, ClassMeta> entry = treeMap.entrySet().stream()
                        .filter(x -> x.getKey().endsWith(className))
                        .findFirst()
                        .orElseThrow(() -> new ApplicationError("class " + className + " not found"));
                var meta = entry.getValue().getLoadClass();
                var classInfo = new ClassInfo(meta);
                var ansi = new PrettyConsole();
                System.out.println(ansi.formatClass(classInfo));
                var list = classInfo.getMethodInfo();
                for (MethodInfo methodInfo : list) {
                    System.out.println(ansi.formatMethod(methodInfo));
                }
            } catch (ApplicationError | ResourceException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }
}
