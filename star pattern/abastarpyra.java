import java.util.Scanner;
public class abastarpyra {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        for(int i = 1;i<=a;i++)
        {
             int d = 1;
           int g = i-1;
            for(int j = 1;j<=a-i;j++)
            {
                System.out.print(" ");
            }
            for(int k = 1;k<=i;k++)
            {
              
                
         char ch = (char)(d+64);
           
                System.out.print(ch);
                d++;
            }  
            for(int l = 1;l<=i-1;l++)
                
         { //int f = 1;
             //    int h = f+65;
            //char z  = (char)f;
            

             char ch = (char)(g+64);
              System.out.print(ch);
              g--;
            }
            System.out.println();

           }
       }
    }
