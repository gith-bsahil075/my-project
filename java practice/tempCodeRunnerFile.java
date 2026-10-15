// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         int[][] array = { { 1, 2, 3 }, { 3, 4, 5 } };
//         int[][] array1 = { { 6, 7, 8 }, { 9, 10, 11 } };
//         int[][] array2 = { { 0, 0, 0 }, { 0, 0, 0 } };
//         for (int i = 0; i < array.length; i++) {
//             for (int j = 0; j < array[i].length; j++) {
//                 array2[i][j] = array1[i][j] + array[i][j];
//                 System.out.println(array2[i][j]);

//             }
//         }
//     }
// }
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// max in array 

// public class javabasic {
//     public static void main(String[] args) {
//         int[] array = { 1, 2, 3, 455, 67, 78 };
//         boolean isarray = true;
//         int max = 0;
//         for (int i = 0; i < array.length; i++) {
//             if (array[i] > max) {
//                 max = array[i];
//             }
//         }
//         System.out.println(max);
//     }
// }
// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// sorted array
// public class javabasic {
//     public static void main(String[] args) {
//         int[] arr = { 1, 2, 3, 4, 6, 7, 8, 9 };
//         boolean isarray = true;
//         for (int i = 0; i < arr.length - 1; i++) {// yha array length 8 h
//             if (arr[i] < arr[i + 1]) {
//                 isarray = false;
//                 break;
//             }

//         }
//         if (isarray)// true
//         {
//             System.out.println("array is  sorted");
//         } else {
//             System.out.println("array is not  sorted");
//         }
//         // System.out.println(arr.length);

//     }
// }
// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         int b = sc.nextInt();
//         int c = sc.nextInt();
//         System.out.println("the sum of three numbers" + a + b + c);
//     }
// }
// ////////////////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int maths = sc.nextInt();
//         int physics = sc.nextInt();
//         int chemistry = sc.nextInt();
//         int cgpa = maths + physics + chemistry / 300 * 100;

//         System.out.println(cgpa + "%");
//         ;
//     }
// }
// /////////////////////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] java) {
//         Scanner sc = new Scanner(System.in);
//         int km = sc.nextInt();
//         System.out.println("the miles" + km * 621371);

//     }
// }
// ///////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         // int num = sc.nextInt();
//         // if (num < 0 || num > 0) {
//         // System.out.println("the num is integer");

//         // } else {
//         // System.out.println("the number is not integer");
//         // }
//         System.out.println(sc.hasNextInt());// hasnextInt ye check krta h ki input num integer h ya nhi
// //bina scanner library ke ye run hi nhi hoga
//     }
// }
// /////////////////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         // Scanner sc = new Scanner(System.in);
//         // int a = sc.nextInt();
//         float operation = 7 / 4.0f * 9 / 2.0f;//7/4*9/2
//         System.out.println(operation);
//     }

// }
// //////////////////////////////////////////////////////////////
// incryt decrypt
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         char grad = 'b';// string me "" isko nhi ' isko use krte h
//         grad = (char) (grad + 8);
//         System.out.println(grad);
//         grad = (char) (grad - 8);
//         System.out.println(grad);
//     }
// }
// ////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         int s = sc.nextInt();
//         int v = sc.nextInt();
//         int u = sc.nextInt();
//         int program = (v * v - u * u) / (2 * a * s);// math.pow(v,2)
//         System.out.println(program);

//     }
// }
// ////////////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
// public static void main(String[] args) {
// Scanner sc = new Scanner(System.in);
// int a = sc.nextInt();
// System.out.println(a > 10);

// }
// }
// ////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner ch = new Scanner(System.in);
//         String name = ch.nextLine();// char single word store krta h

//         System.out.println(name.toUpperCase());
//         String a = "to the sahil";
//         a = a.replace(" ", "_");
//         System.out.println(a);
//         String letter = "Dear <|name|> ,thanks a lot!";
//         letter = letter.replace("<|name|>", "harry");
//         System.out.println(letter);
//         String mystring = "this string contains  double and triple saces";// agar 3 sace nhi h to -1 dikhayga
//         System.out.println(mystring.indexOf("  "));
//         System.out.println(mystring.indexOf("   "));
//         String sahil = "dear harry,\n\t this java course is nice. \n Thanks!";
//         System.out.println(sahil);

//     }
// }
// ///////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int physics = sc.nextInt();
//         int chemistry = sc.nextInt();
//         int maths = sc.nextInt();
//         int hindi = sc.nextInt();
//         int english = sc.nextInt();
//         int grand_total = physics + chemistry + maths + hindi + english;
//         int passing_marks = 33;
//         if (physics > 33 && chemistry > 33 && maths > 33 && hindi > 33 && english > 33) {
//             if (grand_total > 40) {
//                 System.out.println("student pass in  exam");
//             } else {
//                 System.out.println("student doesn,t pass in exam");
//             }

//         } else {
//             System.out.println("student doesn,t pass in each exam");
//         }
//     }
// }
// /////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int income = sc.nextInt();
//         // int vinod = sc.nextInt();
//         // int shyam = sc.nextInt();
//         int tax;
//         if (250000 < income && income <=500000);{ 
//             tax = income * 5 / 100;
//             System.out.println(tax);

//         } else if (500000 < income && income <=1000000) {
//             tax = income * 20 / 100;
//             System.out.println(tax);

//         } else if (1000000<=income) {
//             tax = income * 30 / 100;
//             System.out.println("the tax given by the employee to the government is " + tax);
//         } else {
//             System.out.println("the salary is under the 250000 \n the tax is 0");

//         }

//     }
// }
// /////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int day = sc.nextInt();
//         switch (day) {
//             case 1:
//                 System.out.println("today is monday");
//                 break;
//             case 2:
//                 System.out.println("today is tuesday");
//                 break;
//             case 3:
//                 System.out.println(" today is wednesday");
//                 break;
//             case 4:
//                 System.out.println("today thursday");
//                 break;
//             case 5:
//                 System.out.println(" today is friday");
//                 break;
//             case 6:
//                 System.out.println("today is saturday");
//                 break;
//             default:
//                 System.out.println("today is sunday");
//         }
//     }
// }
// ////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         String website = sc.nextLine();
//         if (website.endsWith(".com")) {
//             System.out.println("this is commercial website");

//         } else if (website.endsWith(".org")) {
//             System.out.println("this is an organization website");
//         } else if (website.endsWith(".in")) {
//             System.out.println("this is indian website");
//         }

//     }
// }
// ///////////////////////////////////////////////////////
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
// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////import 
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
//       obj.setsalary(a);
//         obj.setname(b);
//         System.out.println(obj.getname());
//         System.out.println(obj.getsalary());
//     }

// }
// ////////////////////////////////////////////////////////////////////////////////////////
// guess the number
// import java.util.Scanner;

// class guessthenumber {

//     public int num;

//     public guessthenumber() {

//         num = (int) (Math.random() * 100) + 1;
//         // System.out.println(num);
//     }

//     int number() {

//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         if (num == a) {
//             System.out.println("thats the guessing number");
//         } else if (a < num) {
//             System.out.println("the is small");
//         } else {
//             System.out.println("this is big");

//         }
//         return number();

//     }

// }

// public class javabasic {

//     public static void main(String[] args) {

//         guessthenumber obj = new guessthenumber();
//         // System.out.println("the guess value is ");
//         // System.out.println(obj.num);

//         // if (a == obj.num) {
//         // System.out.println("the guess no is correct");
//         // } else if (a < obj.num) {
//         // System.out.println("the value is small");
//         // } else {
//         // System.out.println("the value is big");
//         // }
//         // System.out.println(number(a));
//         obj.number();

//     }
// }
// ///////////////////////////////////////////
// practice set 5//
// import java.util.Scanner;//star pattern
// public class javabasic{
//     public static void main(String[] args){
//         Scanner sc = new Scanner(System.in);
//         int n = sc.nextInt();
//         for(int i = 0;i<n;i++){
//             for(int j = i; j<n-i;j++){
//                 System.out.printf("*");
//             }
//             System.out.println();

//         }

//     }
// } 
// /////////////////////////////////////////
// import java.util.Scanner;
// public class javabasic{
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int num = sc.nextInt();
//         int a = 0;
//         int i = 0;
//         while(i<= num){

//             a = a+2*i;
//              i++;
            
//         }
       
//         System.out.println(a);
//     }
// } 
// //////////////////////////////////
// import java.util.Scanner;
// public class javabasic{
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int num = sc.nextInt();
//         for(int i = 0; i<=10;i++){
//             System.out.printf("num %d * %d is %d ", num,i,  num*i);
//             System.out.println();
            
//         }

        
//     }
// }
// /////////////////////////////////////////////////
// import java.util.Scanner;
// public class javabasic{
//     public static void main(String[] args){
//         Scanner sc = new Scanner(System.in);
//         int num = sc.nextInt();
//         for(int i = 10;i>0;i--){
//             System.out.printf("the %d is %d is %d ",num,i,num*i);
// System.out.println();
//         }

//     }
// }
// ///////////////////////////////
// import java.util.Scanner;
// public class javabasic{
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         int b = 1;
//         for(int i = 0;i<a;i++){
//             b = b*(a-i);
            
// System.out.println(b);

//         }
        
        
//     }
// }
// ////////////////////////////////////////
// import java.util.Scanner;
// public class javabasic{
//     public static void main(String[] args){
//         Scanner sc = new Scanner(System.in);
//         int a = sc.nextInt();
//         int b = 0;
        
//         for(int i = 1; i<=10;i++){
//             b = b+(a*i);

//         }
//         System.out.println(b);

//     }
 
// }
// ///////////////////////////////////////////
// import java.util.Scanner;
// class year1{ 
//     int leapyear(){ 
//          Scanner sc = new Scanner(System.in);
//         int year = sc.nextInt();

//         if(year%400 == 0||(year%4 == 0 && year%100 != 0)){//agar 100 se divicible h to leap year nhi h
//             System.out.printf("this year is leap year", year);// yha year%100 != 0; yaad rakhna cond ku hua
            
            
//         }
//         else{
//             System.out.println("this is not leap year");
//         }
//         return leapyear();
//     }
// }
    


// public class javabasic{
//     public static void main(String[] args) {
         
//         year1 obj = new year1();
//         obj.leapyear();
       
//     }
// }
// ///////////////////////////////////////////////
//rock paper scissor
// import java.util.Scanner;
// class game{
//     public int paari;
//      int rps(){
//         paari = (int)(Math.random()*3)+1;
//         Scanner sc =new Scanner(System.in);
//         String yourpaari = sc.nextLine().toLowerCase();//ya to isme ye bhi kr sakta h ki computer wala print hi mt kra 
//     //    String rock = "Rock";
//     //   String paper = "Paper";
//     //    String scissor = "Scissor";
//        String computer = "";

//         if(paari == 3){
//            computer = "rock";
//         }
//         else if(paari == 2){
//             computer = "paper";
//         }
//         else if(paari == 1){
//             computer = "scissor";
//         }
//         System.out.println(computer.toLowerCase());
//         if(yourpaari.equals(computer)){
//             System.out.println("mai jeet gya");

//         }
//         else{
//             System.out.println("mai haar gya");
//         }
       
//         return rps();
//      }

// }
// public class javabasic{
//     public static void main(String[] args) {
//         game sc = new game();
//         sc.rps();
//     }
// }
/////////////////////////////////////////////////////////////////
  //arrays  
//  public class javabasic{
//     public static void main(String[] args) {
//         int[] array = {1,2,3,4,5,6,7,99};
//         int sum = 0;
//         for(int arr : array){
//             sum = sum+arr;
//         }
//         System.out.println(sum);
        
//     }
// }
////////////////////////////////////////
// public class javabasic{
//     public static void main(String[] args){
//         float[] array = {1.8f,1.9f,1.7f,1.6f,1.5f,1.4f};//agar float bna rha h to ".f" lgana bhut jruri h nhi to wo double hojayga
//         float target = 1.2f;
//         boolean isinarray = false;
//         for(int i = 0; i<array.length;i++){
//             if(array[i] == target){
//                 isinarray = true;
                
//                 break;
//             }
        

            
//         }
//         if(isinarray){
//             System.out.println("this is in array");
//         }
//         else{
//             System.out.println("this is not in array");
//         }
//     }

// }
///////////////////////////////////
// public class javabasic{
//     public static void main(String[] args){
//         int[] array = {1,2,3,4,5,6,7,8,99};
//         int sum = 0;

         
//         for(int arr : array){
//             sum = sum+arr;
            

            
//              }
//              int avg = sum/array.length;
//              System.out.println(avg);
        
//     }
// }
/////////////////////////////////////////////////////////
// public class javabasic{
//     public static void main(String[] args){
//         int [][] arrays = {{1,2,3,4,5,6,7},{8,9,10,11,12,13,14}};
        
//         for(int i = 0;i<arrays.length;i++){
//             for(int j = 0;j<arrays[i].length;j++){
//                 System.out.print(arrays[i][j]);
//             }
//             System.out.println();
//         }
//     }
// }
//////////////////////////////////
//  public class javabasic{
//     public static void main(String[] args) {
//         int [][]arrays = {{1,2,3,4},{7,8,9,11}};
//         int[][] arrays1 = {{11,22,33,44},{55,66,77,88}};
//         int[][] arrays2 = {{0,0,0,0},{0,0,0,0}};
//         for(int i = 0;i<arrays.length;i++){
//             for(int j = 0;j<arrays[i].length;j++){
//                 arrays2[i][j] = arrays[i][j]+arrays1[i][j];
//                 System.out.println("the row " + i + " and column " + j + " is " + arrays2[i][j]);
//             }
            
//         }
        
//     }
// }
 

////////////////////////////////////////
//reverse

// public class javabasic{
//     public static void main(String[] args) {
//         int[] array = {1,2,3,4,5,6,7,8,9};
//         int med = array.length;
//         int div = Math.floorDiv(med,2);
//         int temp;
//         for(int i =0;i<div;i++){
//             temp = array[i];
//             array[i] = array[med-i-1];
//             array[med-i-1] = temp;
             
//         }
//         for(int element : array ){
//             System.out.print(element+" ");
//         }
       
        
//     }
// }
/////////////////////////////////////////////////////////
// public class javabasic{
//   public static void main(String[] args){
//     int [] array = {1,2,3,4,5,6,7};
//     int l = array.length;
//     int avg = Math.floorDiv(l,2);
//     int temp;
//     for(int i = 0;i<avg;i++){
//   temp = array[i];
//   array[i] = array[l-1-i];
//   array[l-1-i] = temp;

      
//     }
//     for(int element : array){
//       System.out.print(element);
//     }
//   }
// }
////////////////////////////////////////////////////////////

// public class javabasic{
//   public static void main(String[] args){
//     int[] array = {1,2,3,4,55,6,7};
//     int max = array[0];
//     for(int i = 1;i<array.length;i++){
//       if(array[i] > max){
//         max = array[i];
//       }
//     }
//     System.out.println(max);
//   }
// }
/////////////////////////////////////////////
// public class javabasic{ 
// public static void main(String[] args){
//   int[] array = {1,2,33,5,6,7};
//   boolean isinarray = true;
//   for(int i = 0;i<array.length-1;i++){
//     if(array[i]> array[i+1]){
//       isinarray = false;
//       break;
//     }
//   }
//   if(isinarray){
//     System.out.println("the array is sorted");

//   }
//   else{
//     System.out.println("the array is not sorted");
//   }
// }
// }

////////////////////////////////////////
// public class javabasic{
//   public static int sum(int... arr){
//     int result = 0;
//     for(int b : arr){
//       result = result+b;
//     }
//     return result;
//   }
//   public static void main(String[] args){
//     System.out.println("the sum is " + sum(7,3,4,5,5,6,6,6,7,7,7,8,8,8,8,8,8,8,8,8));

//   }
// }
///////////////////////////////////////////////////////
// public class javabasic{
//   public static void main(String[] args){
//     int[] array = {1,2,3,44,5,6,7,88};
//     int target = 45;
//     boolean isinarray = false;
//     for(int i = 0;i<array.length;i++){
//       if(target == array[i]){
//         isinarray = true;
//         break;
//       }
//     }
//     if(isinarray){
//       System.out.println("num is in array");
//     }
//     else{
//       System.out.println("num is not in array");
//     }
//   }
// }
/////////////////////////////////////////////////////////////////////
//second maximum
// public class javabasic{
//   public static void main(String[] args){
//     int[] array = {1,2,3,4,5,66,7,98};
//     int largest = Integer.MIN_VALUE;//integer.min_value
//     int secondlargest = Integer.MIN_VALUE;
    
//     for(int i = 1;i<array.length;i++){
//       if(array[i]>largest){
//         secondlargest = largest;
//         largest = array[i];

//       }
//       else if(array[i]>secondlargest&& array[i] != largest){
//         secondlargest = array[i];
//       }
//     }
//     System.out.println(secondlargest);
    

//   }
// }
///////////////////////////////////
// public class javabasic{
//   public static void main(String[] args){
//     int[] arrays = {1,2,3,44,5,66,7};
//     int largest = Integer.MIN_VALUE;
//     int secondlargest = Integer.MIN_VALUE;
//     for(int i = 1; i<arrays.length;i++){
//       if(arrays[i]>largest){
//         secondlargest = largest;
//         largest = arrays[i];
         


//       }
//       else if(arrays[i]>secondlargest&&arrays[i] != largest){
//         secondlargest = arrays[i];
//       }
      
//     }
//     System.out.println(secondlargest);
   
//   }
// }
///////////////////////////////////////////////
// import java.util.Scanner;

// public class javabasic{
//   static int recursion(int n){
//     if(n == 0 || n == 1)
//     {
//       return 1;
//     }else {
//       return n*recursion(n-1);

//     }


//   }
  
//   public static void main(String[] args) {
//     Scanner sc  = new Scanner(System.in);
//      int n = sc.nextInt();
//      System.out.println(recursion(n));
      
//   }
// }
///////////////////////////////////
//rotate an array by 1
public class javabasic{
  public static void main(String[] args){
    int[] arrays = {1,2,3,4,5,6,7,8};
    int l = arrays.length;
    int temp = arrays[l-1];
    for(int i = 0;i>0;i++ ){
      arrays[i] = arrays[l-1];

    }
    arrays[0] = temp;
    for(int element : arrays){
      System.out.print(element+" ");
    }
  }
}