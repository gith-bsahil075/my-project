import java.util.Scanner;

public class multiplcationtable {
    // public static void main(String[] args) {
    // Scanner sc = new Scanner(System.in);
    // int n = sc.nextInt();
    // int mul;
    // for (int i = 1; i <= 10; i++) {
    // mul = n * i;
    // System.out.printf("%d * %d = %d\n", n, i, n * i);// print f dekhna
    // }

    // }
    // public static void main(String[] args) {
    // Scanner sc = new Scanner(System.in);
    // int n = sc.nextInt();
    // int factorial = 1;
    // int i = 1;
    // while (i <= n) {
    // factorial = factorial * i;
    // i++;
    // }
    // System.out.println(factorial);

    // }
    public static void main(String[] args)// AGAR SUM KRNA H TO 0 LENGE OR PRDUCT KRNA H TO 1 LENGE
    {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int sum = 0;
        int mul;
        for (int i = 1; i <= 10; i++) {
            mul = n * i;

            sum = sum + mul;
        }

        System.out.println(sum);

    }
}
