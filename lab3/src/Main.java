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
/*        String sourceCodePath = "C:\\Users\\CYG\\Desktop\\src.zip";
        List<String> files = readSourceCode(sourceCodePath);
        System.out.println("#of .java files in java.io/java.nio: " + files.size());
        files.forEach(System.out::println);*/
        String byteCodePath = "E:\\360MoveData\\Users\\CYG\\Desktop\\rt.jar";
        List<String> classes = readByteCode(byteCodePath);
        System.out.println("#of .class files in java.io/java.nio: " + classes.size());
        classes.forEach(System.out::println);
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
