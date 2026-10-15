import java.util.Scanner;

class employee {
    int id;
    String name;
    int salary;

    public void details() {
        System.out.println("my id is " + id);
        System.out.println("my name is" + name);
        System.out.println("my salary is" + salary);
    }

    public int getsalary() {
        return salary;
    }
}

public class classoblect {

    public static void main(String[] args) {
        employee obj = new employee();
        employee obj2 = new employee();
        obj.id = 12;
        obj.name = "sahil";
        obj.salary = 12000; // setting a attribute
        // System.out.println(obj.id);
        // System.out.println(obj.name);
        obj2.id = 34;
        obj2.name = "sagar";
        obj2.salary = 3400;

        obj.details();
        obj2.details();
        int salary = obj.getsalary();
        int salary2 = obj2.getsalary();
        System.out.println(salary);
        System.out.println(salary2);

    }

}
