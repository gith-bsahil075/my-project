import java.util.Scanner;

public class veriableargument {
    static int sum(int... arr) {
        int result = 0;
        for (int b : arr) {
            result = result + b;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("welcome to the varrag tutorial");
        System.out.println("the sum of 2 and 4 is" + sum(7, 4, 4, 4, 4, 4, 4, 4, 4));
        System.out.println("the sum of nothing" + sum());
    }
}
