package owpk.visitor;

import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.util.List;

public interface Visitor extends FileVisitor<Path> {
    List<String> getClassNames();
}
