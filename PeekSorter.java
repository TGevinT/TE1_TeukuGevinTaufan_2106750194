import java.io.*;
import java.util.*;

public class PeekSorter{

    public static final boolean COUNT_MERGE_COSTS = true;

    public static long totalMergeCosts = 0;

    public static void mergeRuns(int[] A, int l, int m, int r, int[] B) {
        --m;
        int i, j;
        assert B.length >= r+1;
        if (COUNT_MERGE_COSTS) totalMergeCosts += (r-l+1);
        for (i = m+1; i > l; --i) B[i-1] = A[i-1];
        for (j = m; j < r; ++j) B[r+m-j] = A[j+1];
        for (int k = l; k <= r; ++k)
            A[k] = B[j] < B[i] ? B[j--] : B[i++];
    }

    public static void reverseRange(int[] a, int lo, int hi) {
        while (lo < hi) {
            int t = a[lo]; a[lo++] = a[hi]; a[hi--] = t;
        }
    }


    public static int extendWeaklyIncreasingRunLeft(final int[] A, int i, final int left) {
        while (i > left && A[i-1] <= A[i]) --i;
        return i;
    }

    public static int extendWeaklyIncreasingRunRight(final int[] A, int i, final int right) {
        while (i < right && A[i+1] >= A[i]) ++i;
        return i;
    }

    public static int extendStrictlyDecreasingRunLeft(final int[] A, int i, final int left) {
        while (i > left && A[i-1] > A[i]) --i;
        return i;
    }

    public static int extendStrictlyDecreasingRunRight(final int[] A, int i, final int right) {
        while (i < right && A[i+1] < A[i]) ++i;
        return i;
    }

    public PeekSorter() {
    }

    public void peeksort(final int[] a, final int l, final int r) {
        int n = r - l + 1;
        peeksort(a, l, r, l, r, new int[n]);
    }

    public static void peeksort(int[] A, int left, int right, int leftRunEnd, int rightRunStart, final int[] B) {
        if (leftRunEnd == right || rightRunStart == left) return;

        int mid = left + ((right - left) >> 1);
        if (mid <= leftRunEnd) {
            // |XXXXXXXX|XX     X|
            peeksort(A, leftRunEnd+1, right, leftRunEnd+1,rightRunStart, B);
            mergeRuns(A, left, leftRunEnd+1, right, B);
        } else if (mid >= rightRunStart) {
            // |XX     X|XXXXXXXX|
            peeksort(A, left, rightRunStart-1, leftRunEnd, rightRunStart-1, B);
            mergeRuns(A, left, rightRunStart, right, B);
        } else {
            // find middle run
            final int i, j;
            if (A[mid] <= A[mid+1]) {
                i = extendWeaklyIncreasingRunLeft(A, mid, left == leftRunEnd ? left : leftRunEnd+1);
                j = mid+1 == rightRunStart ? mid : extendWeaklyIncreasingRunRight(A, mid+1, right == rightRunStart ? right : rightRunStart-1);
            } else {
                i = extendStrictlyDecreasingRunLeft(A, mid, left == leftRunEnd ? left : leftRunEnd+1);
                j = mid+1 == rightRunStart ? mid : extendStrictlyDecreasingRunRight(A, mid+1,right == rightRunStart ? right : rightRunStart-1);
                reverseRange(A, i, j);
            }
            if (i == left && j == right) return;
            if (mid - i < j - mid) {
                // |XX     x|xxxx   X|
                peeksort(A, left, i-1, leftRunEnd, i-1, B);
                peeksort(A, i, right, j, rightRunStart, B);
                mergeRuns(A,left, i, right, B);
            } else {
                // |XX   xxx|x      X|
                peeksort(A, left, j, leftRunEnd, i, B);
                peeksort(A, j+1, right, j+1, rightRunStart, B);
                mergeRuns(A,left, j+1, right, B);
            }
        }
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
            pw.printf("start Memory: %d%n", startMemory);
            pw.printf("end Memory: %d%n", endMemory);
            pw.printf("start time: %d%n", startTime);
            pw.printf("end time: %d%n" , endTime);

        } catch (IOException e) {
            System.err.println("Error writing to the output file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        String filename = "reversed_100000"; // The file name
        String outputFilename = "peek_sorter_reversed_100000.txt";
        long startMemory, endMemory;
        long startTime, endTime;

       
        int[] sortedArray = readNumbersFromFile(filename);

        PeekSorter sorter = new PeekSorter();
                
        startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        startTime = System.nanoTime();
        sorter.peeksort(sortedArray, 0, sortedArray.length - 1);
        endTime = System.nanoTime();
        endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        writeNumbersToFile(outputFilename, sortedArray,startMemory, endMemory, startMemory, endTime);

        System.out.printf("%s%n", outputFilename);
        System.out.printf("start Memory: %d%n",startMemory);
        System.out.printf("end Memory: %d%n", endMemory);
        System.out.printf("start time: %d%n", startTime);
        System.out.printf("end time: %d%n" , endTime);
    }
}
