import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(DictionaryTest.class)
@IncludeTags("edit")
public class EditDictionaryTest {
}