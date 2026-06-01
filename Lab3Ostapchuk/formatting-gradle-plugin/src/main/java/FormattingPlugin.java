import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class FormattingPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        target.getTasks().register("breakLines", LineBreakTask.class);
        target.getTasks().register("clearEmptyLines", ClearEmptyLinesTask.class);
    }
}
