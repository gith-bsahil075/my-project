
// import java.util.Scanner;

// public class recursion {
//     static int factorial(int n) {
//         if (n == 0 || n == 1) {
//             return 1;
//         } else {
//             int product = 1;
//             for (int i = 1; i <= n; i++) {
//                 product *= i;
//             }
//             return product;
//         }

//         // if (n == 0 || n == 1) {
//         // return 1;
//         // } else {
//         // return n * factorial(n - 1);
//         // }
//     //}
import java.util.Scanner;

public class recursion {
    static int recursion(int n) {
        // if (n == 1) {
        // return 1;
        // } else {
        // return n + recursion(n - 1);
        // }
        if (n == 1) { 
            return 2;
        } else {
            return 2 * n + recursion(n - 1);
        }

    }

    public static void main(String[] args)

    {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        // System.out.printf("the value of factorial is :" + factorial(n));
        System.out.println(recursion(n));
    }

}
