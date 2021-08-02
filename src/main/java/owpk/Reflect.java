package owpk;

import owpk.exception.ApplicationError;
import owpk.exception.ResourceException;
import owpk.loader.ClassMeta;
import owpk.util.ClassPathScannerUtils;
import owpk.util.Color;
import owpk.util.PrettyConsole;
import owpk.util.ReflectUtils;
import owpk.visitor.BaseVisitor;
import picocli.CommandLine;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static owpk.util.ClassPathScannerUtils.*;

@CommandLine.Command(subcommands = {CacheCommand.class, Reflect.List.class
}, mixinStandardHelpOptions = true, version = "reflect 1.0",
        description = "prints information about class or jar library (e.g. methods signature, class names etc) to STDOUT" +
                "\n\tjava 11+ version required for usage")
public class Reflect implements Runnable {

    @CommandLine.Option(names = {"-v", "--verbose"})
    private boolean verboseName;

    public static void main(String[] args) throws ResourceException {
        CacheManager.getInstance().validateAndSave();
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

    @CommandLine.Command(name = "list", aliases = "ls",
            description = "\tprints list of classes (without any options - only default jvm classes)")
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
            cacheMgr.validateAndSave();
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
            }
        }
    }

    @Override
    public void run() {
        if (className != null && !className.isBlank()) {
            try {
                var jrtClasses = ClassPathScannerUtils.scanJrt();
                var cachedClasses = ClassPathScannerUtils.scanCachedJars(
                        CacheManager.getInstance());
                var treeMap = new TreeMap<>(jrtClasses);
                treeMap.putAll(cachedClasses);
                Map<String, ClassMeta> entry = treeMap.entrySet().stream()
                        .filter(x -> matches(className, x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                printClasses(entry, verboseName);
            } catch (ApplicationError | ResourceException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    private static boolean matches(String input, String target) {
        if (input != null && !input.isBlank() && target != null && !target.isBlank()) {
            input = input.toLowerCase(Locale.ROOT);
            target = target.toLowerCase(Locale.ROOT);
            if (input.charAt(0) == '%') {
                input = input.substring(1);
                return target.endsWith(input);
            }
            if (input.charAt(input.length() - 1) == '%') {
                input = input.substring(0, input.length() - 1);
                if (!input.contains(".") && target.contains("."))
                    target = target.substring(target.lastIndexOf(".") + 1);
                return target.startsWith(input);
            }
            if (input.contains(".")) {
                return input.equals(target);
            } else {
                if (target.contains(".")) {
                    target = target.substring(target.lastIndexOf(".") + 1);
                }
                return target.equals(input);
            }
        }
        return false;
    }

    private static void printClasses(Map<String, ClassMeta> map, boolean verbose) {
        var ansi = new PrettyConsole();
        if (map.size() > 0) {
            var print = map.values().stream().map(classMeta -> {
                Class<?> cl = classMeta.getLoadClass();
                var result = formatClass(ansi, cl, verbose);

                var list = ReflectUtils.getMethods(cl).orElse(new ArrayList<>());
                list.sort(Comparator.comparing(Method::getName));

                for (Method method : list) {
                    result += formatMethod(ansi, method, verbose);
                }
                return result;
            }).collect(Collectors.joining("-----------------\n"));
            System.out.println(print);
        } else System.out.println("empty result");
    }

    public static String formatClass(PrettyConsole ansi, Class<?> cl, boolean verbose) {
        var result = "";
        var name = verbose ? ReflectUtils.getClassName(cl).orElse("") :
                ReflectUtils.getSimpleClassName(cl).orElse("");
        var superCl = ReflectUtils.getSuperType(cl).orElse(null);
        var superClass = verbose ? ReflectUtils.getClassName(superCl).orElse("") :
                ReflectUtils.getSimpleClassName(superCl).orElse("");
        var interfaces = verbose ? ReflectUtils.getInterfacesNames(cl).orElse(Collections.emptyList()) :
                ReflectUtils.getInterfaces(cl).orElse(Collections.emptyList())
                        .stream().map(x -> ReflectUtils.getSimpleClassName(x).orElse(""))
                        .collect(Collectors.toList());
        var annotations = verbose ? ReflectUtils.getClassAnnotations(cl).orElse(Collections.emptyList()) :
                ReflectUtils.getAnnotations(cl).orElse(Collections.emptyList())
                        .stream().map(x -> "@" + ReflectUtils.getSimpleClassName(x).orElse(""))
                        .collect(Collectors.toList());
        var genericsType = ReflectUtils.getClassGenerics(cl).orElse(Collections.emptyList());

        result += ansi.formatClass(annotations, name, genericsType, superClass, interfaces) + "\n";
        return result;
    }

    public static String formatMethod(PrettyConsole ansi, Method method, boolean verbose) {
        var result = "";
        var mName = method.getName();
        var opt = ReflectUtils.getMethodModType(method);
        var modifier = "";
        if (opt.isPresent())
            modifier = Modifier.toString(opt.get());
        var rClass = ReflectUtils.getMethodReturnType(method).get();
        var returnType = verbose ? ReflectUtils.getClassName(rClass).orElse("") :
                ReflectUtils.getSimpleClassName(rClass).orElse("");
        var mAnnotations = verbose ? ReflectUtils.getMethodAnnotationsFullInfo(method).orElse(Collections.emptyList()) :
                ReflectUtils.getMethodAnnotations(method).orElse(Collections.emptyList())
                        .stream().map(x -> "@" + ReflectUtils.getSimpleClassName(x).orElse("")).collect(Collectors.toList());
        var methodArgs = ReflectUtils.getMethodParameters(method).orElse(Collections.emptyList());
        var methodArgsConverted = methodArgs.stream().map(x -> {
            var arg = x.toString();
            var type = arg.substring(0, arg.indexOf(' '));
            if (!verbose) {
                if (type.contains("."))
                    type = type.substring(type.lastIndexOf(".") + 1);
            }
            return type;
        }).collect(Collectors.toList());
        result += ansi.formatMethod(mAnnotations, modifier, returnType, methodArgsConverted, mName) + "\n";
        return result;
    }
}
