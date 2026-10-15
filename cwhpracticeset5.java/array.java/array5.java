public class array5 {
    public static void main(String[] args) {
        int[][] matrix = { { 2, 3, 4 },
                { 5, 6, 7 } };
        int[][] matrix1 = { { 8, 9, 10, },
                { 11, 12, 13 } };
        int[][] result = { { 0, 0, 0 },
                { 0, 0, 0 } };
        // int sum = 0;
        // for (int i = 0; i < matrix.length; i++)// pure matrix ka length 2 hai 0 or 1
        // {
        // for (int j = 0; j < matrix[i].length; j++)// isme loo ke sign me dhyaan dena
        // // jb 0 rehega length me tb iska length 3 hojayga
        // // jo ki valid nh h kyuki index 0,1,2 se start hota h h isliye
        // // ab samjha chutiye
        // {

        // sum = sum + matrix[i][j];

        // }

        // }
        // System.out.print(sum);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.format("mtrix value i = %d is j = %d :", i, j);
                result[i][j] = matrix[i][j] + matrix1[i][j];

                System.out.println(result[i][j]);

            }

        }
    }

}
