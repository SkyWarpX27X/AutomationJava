import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class DictionaryTest {

    private SearchDictionary dictionary;

    @BeforeEach
    void setUp(){
        dictionary = new SearchDictionary();
        dictionary.addWord("world");
        dictionary.addWord("hello");
        dictionary.addWord("he");
        dictionary.addWord("123");
    }

    @Tag("edit")
    @Test
    void testAddFromFile(){
        assumeTrue(Files.exists(Path.of("words.txt")), "words.txt not found");
        dictionary.addWordsFromFile("words.txt");
        assertTrue(dictionary.hasWord("java"), "Words from file not added to dictionary");
    }

    @Tag("edit")
    @ParameterizedTest
    @ValueSource(strings = {"hello", "123", "world"})
    void testDelete(String word){
        dictionary.delWord(word);
        assertFalse(dictionary.hasWord(word), word + " still in dictionary after deletion");
    }

    @Tag("retrieve")
    @ParameterizedTest
    @CsvSource({
            "12, false",
            "hello, true",
            "word, false"
    })
    void testWordPresence(String word, boolean isPresent){
        assertEquals(dictionary.hasWord(word), isPresent, word + " presence in dictionary detected incorrectly");
    }

    @Tag("retrieve")
    @TestFactory
    Stream<DynamicTest> testQuery(){
        return Stream.of("he*", "wo*", "qwe*", "he")
                .map(query -> DynamicTest.dynamicTest("Test if all results starts with "+ query, () -> {
                    for (String res : dictionary.query(query)) {
                        if (query.endsWith("*"))
                            assertTrue(res.startsWith(query.replace("*", "")), "Query " + query + " retrieved " + res);
                        else
                            assertEquals(query, res, query + " must retrieve only itself if exists, but retrieved " + res);
                    }
                }));
    }
}