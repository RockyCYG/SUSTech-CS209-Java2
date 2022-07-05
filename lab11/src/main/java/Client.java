import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 6666;
        boolean flag = true;
        while (flag) {
            try (Socket socket = new Socket(hostname, port)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Please enter a command:");
                Scanner scanner = new Scanner(System.in);
                String inputLine = scanner.nextLine();
                out.println(inputLine);
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    System.out.println(fromServer);
                    if ("GoodBye".equals(fromServer)) {
                        flag = false;
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
