import java.util.Scanner;
public class floydstriangle {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int c = 1;
        for(int i =1; i<=a;i++)
        {
            
            
            for(int j = 1; j<=i;j++)
            {
                System.out.print(c);
                
                c = c+1;

            }
            System.out.println();
        }
    }
    
}
