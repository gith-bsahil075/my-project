
import java.util.Scanner;

class base1 {
    base1() {// constructor to hmesha class k name pr hota h
        System.out.println("mai ek constructor hu");
    }

    base1(int a) {
        System.out.println("mai ek constructor hu" + a);// constructor overload
    }

    public int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;

    }

}

class Derived1 extends base1 {// phle base k constructor call hoga]
    public int y;

    Derived1() {
        // super(0);// super mtlb uppar wala class ka value wala constructor
        System.out.println("mai 1 constructoe hunn 2");
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    Derived1(int x, int y) {
        super(x);
        System.out.println("mai e constructor hu" + x + y);
    }

}

class childofderived extends Derived1 {
    childofderived() {
        System.out.println("i am a child of derived constructor");
    }

    childofderived(int x, int y, int z) {
        super(x, y);
        System.out.println("i am a child of derived1" + z);
    }
}

public class constructorininheritance {
    public static void main(String[] args) {
        base1 b = new base1();
        Derived1 d = new Derived1(2, 4);
        childofderived cd = new childofderived(6, 7, 8);

    }

}
