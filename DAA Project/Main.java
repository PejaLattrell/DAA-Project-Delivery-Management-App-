import java.util.*;



public class Main {

	public static void main(String[] args) 
	{
		Knapsack ks = new Knapsack();


		Scanner scan = new Scanner(System.in);
		boolean cond = true;
		
		System.out.println("Choose an Option\n1.Knapsack Problem\n2.Selection Sort\n3.Customer's delivery\n4.String Matching\n5.Exit\n");
		
		while(cond) {
			
			System.out.print("Enter Option: ");
			int choice = scan.nextInt();
			
			switch (choice) {
			
			case 1: //Run Knapsack Problem
				 int capacity = 15;

				 String product[] = {"Canned Goods", "Cooking Oil", "Noodles", "Soap"};

				 int amount [] = {450, 725, 375, 500};
				 double weight [] = {5.00, 3.00, 2.50, 7};



				 




				 
				 
				 break;

			case 2: //Run Selection Sort
				break;

			case 3: //Run String Delivery
				break;

			case 4: //Run String Matching

				break;		
			case 5: //Exit Program
				
				System.out.println("Program Terminated!");
				cond = false;
			
			
			}
				
		}
		
		scan.close();
	}

}
