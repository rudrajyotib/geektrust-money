package geektrust.money.api;

import geektrust.money.manage.Portfolio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileCommandProcessor {

    private final String filePath;
    private final Portfolio portfolio;

    public FileCommandProcessor(String filePath, Portfolio portfolio) {
        this.filePath = filePath;
        this.portfolio = portfolio;
    }

    public List<String> process() throws IOException {
        File commandFile = new File(this.filePath);
        if (!commandFile.exists()) {
            throw new IllegalArgumentException("File path does not exist");
        }
        if (!commandFile.isFile()) {
            throw new IllegalArgumentException("Not a file");
        }
        try (Stream<String> commandStream = Files.lines(Paths.get(this.filePath))) {
            return commandStream
                    .map(s -> s.split(" "))
                    .map(strings -> Command.valueOf(strings[0]).process(strings, portfolio))
                    .map(s -> s.orElse(""))
                    .filter(s -> !"".equals(s))
                    .collect(Collectors.toList());
        }
    }
}
