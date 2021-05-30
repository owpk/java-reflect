package owpk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import owpk.exception.ApplicationError;
import owpk.util.ReflectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class MethodInfo {
    private String name;
    private String modifier;
    private List<String> annotations;
    private List<String> methodArgs;

    public MethodInfo() {
        name = "";
        modifier = "";
        annotations = new ArrayList<>();
        methodArgs = new ArrayList<>();
    }

    public MethodInfo(String name, String modifier) {
        this();
        this.name = name;
        this.modifier = modifier;
    }

    public MethodInfo(Method method) throws ApplicationError {
        name = method.getName();
        modifier = ReflectUtils.getMethodModType(method);
        annotations = ReflectUtils.getMethodAnnotationsFullInfo(method);
        methodArgs = ReflectUtils.getSimplifiedMethodArgsInfo(method);
    }
}
