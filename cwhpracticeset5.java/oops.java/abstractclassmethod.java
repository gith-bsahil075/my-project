 abstract class base{

public base(){
    System.out.println("i,m a base constructor");
}
public void sayhello(){
    System.out.println("hello");
}
abstract public void greet();
abstract public void greet1();



}
class child extends base{//ya to abstract class bna le ya fir abstract method choose krle
    @Override
    public void greet(){
        System.out.println("good morning");
    }
    @Override
    public void greet1(){
        System.out.println("good afternoon");
    }

    

}
abstract class child1 extends base{
    public void th(){
        System.out.println("i am child class of base");
    }
}
 


public class abstractclassmethod
{
    public static void main(String[] args){//ek abstract method pure class ko abstract bna deta h
         //base c = new base();//abstract class ka object nhi bna sakte h
         child c = new child();
         c.greet();
         c.greet1();
    }
    
}
