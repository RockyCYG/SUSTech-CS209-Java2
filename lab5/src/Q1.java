import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(scanner.nextInt());
        }
        list.stream().filter(i -> i % 2 == 0).forEach(i -> System.out.print(i + " "));
        System.out.println();
        list.stream().filter(i -> i % 2 == 1).forEach(i -> System.out.print(i + " "));
        System.out.println();
        list.stream().filter(Q1::isPrime).forEach(i -> System.out.print(i + " "));
        System.out.println();
        list.stream().filter(Q1::isPrime).filter(i -> i > 5).forEach(i -> System.out.print(i + " "));
    }

    public static boolean isPrime(int n) {
        if (n == 1 || n == 2) {
            return true;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
