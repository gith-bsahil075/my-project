// import java.util.Scanner;

// public class methodpractice {
//     static void method(int a) {

//         for (int i = 1; i <= 10; i++) {

//             System.out.println(a + "*" + i + "is" + "=" + a * i);
// System.out.format("%d x %d = %d" a,i,a*i);
//         }

//     }

//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         method(a);
//     }

// }
// import java.util.Scanner;

// public static void main(String[] args) {
//     Scanner sc = new Scanner(System.in);
//     int a = sc.nextInt();
//     for (int i = 1; i <= a; i++) {
//         for (int j = 1; j <= i; j++) {
//             System.out.printf("*");
//         }
//         System.out.println();
//     }

// }
// import java.util.Scanner;

// public class recursion {
//     static int recursion(int n) {
//         // if (n == 1) {
//         // return 1;
//         // } else {
//         // return n + recursion(n - 1);
//         // }
//         if (n == 1) {
//             return 2;
//         } else {
//             return 2 * n + recursion(n - 1);
//         }

//     }

//     public static void main(String[] args)

//     {

//         Scanner sc = new Scanner(System.in);
//         int n = sc.nextInt();

//         // System.out.printf("the value of factorial is :" + factorial(n));
//         System.out.println(recursion(n));
//     }

// }
//reverse *printing
import java.util.Scanner;

public class methodpractice {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        for (int i = 1; i <= a; i++)

        {
            for (int j = 1; j <= a - i; j++) {
                System.out.printf("*");
            }
            System.out.println();
        }
    }
}

//?????????????? jab index diya ho to 0 se start krenge or jab nth term diya hogaa to 1 se star krna h
// 
// import java.util.Scanner;

// public class methodpractice {
//     static int avg(int... arr) {
//         int c = 0;

//         for (int b : arr) {
//             c = c + b;

//         }
//         return c / arr.length;

//     }

//     public static void main(String[] args) {
//         System.out.println("the average of arrays = " + avg(2, 3, 4, 5, 6, 3, 5, 6, 7, 7, 6, 6, 67777));
//     }

// }
// import java.util.Scanner;

// public class methodpractice {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int c = sc.nextInt();
//         int ferhenite = 9 / 5 * c + 32;
//         System.out.println(ferhenite);
//     }
// }
public class methodpractice {
    public static int sum(int start, int end) {
        if (end > start) {
            return end + sum(start, end - 1);
        } else {
            return end;
        }
    }

    public static void main(String[] args) {
        int result = sum(5, 10);
        System.out.println(result);
    }
}
