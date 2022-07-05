import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTypeParser {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("The number of arguments aren't one!");
        }
        String fileName = args[0];
        Map<String, String> fileTypes = new HashMap<>();
        fileTypes.put("89504e47", "png");
        fileTypes.put("504b0304", "zip or jar");
        fileTypes.put("cafebabe", "class");
        try (FileInputStream fis = new FileInputStream("D:\\java\\CS209\\src\\lab2\\" + fileName)) {
            byte[] bytes = new byte[4];
            fis.read(bytes, 0, 4);
            String fileType = bytesToHexString(bytes);
            System.out.println("Filename: " + fileName);
            System.out.println("File Header(Hex): " + fileType);
            System.out.println("File Type: " + fileTypes.get(fileType));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        for (int i = 0; i < bytes.length; i++) {
            int t = bytes[i] & 0xFF;
            String hex = Integer.toHexString(t);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
