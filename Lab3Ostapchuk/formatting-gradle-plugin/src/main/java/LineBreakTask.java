import org.gradle.api.DefaultTask;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LineBreakTask extends DefaultTask {
    private final ProjectLayout projectLayout;

    @Inject
    public LineBreakTask(ProjectLayout projectLayout) {
        this.projectLayout = projectLayout;
    }

    @TaskAction
    public void breakLines() {
        Path srcDir = Path.of(projectLayout.getProjectDirectory().getAsFile().getAbsolutePath());
        try (Stream<Path> files = Files.walk(srcDir)) {
            files.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).forEach(file -> {
                try {
                    StringBuilder formattedFile = new StringBuilder();
                    for (String line : Files.readAllLines(file)) {
                        if (line.length() < 100) {
                            formattedFile.append(line).append("\n");
                            continue;
                        }
                        for (String part : breakLine(line)) {
                            formattedFile.append(part).append("\n");
                        }
                    }
                    Files.writeString(file, formattedFile.toString());
                } catch (IOException e){
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> breakLine(String line) {
        ArrayList<String> lines = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\s*");
        Matcher matcher = pattern.matcher(line);
        String whitespaces;
        if (matcher.find()) whitespaces = matcher.group();
        else whitespaces = "";
        for (int i = 40; i < line.length(); i++) {
            if (line.charAt(i) == '+' || line.charAt(i) == '-' || line.charAt(i) == '*' || line.charAt(i) == '/' || line.charAt(i) == '.') {
                if (!isInString(line, i)){
                    lines.add(line.substring(0, i));
                    lines.addAll(breakLine(whitespaces + '\t' + line.substring(i)));
                    return lines;
                }
            }
            if (line.charAt(i) == '=') {
                if (!isInString(line, i)) {
                    lines.add(line.substring(0, i+1));
                    lines.addAll(breakLine(whitespaces + '\t' + line.substring(i + 1)));
                    return lines;
                }
            }
        }
        lines.add(line);
        return lines;
    }

    private boolean isInString(String line, int position) {
        boolean inString = false;
        for (int i = 0; i <= position; i++) {
            if (line.charAt(i) == '"') inString = !inString;
        }
        return inString;
    }
}
