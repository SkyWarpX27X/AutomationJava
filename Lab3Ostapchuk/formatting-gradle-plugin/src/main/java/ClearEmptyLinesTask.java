import org.gradle.api.DefaultTask;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ClearEmptyLinesTask extends DefaultTask {
    private final ProjectLayout projectLayout;

    @Inject
    public ClearEmptyLinesTask(ProjectLayout projectLayout) {
        this.projectLayout = projectLayout;
    }

    @TaskAction
    public void clearEmptyLines() {
        Path srcDir = Path.of(projectLayout.getProjectDirectory().getAsFile().getAbsolutePath());
        try (Stream<Path> files = Files.walk(srcDir)) {
            files.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).forEach(file -> {
                try {
                    StringBuilder cleanedFile = new StringBuilder();
                    boolean isPrevEmpty = true;
                    for (String line : Files.readAllLines(file)) {
                        if (line.isBlank()) {
                            if (isPrevEmpty) {
                                continue;
                            }
                            isPrevEmpty = true;
                        } else isPrevEmpty = false;
                        cleanedFile.append(line).append("\n");
                    }
                    Files.writeString(file, cleanedFile.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
