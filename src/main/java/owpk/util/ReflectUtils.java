package owpk.util;

import owpk.exception.ApplicationError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectUtils {

    public static String getClassName(Class<?> clazz) throws ApplicationError {
        return catchException(Class::getName, clazz);
    }

    public static List<String> getClassGenerics(Class<?> clazz) throws ApplicationError {
        return mapToStringAndCollect(Type::getTypeName, clazz.getTypeParameters());
    }

    public static List<String> getMethodsNames(Class<?> clazz) throws ApplicationError {
        Method[] methods = catchException(Class::getMethods, clazz);
        return mapToStringAndCollect(Method::getName, methods);
    }

    public static List<Method> getMethods(Class<?> clazz) throws ApplicationError {
        Method[] methods = catchException(Class::getMethods, clazz);
        return Arrays.stream(methods).collect(Collectors.toList());
    }

    public static List<String> getClassAnnotations(Class<?> clazz) throws ApplicationError {
        Annotation[] annotations = catchException(Class::getAnnotations, clazz);
        return mapToStringAndCollect(Annotation::toString, annotations);
    }

    public static List<String> getMethodAnnotationsFullInfo(Method method) throws ApplicationError {
        Annotation[] annotations = catchException(Method::getDeclaredAnnotations, method);
        return mapToStringAndCollect(Annotation::toString, annotations);
    }

    public static String getMethodModType(Method method) throws ApplicationError {
        int mod = catchException(Method::getModifiers, method);
        return Modifier.toString(mod);
    }

    public static List<String> getMethodArgsFullInfo(Method method) throws ApplicationError {
        var params = catchException(Method::getParameters, method);
        return mapToStringAndCollect(Parameter::toString, params);
    }

    public static List<String> getSimplifiedMethodArgsInfo(Method method) throws ApplicationError {
        var params = getMethodArgsFullInfo(method);
        return params.stream()
                .map(x -> {
                    if (!x.isBlank()) {
                        if (x.contains("."))
                            x = x.substring(x.lastIndexOf(".") + 1);
                        return x.substring(0, x.lastIndexOf(" "));
                    } return x;
                })
                .collect(Collectors.toList());
    }

    public static String getSuperType(Class<?> clazz) throws ApplicationError {
        Class<?> superClass = catchException(Class::getSuperclass, clazz);
        if (superClass != null)
            if (!superClass.equals(Object.class))
                return getClassName(superClass);
        return "";
    }

    public static List<String> getInterfaces(Class<?> clazz) throws ApplicationError {
        Class<?>[] interfaces = catchException(Class::getInterfaces, clazz);
        return mapToStringAndCollect(Class::getName, interfaces);
    }

    private static <T> List<String> mapToStringAndCollect(Function<T, String> function, T[] arr) {
        return Arrays.stream(arr).map(function).collect(Collectors.toList());
    }

    private static <T, R> R catchException(Function<T, R> function, T t) throws ApplicationError {
        try {
            return function.apply(t);
        } catch (Throwable e) {
            throw new ApplicationError(e.getLocalizedMessage());
        }
    }
}
