package owpk.loader;

import lombok.Getter;

@Getter
public class ClassMeta {
    private String className;
    private Class<?> loadClass;

    public ClassMeta(String className, Class<?> loadClass) {
        this.className = className;
        this.loadClass = loadClass;
    }
}
