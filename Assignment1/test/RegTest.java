import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {

    @Test
    public void test01() {
        String s = "Hello Beijing. Hello Java's!";
//        String[] split = s.split("[\\s.]+");
        String regex = "\\w+['-]\\w+|\\w+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Test
    public void test02() {
        String[] a = {"4", "2", "3"};
        Arrays.sort(a, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return (Integer.parseInt(o1) - Integer.parseInt(o2));
            }
        });
        System.out.println(Arrays.toString(a));
    }

    @Test
    public void test03() {
        String s = "   Beijing huanyingni!Xiang YinyueGan dongNi.Hello Java?adsasgdas.     ";
//        String[] split = s.split("[.]");
//        Arrays.stream(split).forEach(System.out::println);
        String path = "D:\\java\\CS209\\Assignment1\\resources\\messy_sentences.txt";
        StringBuilder sb = new StringBuilder("sgldlsgaags");
        sb.reverse();
        System.out.println(sb);
        sb.insert(1, 101);
        System.out.println(sb);
/*        try (FileReader fr = new FileReader(path)) {
            int read;
            while ((read = fr.read(chars)) != -1) {
                System.out.println(1);
            }
            System.out.println(Arrays.toString(chars));
        }*/
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            br.readLine();
            String line = br.readLine();
            System.out.println(line.charAt(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test04() {
        HashSet<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        HashSet<Integer> set2 = new HashSet<>();
        set2.add(2);
        set2.add(3);
        set1.removeAll(set2);
        System.out.println(set1);
        System.out.println(set2);
    }

    @Test
    public void test05() {
        List<String> list = new ArrayList<>();
        list.add("asdfagasdg");
        list.add("at");
        list.sort(String::compareTo);
        System.out.println(list);
    }

    @Test
    public void test06() {
        String s1 = "-(-a-b---c')-'d- -'d-b-c-a----";
        String s2 = "      ''a'b'c''d''          ";
        String s3 = "o'clock";
        String regex = "\\w+(['-]+\\w+)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s3);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Test
    public void testSentence() {
        String regex = "\\w[\\w-',\\s]*[.?!]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("I have just received a letter from my brother, Tim. he is in Australia.");
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}
