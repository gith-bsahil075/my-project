import java.util.Scanner;

public class downtriangle {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n - i; j++) {
        // if(i+j==6)
        // {
        // System.out.print(j);
        // }
        // else
        // {
        System.out.print(j);// jo khali space bachega usse * acquire krlega
        // }
      }
      System.out.println();

    }
  }

}