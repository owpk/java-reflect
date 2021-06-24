package owpk.util;

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

    public String formatClass(List<String> annotations,
          String className, List<String> generics,
          String superClass, List<String> interfaces) {
        var cAnnotations = colorizeList(annotations, Color.YELLOW, "%s%n");
        var cGenerics = colorizeList(generics, Color.GREEN, "<%s>");
        var cSuperClass = colorizeString(superClass, Color.BLUE,
                Color.colorize("\n\textends ", Color.YELLOW) + "%s");
        var cInterfaces = colorizeList(interfaces, Color.GREEN, ", ",
                Color.colorize("\n\timplements ", Color.YELLOW) + "%s");
        return String.format("%s%s%s%s%s", cAnnotations, className, cGenerics, cSuperClass, cInterfaces);
    }

    public String formatMethod(List<String> annotations, String mod,
          String returnType, List<String> args, String methodName) {
        var cAnnotations = colorizeList(annotations, Color.YELLOW, "%s%n");
        var cMod = colorizeString(mod, Color.PINK, "%s ");
        var cReturnType = colorizeString(returnType, Color.BLUE, "%s ");
        var cMethodArgs = colorizeList(args, Color.BLUE);
        var cName = methodName.isBlank() ? "" : methodName;
        return String.format("%s%s%s%s(%s)", cAnnotations, cMod, cReturnType, cName, cMethodArgs);
    }
}
