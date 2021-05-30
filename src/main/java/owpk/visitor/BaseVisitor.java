package owpk.visitor;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class BaseVisitor extends SimpleFileVisitor<Path> implements Visitor {
    protected List<String> classNames;

    public BaseVisitor(List<String> classNames) {
        this.classNames = classNames;
    }

    public BaseVisitor() {
        classNames = new LinkedList<>();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        visitClassFile(file, substringClassPath());
        return FileVisitResult.CONTINUE;
    }

    protected void visitClassFile(Path p, Function<String, String> function) {
        String path = p.toString();
        if (path.endsWith(".class") && !path.contains("$")) {
            String classPath = function.apply(path);
            classNames.add(classPath);
        }
    }

    protected Function<String, String> substringClassPath() {
        return x -> {
            String name = x.substring(1, x.lastIndexOf("."));
            return name.replaceAll("/", "\\.");
        };
    }

    @Override
    public List<String> getClassNames() {
        return classNames;
    }
}
