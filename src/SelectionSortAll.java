
public class SelectionSortAll {
	
	 public static void selectionSort(String[] arr, double[] weights, int[] amounts, String sortBy) {
	        int n = arr.length;
	        for (int i = 0; i < n - 1; i++) {
	            int minIndex = i;
	            for (int j = i + 1; j < n; j++) {
	                if (arr[j].compareTo(arr[minIndex]) < 0) {
	                    minIndex = j;
	                }
	            }
	            swap(arr, i, minIndex);
	            swap(weights, i, minIndex);
	            swap(amounts, i, minIndex);
	        }
	    }

	    public static void selectionSort(double[] arr, String[] names, int[] amounts, String sortBy) {
	        int n = arr.length;
	        for (int i = 0; i < n - 1; i++) {
	            int minIndex = i;
	            for (int j = i + 1; j < n; j++) {
	                if (arr[j] < arr[minIndex]) {
	                    minIndex = j;
	                }
	            }
	            swap(arr, i, minIndex);
	            swap(names, i, minIndex);
	            swap(amounts, i, minIndex);
	        }
	    }

	    public static void selectionSort(int[] arr, String[] names, double[] weights, String sortBy) {
	        int n = arr.length;
	        for (int i = 0; i < n - 1; i++) {
	            int minIndex = i;
	            for (int j = i + 1; j < n; j++) {
	                if (arr[j] < arr[minIndex]) {
	                    minIndex = j;
	                }
	            }
	            swap(arr, i, minIndex);
	            swap(names, i, minIndex);
	            swap(weights, i, minIndex);
	        }
	    }

	    public static void swap(String[] arr, int i, int j) {
	        String temp = arr[i];
	        arr[i] = arr[j];
	        arr[j] = temp;
	    }

	    public static void swap(double[] arr, int i, int j) {
	        double temp = arr[i];
	        arr[i] = arr[j];
	        arr[j] = temp;
	    }

	    public static void swap(int[] arr, int i, int j) {
	        int temp = arr[i];
	        arr[i] = arr[j];
	        arr[j] = temp;
	    }

	    public static void printResults(String message, String[] names, double[] weights, int[] amounts) {
	        System.out.println(message);
	        System.out.println("Product Name\tWeight\tAmount");
	        for (int i = 0; i < names.length; i++) {
	            System.out.println(names[i] + "\t\t" + weights[i] + "\t" + amounts[i]);
	        }
	        System.out.println();
	    }
}
