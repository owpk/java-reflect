import java.lang.reflect.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.net.URI;
import java.util.function.Consumer;

public class Reflect {
   private static Map<String, MetaInfo> knownJvmClasses;
   static {
      try {
         knownJvmClasses = new LinkedHashMap<>();
         FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));
         Path modules = fs.getPath("modules", "java.base", "java/");
         List<Path> list = Files.walk(modules).collect(Collectors.toList());
         class Visitor extends SimpleFileVisitor<Path> {
            @Override
            public FileVisitResult visitFile(Path p, BasicFileAttributes attr) {
               String path = p.toString();
               String classPath = path.substring("modules/java.base/".length(), 
                     path.lastIndexOf(".")).replaceAll("/", "\\.");
               if (!classPath.contains("$")) {
                  try {
                  Class clazz = allocateClass(classPath, null);
                  String className = classPath.substring(classPath.lastIndexOf(".") + 1);
                  knownJvmClasses.put(className, new MetaInfo(classPath, clazz));      
                  } catch (Exception e) {
                     // ignore
                  }
               }
               return FileVisitResult.CONTINUE;
            }
         }
         Files.walkFileTree(modules, new Visitor());
      } catch(Exception e) {
         // ignore
      }
   }

   private enum Color {
      GREEN(46), BLUE(45), YELLOW(179), RED(124);

      private int ind;

      Color(int ind) {
         this.ind = ind;
      }

      public int getInd() {
         return ind;
      }
   }

   private static class MetaInfo {
      String classPath;
      Class clazz;

      public MetaInfo(String classPath, Class clazz) {
         this.classPath = classPath;
         this.clazz = clazz;
      }
   }
   
   public static void main(String... args) {
      try {
         if (args[0].equals("-a")) {
            knownJvmClasses.entrySet().forEach(x -> System.out.println(x.getValue().classPath));
            return;
         }
         String className = args[0];
         Command cmd = new SpecificClassMethodsInfoCommand(className);
         cmd.execute();
      } catch(Exception e) {
         //ignore
      }
   }

   private interface Command {
      void execute() throws Exception;
   }

   private static class SpecificClassMethodsInfoCommand implements Command {
      private String className;

      public SpecificClassMethodsInfoCommand(String className) {
         this.className = className;
      }

      public void execute() throws Exception {
         MetaInfo meta = knownJvmClasses.get(className);
         if (meta == null) {
            Class clazz = allocateClass(className, 
                  x -> System.out.println(
                     colorize(Color.RED, String.format("Class \"%s\" not found!", className))));
            meta = new MetaInfo(className, clazz);
         } 
         Class known = meta.clazz;
         Method[] methods = known.getMethods();
         System.out.println(colorize(Color.RED, meta.classPath));
         printMethods(methods);
      }

      public void printMethods(Method... methods) {
         for(Method m: methods) {
            Class<?>[] argsType = m.getParameterTypes();
            String methodArgs = Arrays.stream(argsType)
               .map(x -> colorize(Color.YELLOW, x.getSimpleName())).collect(Collectors.joining(", "));
            String returnType = colorize(Color.BLUE, m.getReturnType().getSimpleName());
            System.out.printf("%s %s(%s)%n",returnType, m.getName(), methodArgs);
         }
      }
   }

   private static String colorize(Color color, String arg) {
      return String.format("\033[38;5;%dm%s\033[0m", color.getInd(), arg);
   }

   private static Class allocateClass(String name, Consumer<String> consumer) throws Exception {
      Class c = null;
      try {
         c = Class.forName(name);
      } catch (ClassNotFoundException e) {
         if (consumer != null)
            consumer.accept(name);
         throw new Exception();
      }
      return c;
   }
}
