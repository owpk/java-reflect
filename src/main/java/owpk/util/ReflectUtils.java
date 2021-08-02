package owpk.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectUtils {

    public static Optional<String> getClassName(Class<?> clazz) {
        return catchException(Class::getTypeName, clazz);
    }

    public static Optional<String> getSimpleClassName(Class<?> clazz) {
        if (clazz != null && clazz.equals(Object.class)) {
            var type = ((ParameterizedType) clazz.getGenericSuperclass());
            if (type != null) {
                return getTypeLetter(clazz);
            }
        }
        return catchException(Class::getSimpleName, clazz);
    }

    public static Optional<String> getTypeLetter(ParameterizedType type) {
        return catchException(x -> (x.getActualTypeArguments()[0]).getTypeName(), type);
    }

    public static Optional<String> getTypeLetter(Class<?> clazz) {
        return catchException(x -> (((ParameterizedType) x.getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName(), clazz);
    }

    public static Optional<List<String>> getClassGenerics(Class<?> clazz) {
        var opt = catchException(Class::getTypeParameters, clazz);
        return opt.map(typeVariables -> Arrays.stream(typeVariables)
                .map(TypeVariable::getName)
                .collect(Collectors.toList()));
    }

    public static Optional<List<String>> getMethodsNames(Class<?> clazz) {
        var methods = catchException(Class::getMethods, clazz);
        return mapAndCollect(Method::getName, methods);
    }

    public static Optional<List<Method>> getMethods(Class<?> clazz) {
        var methods = catchException(Class::getMethods, clazz);
        return methods.map(value -> Arrays.stream(value).collect(Collectors.toList()));
    }

    public static Optional<List<String>> getClassAnnotations(Class<?> clazz) {
        var annotations = catchException(Class::getAnnotations, clazz);
        return mapAndCollect(Annotation::toString, annotations);
    }

    public static Optional<List<Class<?>>> getAnnotations(Class<?> clazz) {
        var annotations = catchException(Class::getAnnotations, clazz);
        return mapAndCollect(Annotation::annotationType, annotations);
    }

    public static Optional<List<String>> getMethodAnnotationsFullInfo(Method method) {
        var annotations = catchException(Method::getDeclaredAnnotations, method);
        return mapAndCollect(Annotation::toString, annotations);
    }

    public static Optional<List<Class<?>>> getMethodAnnotations(Method method) {
        var annotations = catchException(Method::getDeclaredAnnotations, method);
        return mapAndCollect(Annotation::annotationType, annotations);
    }

    public static Optional<Integer> getMethodModType(Method method) {
        return catchException(Method::getModifiers, method);
    }

    public static Optional<List<String>> getMethodArgsFullInfo(Method method) {
        var params = catchException(Method::getParameters, method);
        return mapAndCollect(Parameter::toString, params);
    }

    public static Optional<List<Parameter>> getMethodParameters(Method method) {
        var params = catchException(Method::getParameters, method);
        return mapAndCollect(x -> x, params);
    }

    public static Optional<List<Class<?>>> getMethodArgs(Method method) {
        var params = catchException(Method::getParameters, method);
        return mapAndCollect(Parameter::getType, params);
    }

    public static Optional<Class<?>> getMethodReturnType(Method method) {
        var opt = catchException(Method::getReturnType, method);
        if (opt.isPresent())
            return Optional.of(opt.get());
        return Optional.empty();
    }

    public static Optional<Class<?>> getSuperType(Class<?> clazz) {
        var superClass = catchException(Class::getSuperclass, clazz);
        if (superClass.isPresent()) {
            var cl = superClass.get();
            if (!cl.equals(Object.class))
                return Optional.of(cl);
        }
        return Optional.empty();
    }

    public static Optional<List<String>> getInterfacesNames(Class<?> clazz) {
        var opt = catchException(Class::getInterfaces, clazz);
        return mapAndCollect(Class::getName, opt);
    }

    public static Optional<List<Class<?>>> getInterfaces(Class<?> clazz) {
        var opt = catchException(Class::getInterfaces, clazz);
        return mapAndCollect(x -> x, opt);
    }

    @SuppressWarnings("all")
    private static <T, R> Optional<List<R>> mapAndCollect(
            Function<T, R> function, Optional<T[]> arr) {
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
