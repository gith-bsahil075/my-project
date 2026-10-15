
public class method1 {
    static void change(int a) {
        a = 98;// isme koi bhi value de change nhi hoga
    }

    static void telljoke() {
        System.out.println("i,m a hero");
    }

    static void change2(int[] array) {
        array[0] = 98;
    }

    static void foo() {
        System.out.println("good morning shailesh");
    }

    static void foo(int a) {// yha int a parameter h
        System.out.printf("this is " + a + " good morning");

    }

    public static void main(String[] args) {
        int[] marks = { 54, 67, 87, 89 };
        int x = 45;
        change2(marks);
        System.out.println("this is sahil- " + marks[0]);// object ki copy bnake pass nhi hui h uska reference pass hua
                                                         // h

        foo();
        foo(800);// yha 8000 argument h
    }

}
