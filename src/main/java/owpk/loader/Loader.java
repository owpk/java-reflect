package owpk.loader;

public interface Loader {
     ClassMeta load(String className) throws Throwable;
}
