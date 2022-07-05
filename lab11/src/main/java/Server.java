import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ArrayList<Teacher> teacherList = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("./FacultyList.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] teacher = line.split(",");
                teacherList.add(new Teacher(teacher[0].trim(), teacher[1].trim(), teacher[2].trim()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int port = 6666;
        PrintWriter out = null;
        BufferedReader in = null;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is OK, is waiting for connect......");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                if ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    String command = inputLine.split(" ")[0];
                    if ("NAME".equals(command)) {
                        String name = inputLine.substring(5);
                        for (Teacher teacher : teacherList) {
                            if (name.equals(teacher.getName())) {
                                out.println(teacher.getName() + ", " + teacher.getPosition() + ", " + teacher.getDepartment());
                            }
                        }
                    } else if ("FIRSTLETTER".equals(command)) {
                        String firstLetter = inputLine.substring(12);
                        for (Teacher teacher : teacherList) {
                            if (teacher.getName().startsWith(firstLetter)) {
                                out.println(teacher.getName() + ", " + teacher.getPosition() + ", " + teacher.getDepartment());
                            }
                        }
                    } else if ("DEP".equals(command)) {
                        String department = inputLine.substring(4);
                        for (Teacher teacher : teacherList) {
                            if (department.equals(teacher.getDepartment())) {
                                out.println(teacher.getName() + ", " + teacher.getPosition() + ", " + teacher.getDepartment());
                            }
                        }
                    } else {
                        out.println("GoodBye");
                    }
                    System.out.println("Command processed");
                }
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
