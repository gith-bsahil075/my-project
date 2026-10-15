class base {
    public int x;

    public int getx() {
        return x;
    }

    public void setx(int x) {
        System.out.println("im in base and setting x now");
        this.x = x;
    }

    public void printme() {
        System.out.println("i am a constructor");
    }// constructor
}

class Derived extends base {
    int y;

    public int gety() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

public class inheritance {
    public static void main(String[] args) {
        Derived b = new Derived();
        b.setx(45);
        System.out.println(b.getx());
        b.setY(43);
        System.out.println(b.gety());// animal or dog ka inheritance bnana h

    }
}
