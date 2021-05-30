package owpk.util;

public enum Color {
    PINK(213), GREEN(46), BLUE(45), YELLOW(179), RED(124);
    private int ind;

    Color(int ind) {
        this.ind = ind;
    }

    public int getInd() {
        return ind;
    }

    public static String colorize(String input, Color color) {
        return String.format("\033[38;5;%dm%s\033[0m", color.getInd(), input);
    }
}
