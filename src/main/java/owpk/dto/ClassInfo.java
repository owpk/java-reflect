package owpk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import owpk.exception.ApplicationError;
import owpk.util.ReflectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassInfo {
    private String name;
    private String superClass;
    private List<String> interfaces;
    private List<String> annotations;
    private List<String> genericsType;
    private List<MethodInfo> methodInfo;

    public ClassInfo(String name) {
        this.name = name;
        superClass = "";
        interfaces = new ArrayList<>();
        genericsType = new ArrayList<>();
        annotations = new ArrayList<>();
    }

    public ClassInfo(Class<?> cl) {
        try {
            name = ReflectUtils.getClassName(cl);
            superClass = ReflectUtils.getSuperType(cl);
            interfaces = ReflectUtils.getInterfaces(cl);
            annotations = ReflectUtils.getClassAnnotations(cl);
            genericsType = ReflectUtils.getClassGenerics(cl);
            methodInfo = ReflectUtils.getMethods(cl).stream().map(x -> {
                try {
                    return new MethodInfo(x);
                } catch (ApplicationError applicationError) {
                    return new MethodInfo();
                }
            }).collect(Collectors.toList());
        } catch (ApplicationError applicationError) {
            // TODO: log
        }
    }
}
