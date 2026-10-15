class ekclass {
    int a;

    ekclass(int a) {
        this.a = a;
    }

    public int returnone() {
        return 1;
    }

    public int geta() {
        return a;
    }
}

class doclass extends ekclass {
    doclass(int c) {
        super(c);
        System.out.println("mai ek constructor hu");

    }
}

public class thisandsuperkey {
    public static void main(String[] java) {
        ekclass e = new ekclass(5);
        doclass d = new doclass(5);
        System.out.println(e.geta());
    }

}
