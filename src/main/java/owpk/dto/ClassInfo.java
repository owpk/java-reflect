package owpk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import owpk.util.ReflectUtils;

import java.util.ArrayList;
import java.util.Collections;
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
        name = ReflectUtils.getClassName(cl).orElse("");
        superClass = ReflectUtils.getSuperType(cl).orElse("");
        interfaces = ReflectUtils.getInterfaces(cl).orElse(Collections.emptyList());
        annotations = ReflectUtils.getClassAnnotations(cl).orElse(Collections.emptyList());
        genericsType = ReflectUtils.getClassGenerics(cl).orElse(Collections.emptyList());
        var opt = ReflectUtils.getMethods(cl);
        if (opt.isPresent()) {
            var methods = opt.get();
            methodInfo = methods.stream().map(MethodInfo::new).collect(Collectors.toList());
        } else methodInfo = Collections.emptyList();
    }
}
