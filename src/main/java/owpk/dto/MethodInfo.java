package owpk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import owpk.exception.ApplicationError;
import owpk.util.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Data
public class MethodInfo {
    private String name;
    private String modifier;
    private String returnType;
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

    public MethodInfo(Method method) {
        name = method.getName();
        var opt = ReflectUtils.getMethodModType(method);
        if (opt.isPresent()) {
            modifier = Modifier.toString(opt.get());
        } else modifier = "";
        returnType = ReflectUtils.getMethodReturnType(method).orElse("");
        annotations = ReflectUtils.getMethodAnnotationsFullInfo(method)
                .orElse(Collections.emptyList());
        methodArgs = ReflectUtils.getMethodArgsFullInfo(method)
                .orElse(Collections.emptyList());
    }
}
