package owpk.loader;

import owpk.exception.ApplicationError;

public class SimpleLoader implements Loader {

    @Override
    public ClassMeta load(String className) throws ApplicationError {
        try {
            return new ClassMeta(className, Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new ApplicationError(e.getLocalizedMessage());
        }
    }
}
