package owpk;

import owpk.command.CacheCommand;
import owpk.command.ClassCommand;
import picocli.CommandLine;

@CommandLine.Command(subcommands = {
        CacheCommand.class, ClassCommand.class
}, mixinStandardHelpOptions = true, version = "reflect 1.0",
        description = "prints information about class or jar library (e.g. methods signature, class names etc) to STDOUT" +
                "\n\tjava 11+ version required for usage")
public class Reflect {

    public static void main(String[] args) {
        new CommandLine(Reflect.class).execute(args);
    }
}
