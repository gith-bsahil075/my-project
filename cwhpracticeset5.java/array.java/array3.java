public class array3 {
    public static void main(String[] args) {
        int[] physic = { 35, 35, 37, 48, 56 };

        int totalsum = 0;
        for (int marks : physic) {
            totalsum = totalsum + marks;

        }
        float avg = totalsum / physic.length;
        System.out.println(avg);
    }

}
