import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        String sourceCodePath = "E:\\360MoveData\\Users\\CYG\\Desktop\\src.zip";
        List<String> files = readSourceCode(sourceCodePath);
        String byteCodePath = "E:\\360MoveData\\Users\\CYG\\Desktop\\rt.jar";
        List<String> classes = readByteCode(byteCodePath);
        List<String> newList = classes.stream().map(s -> {
            int index = s.indexOf('$');
            if (index >= 0) {
                return s.substring(0, index) + ".java";
            } else {
                return s.substring(0, s.length() - 5) + "java";
            }
        }).toList();
        System.out.println(".java files in src.zip that don't have corresponding .class files:");
        List<String> ans1 = files.stream().filter(s -> !newList.contains(s)).toList();
        System.out.println(ans1.size());
        ans1.forEach(System.out::println);
        System.out.println(".class files in rt.jar that don't have corresponding .java files:");
        List<String> ans2 = newList.stream().filter(s -> !files.contains(s)).toList();
        System.out.println(ans2.size());
        ans2.forEach(System.out::println);
    }

    public static List<String> readSourceCode(String path) throws IOException {
        List<String> files = new ArrayList<>();
        ZipEntry zipEntry = null;
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path));
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                String name = zipEntry.getName();
                String[] split = name.split("/");
                if (split.length > 2 && "java".equals(split[0]) && ("io".equals(split[1]) || "nio".equals(split[1]))) {
                    files.add(zipEntry.getName());
                }
            }
        }
        return files;
    }

    public static List<String> readByteCode(String path) throws IOException {
        List<String> classes = new ArrayList<>();
        JarEntry jarEntry = null;
        JarInputStream jarInputStream = new JarInputStream(new FileInputStream(path));
        while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
            if (!jarEntry.isDirectory()) {
                String name = jarEntry.getName();
                String[] split = name.split("/");
                if (split.length > 2 && "java".equals(split[0]) && ("io".equals(split[1]) || "nio".equals(split[1]))) {
                    classes.add(jarEntry.getName());
                }
            }
        }
        return classes;
    }
}
