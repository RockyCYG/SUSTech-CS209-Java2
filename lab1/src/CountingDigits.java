public class CountingDigits {

    public static int getNumberOfDigitZero(long value) {
        //TODO:to count the number of digit 0 appearng in a long
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if ((value & 1) == 0) {
                count++;
            }
            value = value >> 1;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(getNumberOfDigitZero(1280));
    }
}
