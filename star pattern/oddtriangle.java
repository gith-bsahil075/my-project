import java.util.Scanner;

public class oddtriangle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int i;

        for (i = 1; i <= n; i++) {

            for (int j = 1; j <= 2*i-1; j = j + 2) {
                // for(int k = 1;k<=n;k+1)
                // {
                // System.out.print()
                // }
                System.out.print(j);

            }
            System.out.println();
        }
    }
}
