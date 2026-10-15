import java.util.Scanner;
public class startriangleultamast {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int nst = n;
        int nsp = 0;
        for(int i = 1;i<=n;i++)
        {
            for(int j = 1;j<=nsp;j++)
            {
                System.out.print(" ");
            }
            for(int k = 1;k<=nst;k++)
            {
                System.out.print("*");
            }
            nsp++;
            nst--;
            System.out.println();
        }


    }

}
