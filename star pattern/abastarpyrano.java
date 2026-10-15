import java.util.Scanner;

public class abastarpyrano {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        
       
        for (int i = 1; i <= a; i++) {
              int s = i-1;
           
            
            for (int j = 1; j <= a - i; j++) {
                System.out.print(" ");
            }
            for (int k = 1; k <= i; k++) {
                System.out.print(k);
            }
            for(int l = 1;l<=i-1;l++)
            {
               
                System.out.print(s);
                s--;
            }


            System.out.println();

        }
    }

}
