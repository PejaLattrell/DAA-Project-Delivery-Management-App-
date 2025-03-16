
import java.util.*;

public class Knapsack {

	 public KnapsackResult knapsack(int capacity, double[] weights, int[] values, String[] products) {
	        int n = products.length;
	        int maxAmount = 0;
	        List<String> selectedItemsList = new ArrayList<>();

	        for (int i = 0; i < (1 << n); i++) {
	            int currentAmount = 0;
	            double currentWeight = 0;
	            List<String> currentItems = new ArrayList<>();

	            for (int j = 0; j < n; j++) {
	                if ((i & (1 << j)) > 0) {
	                    currentAmount += values[j];
	                    currentWeight += weights[j];
	                    currentItems.add(products[j]);
	                }
	            }

	            if (currentWeight <= capacity && currentAmount > maxAmount) {
	                maxAmount = currentAmount;
	                selectedItemsList = new ArrayList<>(currentItems);
	            }
	        }

	        String[] selectedItems = selectedItemsList.toArray(new String[0]);
	        return new KnapsackResult(maxAmount, selectedItems);
	    }
}
