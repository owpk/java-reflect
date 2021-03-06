package owpk.util;

import java.util.List; import java.util.stream.Collectors;

public class PrettyConsole {

    public String colorizeList(List<String> list, Color color, String delimiter, String format) {
        if (checkForEmptyList(list))
            return String.format(format, list.stream().map(x -> Color.colorize(x, color)).collect(Collectors.joining(delimiter)));
        else return "";
    }

    public String colorizeList(List<String> list, Color color) {
        return colorizeList(list, color, ", ", "%s");
    }

    private String colorizeList(List<String> list, Color color, String format) {
        return colorizeList(list, color, ", ", format);
    }

    public String colorizeString(String input, Color color, String format) {
        if (checkForEmptyString(input))
            return String.format(format, Color.colorize(input, color));
        else return "";
    }

    public String colorizeString(String input, Color color) {
        return colorizeString(input, color, "%s");
    }

    public boolean checkForEmptyString(String input) {
        return input != null && !input.isBlank();
    }

    public boolean checkForEmptyList(List<String> list) {
        return list != null && list.size() > 0;
    }

    public String formatClass(List<String> annotations, String formattedClassName,
                              String formattedSuperClass, List<String> formattedInterfaces) {
        var cAnnotations = colorizeList(annotations, Color.YELLOW, "%s%n");
        var cSuperClass = checkForEmptyString(formattedSuperClass) ? Color.colorize("\n\textends ", Color.YELLOW) + formattedClassName : "";
        var cInterfaces = checkForEmptyList(formattedInterfaces) ? Color.colorize("\n\timplements ", Color.YELLOW) + colorizeList(formattedInterfaces, Color.GREEN) : "";
        return String.format("%s%s%s%s", cAnnotations, formattedClassName, cSuperClass, cInterfaces);
    }

    public String formatClassName(Color color, String name, List<String> generics) {
        String cName;
        if (color != null)
            cName = colorizeString(name, color);
        else cName = name;
        var cGenerics = colorizeList(generics, Color.GREEN, "<%s>");
        return String.format("%s%s", cName, cGenerics);
    }

    public String formatClassName(String name, List<String> generics) {
        return formatClassName(null, name, generics);
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



