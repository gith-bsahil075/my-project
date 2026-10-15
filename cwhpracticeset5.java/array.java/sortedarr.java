
public class sortedarr {
    public static void main(String[] args)

    {
        int[] array = { 1, 2, 8, 9, 10, 12, 12, 23, 33 };
        boolean isarray = true;
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                isarray = false;
                break;

            }
        }
        if (isarray) {
            System.out.println("the array is sorted");
        } else {
            System.out.println("the array is not sorted");
        }

    }
}