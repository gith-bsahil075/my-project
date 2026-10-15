// // import java.util.Scanner;

// // class employee {
// //     String name;
// //     int salary;

// //     public int getsalary() {
// //         return salary;
// //     }

// //     public String getname() {
// //         return name;
// //     }

// //     public void setname(String n) {
// //         name = n;
// //     }

// // }

// // public class oopspractice {
// //     public static void main(String[] args) {
// //         employee obj = new employee();
// //         obj.name = "sahil";
// //         obj.salary = 1200;
// //         // employee obj1 = new employee();
// //         // obj.setname("rahul");
// //         System.out.println(obj.getname());
// //         System.out.println(obj.getsalary());

// //     }
// // }
// // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// class cellphone {
//     public void ringing() {
//         System.out.println("buzz");
//     }

//     public void vibrating() {
//         System.out.println("burrr...burrrr");
//     }
// }

// public class oopspractice {
//     public static void main(String[] args) {
//         cellphone obj = new cellphone();
//         obj.ringing();
//         obj.vibrating();
//     }
// }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// import java.util.Scanner;

// class square {
//     // public int square(int c) {
//     // int a;
//     // a = c * c;
//     // return a;}
//     int side;

//     public int area() {
//         return side * side;
//     }

//     public int perimeter() {
//         return 4 * side;
//     }

//     public int perimeter(int c) {
//         int e = 4 * c;
//         return e;
//     }
// }

// public class oopspractice {

//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int c = sc.nextInt();
//         square obj = new square();
//         System.out.println(obj.square(c));
//         System.out.println(obj.perimeter(c));

//     }
// }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////import 
// import java.util.Scanner;

// class employee {
//     int salary;
//     String name;

//     public int getsalary() {
//         return salary;
//     }

//     public void setsalary(int a) {
//         salary = a;
//     }

//     public void setname(String b) {
//         name = b;
//     }

//     public String getname() {
//         return name;
//     }

// }

// public class oopspractice {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         String b = sc.nextLine();

//         employee obj = new employee();
//         obj.setsalary(a);
//         obj.setname(b);
//         System.out.println(obj.getname());
//         System.out.println(obj.getsalary());
//     }

// }
//////////////////////////////////////////////////////////////////////////////////////////
// guess the number
// import java.util.Scanner;

// class guessthenumber {

//     public int num;

//     public guessthenumber() {

//         num = (int) (Math.random() * 100) + 1;
//         // System.out.println(num);
//     }

//     int number(int a) {
//         if (num == a) {
//             System.out.println("thats the guessing number");
//         } else if (a < num) {
//             System.out.println("the is small");
//         } else {
//             System.out.println("this is big");

//         }
//         return false;

//     }

// }

// public class oopspractice {

//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         guessthenumber obj = new guessthenumber();
//         // System.out.println("the guess value is ");
//         // System.out.println(obj.num);

//         if (a == obj.num) {
//             System.out.println("the guess no is correct");
//         } else if (a < obj.num) {
//             System.out.println("the value is small");
//         } else {
//             System.out.println("the value is big");
//         }
//         System.out.println(number(a));

//     }
// }



 //guess the number
import java.util.Scanner;

class guessthenumber {

    public int num;

    public guessthenumber() {

        num = (int) (Math.random() * 100) + 1;
        // System.out.println(num);
    }

    int number() {

        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        if (num == a) {
            System.out.println("thats the guessing number");
        } else if (a < num) {
            System.out.println("the is small");
        } else {
            System.out.println("this is big");

        }
        return number();

    }

}

public class oopspractice {

    public static void main(String[] args) {

        guessthenumber obj = new guessthenumber();
        // System.out.println("the guess value is ");
        // System.out.println(obj.num);

        // if (a == obj.num) {
        // System.out.println("the guess no is correct");
        // } else if (a < obj.num) {
        // System.out.println("the value is small");
        // } else {
        // System.out.println("the value is big");
        // }
        // System.out.println(number(a));
        obj.number();

    }
 }

