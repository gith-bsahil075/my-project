import java.util.Scanner;

public class numberalphabet {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        
        {
         for(int i = 1;i<=n; i++)
         { 
            int a = 1;
           for(int j =1; j<=i;j++)
           {
            
             if(i%2 == 0)
             
            
            int d = a+64;
            char ch = (char)d;
                System.out.print(ch);
                a++;

             }
             else{
                System.out.print(j);
             }
            

           }
          System.out.println();
         }

        }


    }


    
}
