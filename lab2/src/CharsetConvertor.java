import java.io.*;
import java.nio.charset.StandardCharsets;

public class CharsetConvertor {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("The number of arguments aren't three!");
            return;
        }
        String source = args[0];
        String target = source + "_utf8";
        String charset1 = args[1];
        String charset2 = args[2];
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\java\\CS209\\src\\lab2\\" + source), charset1));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\java\\CS209\\src\\lab2\\" + target), charset2))) {
            String str = "";
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            bw.write(sb.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
