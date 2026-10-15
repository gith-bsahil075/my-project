
import java.util.Scanner;
public class besidestartriangle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for(int i = 1; i<=n;i++)
        {
            //for(int j = 1; j<= n-i;j++)
            //{
               // if( n-i == j)//i+j=6
                //{ 
                  //  System.out.print("x");
               // }
                //else
               // {
                 //   System.out.print(" ");
               // }
             //  System.out.print("*");
              
            
           // }
            for(int k = 1; k<=i;k++)
            {
              System.out.print("*");
            }

            System.out.println();
        }

     
         }
    


}
