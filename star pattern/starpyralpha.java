import java.util.Scanner;
public class starpyralpha {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt(); 

        for(int i = 1; i<=a;i++)
        {
            int b = 1;
            
            for(int j = 1;j<=a-i;j++)
            {
                System.out.print(" ");
            }
            for(int k =1;k<=2*i-1;k++)
            {
                int c = b+64;
                char ch = (char)c;
                System.out.print(ch);
                b++;

            }
            System.out.println();
        }
    }
    
}
