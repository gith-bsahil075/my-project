import java.util.Scanner;

public class method2 {
    static int logic(int x, int y) { // jb int k phle kuchh nhi h to access modifier default hota h
        int z;
        if (x > y) {
            z = x + y;
        } else {
            z = (x + y) * 5;
        }

        return z;

    }

    public static void main(String[] args) {
        // static jruri h kyuki ye static method h
        method2 obj = new method2();

        int a = 12;
        int b = 31;
        int c;
        c = obj.logic(a, b);
        int a1 = 23;
        int b2 = 34;
        int c1;
        c1 = obj.logic(a1, b2);
        System.out.println(c);
        System.out.println(c1);

    }
}