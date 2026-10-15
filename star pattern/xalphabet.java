
import java.util.Scanner;
public class xalphabet {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for(int i = 1; i<=n;i++)
        {
            for(int j = 1; j<=n;j++)
            {
                if(j == i || i+j == n+1 )//i+j=6
                { 
                    System.out.print("x");
                }
                else
                {
                    System.out.print(" ");
                }
            
            }


            System.out.println();
        }

     
         }
    


}
