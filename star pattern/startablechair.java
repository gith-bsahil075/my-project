import java.util.Scanner;
public class startablechair {
    public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int nst = n;
    int nsp = 0;
    for(int m = 1;m<=2*n+1;m++){
       // System.out.print(m);
      //System.out.print("*");
    }
    System.out.println();
    for(int i = 1;i<=n;i++)
    {
       // int a = 1;
       int b = 1;
       int z = b+64;
       char ch = (char)z;
       for(int j = 1;j<=nst;j++)
       {
       // System.out.print(j);
       // System.out.print("*");
       System.out.print(ch);
       ch++;
       } 
       for(int k = 1;k<=nsp;k++){
        System.out.print(" ");
        ch++;
       }
       for(int l = 1; l<=nst;l++){
       // System.out.print(l);
       // System.out.print("*");
       System.out.print(ch);
       ch++;
       }
       nsp = nsp+2;
       nst--;
       System.out.println();
    }
    
}
}
