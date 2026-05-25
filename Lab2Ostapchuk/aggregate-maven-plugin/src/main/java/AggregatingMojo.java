import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
@Mojo(name = "aggregate")
public class AggregatingMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true)
    private MavenProject project;

    @Parameter(property = "aggregate.dest", defaultValue = "${project.basedir}/aggregated/Aggregated.java")
    private Path destination;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            Files.createDirectories(destination.getParent());
            Files.createFile(destination);
            BufferedWriter writer = new BufferedWriter(new FileWriter(destination.toString()));
            for (String source : project.getCompileSourceRoots()){
                try (Stream<Path> files = Files.walk(Path.of(source))){
                    files.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).forEach(file -> {
                        try (Stream<String> lines = Files.lines(file)){
                            lines.forEach(line -> {
                                try {
                                    if (line.contains("public class")){
                                        writer.write(unpublicClass(line));
                                    } else {
                                        writer.write(line);
                                    }
                                    writer.newLine();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLog().info("All java code aggregated in " + destination);
    }

    private String unpublicClass(String line){
        String[] separated = line.split("public class", 2);
        if ((separated[0].contains("\"") && separated[1].contains("\"")) || separated[0].contains("//") || separated[0].contains("/*"))
            return line;
        return line.replace("public class", "class");
    }
}
