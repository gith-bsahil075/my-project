import java.util.Scanner;

public class array2 {
    public static void main(String[] args) {

        float[] array = { 1.8f, 2.7f, 3.9f, 4.5f, 5.7f };
        float num = 1.8f;
        boolean isinarray = false;
        for (float arrays : array) {
            if (num == arrays) {
                isinarray = true;
                break;
            }
        }
        if (isinarray) {
            System.out.print("this num is in array");
        } else {
            System.out.print("this num is not in array");

        }

    }
}
