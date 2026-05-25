import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
// TODO for test
@Mojo(name = "todo", defaultPhase = LifecyclePhase.VALIDATE)
public class CodeCheckingMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true)
    private MavenProject project;

    //TODO for test again
    @Override
    public void execute() throws MojoExecutionException {
        for (String source : project.getCompileSourceRoots()){
            try (Stream<Path> files = Files.walk(Path.of(source))){
                files.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).forEach(file -> {
                    try {
                        for (String todo : getAllTodo(Files.readString(file))){
                            getLog().info(todo);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ArrayList<String> getAllTodo(String file){
        ArrayList<String> res = new ArrayList<>();
        Pattern pattern = Pattern.compile("(// ?TODO.*?$)|(/\\* ?TODO.*?\\*/)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(file);
        while (matcher.find()){
            res.add(matcher.group());
        }
        return res;
    }
}
