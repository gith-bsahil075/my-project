// public class constructor{

// class MyMainEmployee {
//     private int id;
//     private String name;

//     public MyMainEmployee(String myname) {
//         id = 56;
//         name = myname;
//     }

//     public String getname() {
//         return name;
//     }

//     public int getid() {
//         return id;
//     }

//       public static void main(String[] args) {
//             MyMainEmployee harry = new MyMainEmployee("codewithharry");
//             System.out.println(harry.getid());
//             System.out.println(harry.getname());
//         }

// }
//  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// import java.util.Scanner;

// class Cylinder {

//     private int height;// private lgane pr getter setter us hots h
//     private int radius;

//     public int cylinderradius(int radius, int height) {
//         radius = 2 * 4 * radius * height;
//         return radius;// math.pie
//     }

//     public int cylindervolume(int r, int h) {
//         return 4 * r * r * h;// math.pie
//     }

//     public Cylinder(int a, int b) {
//         this.radius = a;
//         this.height = b;
//     }

//     public void setradius(int a) {
//         radius = a;

//     }

//     public float getradius() {
//         return radius;
//     }

//     public void setheight(int b) {
//         height = b;
//     }

//     public float getheight() {
//         return height;
//     }// constructor bnayenge
//      // public cylinder(int a, int b) {
//      // height = a;
//      // radius = b;

// }

// public class Constructor {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         int b = sc.nextInt();

//         Cylinder obj = new Cylinder(a, b);// jab constructor bnayega to value yha input krna padta h or setters ko hta
//         // obj.setheight(a);
//         // obj.setradius(b);
//         System.out.println(obj.getheight());
//         System.out.println(obj.getradius());
//         System.out.println(obj.cylinderradius(3, 4));
//         System.out.println(obj.cylindervolume(5, 6));

//     }
// }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class rectangle {
    private int length;
    private int breadth;

    public rectangle() {// constructor
        this.length = 4;
        this.breadth = 5;
    }

    public rectangle(int length, int breadth) {
        this.length = length;
        this.breadth = breadth;
    }

    public int getlength() {
        return length;
    }

    public int getbreadth() {
        return breadth;
    }

}

public class Constructor {
    public static void main(String[] args) {
        rectangle obj = new rectangle();// yha pr arg deta to custom wala constructor call hota

        System.err.println(obj.getlength());
        System.err.println(obj.getbreadth());
    }
}