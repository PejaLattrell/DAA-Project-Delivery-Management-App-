import java.util.Scanner;

public class ArrayPartitioning {
    // Partition the array around a pivot and return the pivot's final position
    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];  // Choose the rightmost element as pivot
        int i = left - 1;  // Index of the smaller element

        // Move elements smaller than pivot to the left
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        // Place pivot in its final position
        swap(arr, i + 1, right);
        return i + 1;
    }

    // Helper method to swap two elements in the array
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Recursive QuickSelect to find the k-th smallest element (0-based index)
    private static int quickSelect(int[] arr, int left, int right, int k) {
        if (left == right) {  // Base case: only one element
            return arr[left];
        }

        // Partition and get the pivot's position
        int pivotIdx = partition(arr, left, right);

        // Compare pivot position with k
        if (k == pivotIdx) {
            return arr[k];
        } else if (k < pivotIdx) {
            // Recurse on the left partition
            return quickSelect(arr, left, pivotIdx - 1, k);
        } else {
            // Recurse on the right partition
            return quickSelect(arr, pivotIdx + 1, right, k);
        }
    }

    // Public method to find the k-th smallest element (1-based index for user)
    public static int findKthSmallest(int[] arr, int k) {
        if (arr == null || k < 1 || k > arr.length) {
            throw new IllegalArgumentException("Invalid input: k must be between 1 and array length");
        }
        // Convert to 0-based index for internal use
        return quickSelect(arr, 0, arr.length - 1, k - 1);
    }

    // Main method with user input
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get array size from user
        System.out.print("Enter the number of elements in the array: ");
        int n;
        try {
            n = scanner.nextInt();
            if (n <= 0) {
                throw new IllegalArgumentException("Array size must be positive");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a positive integer.");
            scanner.close();
            return;
        }

        // Initialize array and get elements from user
        int[] arr = new int[n];
        System.out.println("Enter " + n + " integers:");
        for (int i = 0; i < n; i++) {
            try {
                arr[i] = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter integers only.");
                scanner.close();
                return;
            }
        }

        // Get k from user
        System.out.print("Enter the value of k (1 to " + n + ") to find the k-th smallest element: ");
        int k;
        try {
            k = scanner.nextInt();
            if (k < 1 || k > n) {
                throw new IllegalArgumentException("k must be between 1 and " + n);
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a valid integer for k.");
            scanner.close();
            return;
        }

        // Display original array
        System.out.print("Original array: ");
        printArray(arr);

        // Find and display the k-th smallest element
        int result = findKthSmallest(arr, k);
        System.out.println("The " + k + "th smallest element is: " + result);

        // Display modified array
        System.out.print("Modified array: ");
        printArray(arr);

        scanner.close();
    }

    // Helper method to print the array
    private static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}