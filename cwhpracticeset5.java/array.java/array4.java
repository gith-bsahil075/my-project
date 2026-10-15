public class array4 {
    public static void main(String[] args) {
        int[][] array = { { 1, 2, 3 }, { 6, 7, 9 } };

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

}
 