import java.util.ArrayList;
import java.util.List;

public class CombinationGenerator {
    public static void main(String[] args) {
        List<Boolean> result = generateCombinations(new int[]{1,1,1}, new boolean[6]);
        System.out.println(result);
    }

    public static List<Boolean> generateCombinations(int[] counts, boolean[] array) {
        List<Boolean> combinations = new ArrayList<>();
        generateCombinationsHelper(counts, 0, array, combinations);
        return combinations;
    }

    private static void generateCombinationsHelper(int[] counts, int index, boolean[] array, List<Boolean> combinations) {
        if (index == counts.length) {
            for (boolean value : array) {
                combinations.add(value);
            }
            return;
        }

        for (int i = 0; i <= array.length - counts[index]; i++) {
            boolean validCombination = true;

            // Check if there are no consecutive false values
            for (int j = i; j < i + counts[index]; j++) {
                if (array[j] || (j>0 && array[j-1]) ) {
                    validCombination = false;
                    break;
                }
            }

            if (validCombination) {
                for (int j = i; j < i + counts[index]; j++) {
                    array[j] = true;
                }
                generateCombinationsHelper(counts, index + 1, array, combinations);
                for (int j = i; j < i + counts[index]; j++) {
                    array[j] = false;
                }
            }
        }
    }
}