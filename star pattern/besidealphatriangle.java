import java.util.Scanner;

public class besidealphatriangle {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        //int a = 1;
       
        for(int i = 1;i<=n;i++)
           
        {
             int a = 1;
          for(int j = 1;j<=n-i;j++)
          {
            System.out.print(" ");
          }
          for(int k = 1;k<=i;k++)
          {
             int d = a + 64;
        char ch = (char)d;
            System.out.print(ch);
            a++;

          }
          System.out.println();
        }
    }
    }
