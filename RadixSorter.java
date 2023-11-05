// Radix Sort in Java Programming

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RadixSorter {

    // Using counting sort to sort the elements in the basis of significant places
    void countingSort(int array[], int size, int place) {
        int[] output = new int[size + 1];
        int max = array[0];
        for (int i = 1; i < size; i++) {
        if (array[i] > max)
            max = array[i];
        }
        int[] count = new int[max + 1];

        for (int i = 0; i < max; ++i)
        count[i] = 0;

        // Calculate count of elements
        for (int i = 0; i < size; i++)
        count[(array[i] / place) % 10]++;

        // Calculate cumulative count
        for (int i = 1; i < 10; i++)
        count[i] += count[i - 1];

        // Place the elements in sorted order
        for (int i = size - 1; i >= 0; i--) {
        output[count[(array[i] / place) % 10] - 1] = array[i];
        count[(array[i] / place) % 10]--;
        }

        for (int i = 0; i < size; i++)
        array[i] = output[i];
    }

    // Function to get the largest element from an array
    int getMax(int array[], int n) {
        int max = array[0];
        for (int i = 1; i < n; i++)
        if (array[i] > max)
            max = array[i];
        return max;
    }

    // Main function to implement radix sort
    void radixSort(int array[], int size) {
        // Get maximum element
        int max = getMax(array, size);

        // Apply counting sort to sort elements based on place value.
        for (int place = 1; max / place > 0; place *= 10)
        countingSort(array, size, place);
    }

    public static int[] readNumbersFromFile(String filename) {
        List<Integer> numbersList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    int number = Integer.parseInt(line);
                    numbersList.add(number);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping non-integer value: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        Integer[] numbersArray = numbersList.toArray(new Integer[0]);
        int[] numbersIntArray = Arrays.stream(numbersArray).mapToInt(Integer::intValue).toArray();

        return numbersIntArray;
    }

   // Function to write an array of numbers to a file
   public static void writeNumbersToFile(String filename, int[] numbers, long startMemory, long endMemory, long startTime, long endTime) {
    try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
        for (int number : numbers) {
            pw.println(number);
        }
        pw.printf("%nstart Memory: %d%n", startMemory);
        pw.printf("end Memory: %d%n", endMemory);
        pw.printf("start time: %d%n", startTime);
        pw.printf("end time: %d%n" , endTime);

    } catch (IOException e) {
        System.err.println("Error writing to the output file: " + e.getMessage());
    }
}

    public static void main(String[] args) {
        List<String> filenames = new ArrayList<>(Arrays.asList("sorted_1000", "sorted_10000", "sorted_100000", "random_1000",
        "random_10000", "random_100000", "reversed_1000", "reversed_10000", "reversed_100000" ));
        for (String file : filenames ){
            String filename = "dataset_input/" + file; // The file name
            String outputFilename = "output_radixsort/radix_sorter_" + file + ".txt";
            long startMemory, endMemory;
            long startTime, endTime;
        
            int[] sortedArray = readNumbersFromFile(filename);
            int size = sortedArray.length;

            RadixSorter rs = new RadixSorter();
                    
            startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            startTime = System.nanoTime();
            rs.radixSort(sortedArray, size);
            endTime = System.nanoTime();
            endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            writeNumbersToFile(outputFilename, sortedArray,startMemory, endMemory, startTime, endTime);

            System.out.printf("%n%s%n", outputFilename);
            System.out.printf("start Memory: %d%n",startMemory);
            System.out.printf("end Memory: %d%n", endMemory);
            System.out.printf("start time: %d%n", startTime);
            System.out.printf("end time: %d%n" , endTime);
        }
    }

}