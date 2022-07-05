import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] chessboard = new int[8][8];
        A b = new B();
        b.setChessboard(chessboard);
        System.out.println(Arrays.deepToString(b.getChessboard()));
    }
}
