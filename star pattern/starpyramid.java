import java.util.Scanner;
public class starpyramid {
    public static void main(String[] args)
     {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        //int s = sc.nextInt();
        for(int i = 1;i<=a;i++)
        { 
            for(int j = 1; j<= a-i;j++)
            {
               System.out.print(" "); 
            }
            
                for(int l = 1;l<=2*i-1;l++){
               System.out.print("*");
                }
                System.out.println();
           }
            
        }


    }
    

