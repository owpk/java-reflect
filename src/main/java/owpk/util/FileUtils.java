package owpk.util;

import owpk.exception.ResourceException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileUtils {

    public static List<String> readAllLines(String path) throws ResourceException {
        List<String> lines;
        try {
            lines = new ArrayList<>(Files.readAllLines(Paths.get(path)));
        } catch (IOException e) {
            throw new ResourceException(e.getLocalizedMessage());
        }
        return lines;
    }


    public static List<String> readAllLines(Path path) throws ResourceException {
        List<String> lines;
        try {
            lines = new ArrayList<>(Files.readAllLines(path));
        } catch (IOException e) {
            throw new ResourceException(e.getLocalizedMessage());
        }
        return lines;
    }

    public static void appendLineToFile (String line, String path) throws ResourceException {
        try {
            Files.write(Paths.get(path),
                    (line + "\n").getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ResourceException(e.getLocalizedMessage());
        }
    }

    public static void distinctFileLines(String path) throws ResourceException {
        try {
            Path p = Paths.get(path);
            Set<String> set = new HashSet<>(Files.readAllLines(p));
            Files.write(p, set);
        } catch (IOException e) {
            throw new ResourceException(e.getLocalizedMessage());
        }
    }

    public static void clear(String path) throws ResourceException {
        try {
            FileWriter fwOb = new FileWriter(path, false);
            PrintWriter pwOb = new PrintWriter(fwOb, false);
            pwOb.flush();
            pwOb.close();
            fwOb.close();
        } catch (IOException e) {
            throw new ResourceException(e.getLocalizedMessage());
        }
    }

    public static void write(Path path, Set<String> set) throws ResourceException {
        try {
            Files.write(path, set);
        } catch (IOException e) {
            throw new ResourceException(e.getLocalizedMessage());
        }
    }
}
