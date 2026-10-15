public class array7maxmin {
    public static void main(String[] args) {
        int[] array = { 1, 2, 3, 455, 6, 1207 };
        int max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        System.out.print(max);

    }
}
