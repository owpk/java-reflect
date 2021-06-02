package owpk.util;

import owpk.dto.ClassInfo;
import owpk.dto.MethodInfo;

import java.util.List;
import java.util.stream.Collectors;

public class PrettyConsole {

    public String colorizeList(List<String> list, Color color, String delimiter, String format) {
        if (list.size() == 0) return "";
        return String.format(format, list.stream().map(x -> Color.colorize(x, color)).collect(Collectors.joining(delimiter)));
    }

    public String colorizeList(List<String> list, Color color) {
        return colorizeList(list, color, ", ", "%s");
    }

    private String colorizeList(List<String> list, Color color, String format) {
        return colorizeList(list, color, ", ", format);
    }

    public String colorizeString(String input, Color color, String format) {
        if (input.isBlank())
            return "";
        return String.format(format, Color.colorize(input, color));
    }

    public String colorizeString(String input, Color color) {
        return colorizeString(input, color, "%s");
    }

    public String formatClass(ClassInfo classInfo) {
        var cAnnotations = colorizeList(classInfo.getAnnotations(), Color.YELLOW, "%s%n");
        var cName = classInfo.getName();
        var cGenerics = colorizeList(classInfo.getGenericsType(), Color.GREEN, "<%s>");
        var cSuperClass = colorizeString(classInfo.getSuperClass(), Color.BLUE,
                Color.colorize("\n\textends ", Color.YELLOW) + "%s");
        var cInterfaces = colorizeList(classInfo.getInterfaces(), Color.GREEN, ", ",
                Color.colorize("\n\timplements ", Color.YELLOW) + "%s");
        return String.format("%s%s%s%s%s", cAnnotations, cName, cGenerics, cSuperClass, cInterfaces);
    }

    public String formatMethod(MethodInfo methodInfo) {
        var cAnnotations = colorizeList(methodInfo.getAnnotations(), Color.YELLOW, "%s%n");
        var cMod = colorizeString(methodInfo.getModifier(), Color.PINK, "%s ");
        var cReturnType = colorizeString(methodInfo.getReturnType(), Color.BLUE, "%s ");
        var cMethodArgs = colorizeList(methodInfo.getMethodArgs(), Color.BLUE);
        var cName = methodInfo.getName().isBlank() ? "" : methodInfo.getName();
        return String.format("%s%s%s%s(%s)", cAnnotations, cMod, cReturnType, cName, cMethodArgs);
    }
}
