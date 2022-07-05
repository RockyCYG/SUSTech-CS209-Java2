import java.util.Arrays;

public class ByteBuffParser {
    public static int[] ParseByteBuff(Byte[] byteBuffer, int[] lengthOfBits) {
        //TODO:lengthOfBits stores the order and bits to be parsed
        //convert the contents of the byteBuffer to int according to lengthOfBits
        int[] ans = new int[lengthOfBits.length];
        int index = 0;
        int start = 0;
        for (int i = 0; i < lengthOfBits.length; i++) {
            if (start + lengthOfBits[i] <= 8) {
                int bits = byteBuffer[index] >> (8 - start - lengthOfBits[i]);
                ans[i] = bits & ((int) Math.pow(2, lengthOfBits[i]) - 1);
                start += lengthOfBits[i];
                if (start == 8) {
                    start = 0;
                    index++;
                }
            } else {
                int length = 8 - start;
                int sum = byteBuffer[index] & ((int) Math.pow(2, 8 - start) - 1);
                start = 0;
                index++;
                while (true) {
                    if (lengthOfBits[i] - length > 8) {
                        sum = (sum << 8) + byteBuffer[index];
                        length += 8;
                        index++;
                    } else {
                        int bits = byteBuffer[index] >> (8 - lengthOfBits[i] + length);
                        sum = (sum << (lengthOfBits[i] - length)) + (bits & ((int) Math.pow(2, lengthOfBits[i] - length) - 1));
                        start += lengthOfBits[i] - length;
                        if (start == 8) {
                            index++;
                            start = 0;
                        }
                        break;
                    }
                }
                ans[i] = sum;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        Byte[] buffer = {(byte) 128, (byte) 127, (byte) 60, (byte) 31, (byte) 21};
        int[] lengthOfBits = {3, 28, 5, 4}; //Notice:Each element <= 32
        //{(byte)128, (byte)4} ----> 10000000 00000100
        //                           |__||__| |______|
        //     split by lengthOfBits  4   4     8
        //                          1000 0000  00000100
        //               result:      8   0      4

        int[] result = ParseByteBuff(buffer, lengthOfBits);
        System.out.println(Arrays.toString(result));
        //8,0,4
    }
}


