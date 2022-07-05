import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"unchecked", "rawtypes"})
class TextProcessorTest {

    private static Class<?> textProcessorClass, positionClass;
    private static Object newConceptEnglish, messy_sentences;

    @BeforeAll
    static void setUp() {
        try {
            textProcessorClass = Class.forName("TextProcessor");
            positionClass = Class.forName("Position");
            checkDeclarations();
            newConceptEnglish = textProcessorClass.getDeclaredConstructor(String.class).newInstance("resources/new_concept_english.txt");
            messy_sentences = textProcessorClass.getDeclaredConstructor(String.class).newInstance("resources/messy_sentences.txt");
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    static void checkDeclarations() {
        MethodEntity[] textProcessorMethods = {
                new MethodEntity("getCommonWords", TreeMap.class, int.class, String[].class),
                new MethodEntity("highlightWord", ArrayList.class, positionClass),
                new MethodEntity("fixLowercaseFirstLetter", List.class),
                new MethodEntity("encrypt", String.class),
                new MethodEntity("ngramSim", HashSet.class, int.class, String.class),
        };
        MethodEntity[] positionMethods = {
                new MethodEntity("getLine", int.class),
                new MethodEntity("getCol", int.class)
        };
        ArrayList<String> errorMessages = new ArrayList<>();
        for (MethodEntity m : textProcessorMethods) {
            if (!m.checkForClass(textProcessorClass)) {
                errorMessages.add("[TextProcessor] " + m + " does not exist");
            }
        }
        for (MethodEntity m : positionMethods) {
            if (!m.checkForClass(positionClass)) {
                errorMessages.add("[Position] " + m + " does not exist");
            }
        }
        assertTrue(errorMessages.isEmpty(), String.join(System.lineSeparator(), errorMessages));
    }

    @Test
    void getCommonWords() {
        try {
            String[] stopwords = Files.readAllLines(Paths.get("resources", "stop_words_english.txt")).toArray(new String[]{});
            Method method = textProcessorClass.getMethod("getCommonWords", int.class, String[].class);
            final String[] words = {"time", "asked", "people", "man", "great", "told", "car", "years", "answered", "house"};
            final int[] freqs = {67, 45, 33, 30, 27, 27, 24, 22, 23, 21};
            Map<String, Integer> expected = IntStream.range(0, words.length)
                    .boxed()
                    .collect(Collectors.toMap(i -> words[i], i -> freqs[i]));
            Object res = method.invoke(newConceptEnglish, words.length, stopwords);
            assertTrue(res instanceof TreeMap);
            assertEquals(expected, res);
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void highlightWord() {
        try {
            Method method = textProcessorClass.getMethod("highlightWord", positionClass);
            List<String> expected = Arrays.asList(
                    "Position(line=1069,col=32)",
                    "Position(line=1101,col=34)",
                    "Position(line=1112,col=52)",
                    "Position(line=1393,col=21)",
                    "Position(line=1398,col=26)",
                    "Position(line=1630,col=26)",
                    "Position(line=1710,col=45)"
            );
            Object param = positionClass.getDeclaredConstructor(int.class, int.class).newInstance(1069, 32);
            Object res = method.invoke(newConceptEnglish, param);
            assertTrue(res instanceof ArrayList);
            List<String> resConverted = (List<String>) ((ArrayList) res).stream().map(this::formatPosition).collect(Collectors.toList());
            assertEquals(expected.toString(), resConverted.toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void fixLowercaseFirstLetter() {
        try {
            Method method = textProcessorClass.getMethod("fixLowercaseFirstLetter");
            List<String> expected = Arrays.asList(
                    "Position(line=2,col=1)",
                    "Position(line=2,col=46)",
                    "Position(line=6,col=2)",
                    "Position(line=6,col=18)",
                    "Position(line=6,col=34)",
                    "Position(line=6,col=54)",
                    "Position(line=7,col=1)",
                    "Position(line=7,col=26)",
                    "Position(line=8,col=1)",
                    "Position(line=8,col=7)",
                    "Position(line=8,col=15)",
                    "Position(line=8,col=22)",
                    "Position(line=8,col=28)",
                    "Position(line=8,col=32)"
            );
            Object res = method.invoke(messy_sentences);
            assertTrue(res instanceof List);
            List<String> resConverted = (List<String>) ((List) res).stream().map(this::formatPosition).collect(Collectors.toList());
            assertEquals(expected.toString(), resConverted.toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void encrypt() {
        try {
            Method method = textProcessorClass.getMethod("encrypt");
            String expected = "tseT4 lamron6 s115ecnetnes9.\ntsEt4 e101cnetneS8 ton3 gninnigeb9 htiw4 reppu5 e101sac4. tset4\n  e101cnetnes8 GNINNAPS8\ne101lpitlum8\ns115enil5..\n\tymmud5 e101cnetnes8. tset4 LLUF4 POTS4? tset4 NOITSEUQ8 KRAM4! tset4 NOITAMALCXE11 KRAM4.\ne101w2 t'nod5 redisnoc8 S83RBBA5. e101w2 t'nod5 redisnoc8 a97mmoc5 retfa5 lluf4 s115pots5.,\nylno4. e101lpmis6. s115esac5. lliw4. e101b2. detset6. 43214";
            Object res = method.invoke(messy_sentences);
            assertTrue(res instanceof String);
            assertEquals(expected, res.toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void ngramSim() {
        try {
            Method method = textProcessorClass.getMethod("ngramSim", int.class, String.class);
            Object res = method.invoke(messy_sentences, 3, "resources/messy_sentences2.txt");
            HashSet<List<String>> expected = new HashSet<>(Arrays.asList(Arrays.asList("test", "full", "stop"), Arrays.asList("full", "stop", "test")));
            assertTrue(res instanceof HashSet);
            assertEquals(expected, res);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    private String formatPosition(Object pos) {
        try {
            Method getLineMethod = positionClass.getMethod("getLine");
            Method getColMethod = positionClass.getMethod("getCol");
            Object line = getLineMethod.invoke(pos);
            Object col = getColMethod.invoke(pos);
            return String.format("Position(line=%s,col=%s)", line, col);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @Test
    public void testMethod1() {
        try {
            String[] stopwords = Files.readAllLines(Paths.get("resources", "stop_words_english.txt")).toArray(new String[]{});
            Method method = textProcessorClass.getMethod("getCommonWords", int.class, String[].class);
            final String[] words = new String[3000];
            final int[] freqs = new int[3000];
            Map<String, Integer> expected = new HashMap<>();
            Object res = method.invoke(newConceptEnglish, words.length, stopwords);
            assertTrue(res instanceof TreeMap);
            assertEquals(expected, res);
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMethod2() {
        try {
            Method method = textProcessorClass.getMethod("highlightWord", positionClass);
            List<String> expected = Arrays.asList(
                    "Position(line=167,col=38)",
                    "Position(line=458,col=1)",
                    "Position(line=1010,col=1)",
                    "Position(line=1540,col=27)",
                    "Position(line=1545,col=23)",
                    "Position(line=1558,col=30)",
                    "Position(line=1560,col=3)",
                    "Position(line=1782,col=27)"
            );
            Object param = positionClass.getDeclaredConstructor(int.class, int.class).newInstance(458, 1);
            Object res = method.invoke(newConceptEnglish, param);
            assertTrue(res instanceof ArrayList);
            List<String> resConverted = (List<String>) ((ArrayList) res).stream().map(this::formatPosition).collect(Collectors.toList());
            assertEquals(expected.toString(), resConverted.toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            fail();
        }
    }
}