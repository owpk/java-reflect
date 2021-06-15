package owpk.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectUtils {

    public static Optional<String> getClassName(Class<?> clazz) {
        Optional<String> name = catchException(Class::getName, clazz);
        if (name.isPresent()) {
            String presented = name.get();
            if (presented.endsWith(";"))
               presented = presented.substring(0, presented.length() - 1);
            if (presented.startsWith("[L"))
               presented = presented.substring(2) + "[]";
            return Optional.of(presented);
        } else return Optional.empty();
    }

    public static Optional<List<String>> getClassGenerics(Class<?> clazz) {
        var opt = catchException(Class::getTypeParameters, clazz);
        return opt.map(typeVariables -> Arrays.stream(typeVariables)
                .map(TypeVariable::getName)
                .collect(Collectors.toList()));
    }

    public static Optional<List<String>> getMethodsNames(Class<?> clazz) {
        var methods = catchException(Class::getMethods, clazz);
        return mapToStringAndCollect(Method::getName, methods);
    }

    public static Optional<List<Method>> getMethods(Class<?> clazz) {
        var methods = catchException(Class::getMethods, clazz);
        return methods.map(value -> Arrays.stream(value).collect(Collectors.toList()));
    }

    public static Optional<List<String>> getClassAnnotations(Class<?> clazz) {
        var annotations = catchException(Class::getAnnotations, clazz);
        return mapToStringAndCollect(Annotation::toString, annotations);
    }

    public static Optional<List<String>> getMethodAnnotationsFullInfo(Method method) {
        var annotations = catchException(Method::getDeclaredAnnotations, method);
        return mapToStringAndCollect(Annotation::toString, annotations);
    }

    public static Optional<Integer> getMethodModType(Method method) {
        return catchException(Method::getModifiers, method);
    }

    public static Optional<List<String>> getMethodArgsFullInfo(Method method) {
        var params = catchException(Method::getParameters, method);
        return mapToStringAndCollect(Parameter::toString, params);
    }

    public static Optional<String> getMethodReturnType(Method method) {
       var opt = catchException(Method::getReturnType, method);
       if (opt.isPresent()) {
          return getClassName(opt.get());
       }
       return Optional.empty();
    }

    public static Optional<String> getSuperType(Class<?> clazz) {
        var superClass = catchException(Class::getSuperclass, clazz);
        if (superClass.isPresent()) {
            var cl = superClass.get();
            if (!cl.equals(Object.class))
                return getClassName(cl);
        }
        return Optional.empty();
    }

    public static Optional<List<String>> getInterfaces(Class<?> clazz) {
        var opt = catchException(Class::getInterfaces, clazz);
        return mapToStringAndCollect(Class::getName, opt);
    }


    @SuppressWarnings("all")
    private static <T> Optional<List<String>> mapToStringAndCollect(
            Function<T, String> function, Optional<T[]> arr) {
        return arr.map(ts -> Arrays.stream(ts).map(function)
                .collect(Collectors.toList()))
                .or(Optional::empty);
    }

    private static <T, R> Optional<R> catchException(Function<T, R> function, T t) {
        try {
            return Optional.of(function.apply(t));
        } catch (Throwable e) {
            return Optional.empty();
        }
    }
}
