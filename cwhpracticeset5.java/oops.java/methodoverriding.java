class a {
    public int a;

    public int harry() {
        return 4;
    }

    public void meth2() {
        System.out.println("i am method 2 of class a" + a);
    }

}

class b extends a {
    public void meth3() {
        System.out.println("i am meth3 of class b");
    }

    @Override
    public void meth2() {
        System.out.println("i m method 2 of class b");
    }

}

public class methodoverriding {
    public static void main(String[] args) {
        a A = new a();
        b B = new b();
        A.meth2();
        B.meth2();
        B.meth3();
    }

}
