import java.util.*;

public class Main {
    public static void main(String[] args) {
        Knapsack ks = new Knapsack();
        StringMatching sm = new StringMatching();
        SelectionSortAll ss = new SelectionSortAll();
        
        Scanner scan = new Scanner(System.in);
        boolean cond = true;

        System.out.println("Choose an Option\n1. Knapsack Problem\n2. Selection Sort\n3. Customer's Delivery\n4. String Matching\n5. Exit\n");

        while (cond) {
            System.out.print("Enter Option: ");
            int choice = scan.nextInt();
            scan.nextLine();  // Consume the newline

            switch (choice) {
                case 1: // Run Knapsack Problem
                    int capacity = 15;
                    String[] product = {"Canned Goods", "Cooking Oil", "Noodles", "Soap"};
                    int[] amount = {450, 725, 375, 500};
                    double[] weight = {5.00, 3.00, 2.50, 7.00};

                    KnapsackResult result = ks.knapsack(capacity, weight, amount, product);
                  
                    System.out.println("Maximum Amount: " + result.getMaxAmount());
                    System.out.println("Selected Items: ");
                    for (String item : result.getSelectedItems()) {
                        System.out.println("- " + item);
                    }
                    break;

                case 2: // Run Selection Sort
                	String[] productNames = {"Canned Goods", "Cooking Oil", "Noodles", "Soap"};
                    double[] weights = {5, 3, 2.5, 7};
                    int[] amounts = {450, 725, 375, 500};

                    ss.selectionSort(productNames, weights, amounts, "name");
                    ss.printResults("Sorted by Product Name:", productNames, weights, amounts);

                    ss.selectionSort(weights, productNames, amounts, "weight");
                    ss.printResults("Sorted by Weight:", productNames, weights, amounts);

                    ss.selectionSort(amounts, productNames, weights, "amount");
                    ss.printResults("Sorted by Amount:", productNames, weights, amounts);
                
                    break;

                case 3: // Placeholder for Customer's Delivery
                    System.out.println("Customer's Delivery Feature Coming Soon!");
                    break;

                case 4: // Run String Matching
                    System.out.print("Word to find: ");
                    String word = scan.nextLine();

                    System.out.print("Address: ");
                    String address = scan.nextLine();

                    sm.findWord(word, address);
                    break;

                case 5: // Exit Program
                    System.out.println("Program Terminated!");
                    cond = false;
                    break;

                default:
                    System.out.println("Invalid Option. Try Again.");
            }
        }

        scan.close();
    }
}
