
public class KnapsackResult {
	 private int maxAmount;
	    private String[] selectedItems;

	    public KnapsackResult(int maxAmount, String[] selectedItems) {
	        this.maxAmount = maxAmount;
	        this.selectedItems = selectedItems;
	    }

	    public int getMaxAmount() {
	        return maxAmount;
	    }

	    public String[] getSelectedItems() {
	        return selectedItems;
	    }
	}

