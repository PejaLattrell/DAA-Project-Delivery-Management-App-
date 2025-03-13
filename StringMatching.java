package DAA;
import java.util.ArrayList;
import java.util.Scanner;

public class StringMatching {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        
        System.out.print("Word to find: ");
        String word = input.nextLine();
        
        System.out.print("Address: ");
        String address = input.nextLine();
        
       
        findWord(word, address);
        
        input.close();
    }
    
    
    public static void findWord(String word, String address) {
        
        String wordLower = word.toLowerCase();
        String addressLower = address.toLowerCase();
        
        ArrayList<Integer> positions = new ArrayList<>();
        int pos = 0;
        int wordCount = 0;
        
        
        String[] words = addressLower.split("\\s+");
        
        
        for (int i = 0; i < words.length; i++) {
            
            String cleanWord = words[i].replaceAll("[^a-zA-Z0-9]", "");
            
            if (cleanWord.equals(wordLower)) {
                positions.add(i + 1); 
                wordCount++;
            }
        }
        
        
        System.out.println("No. of occurrences: " + wordCount);
        
        if (wordCount > 0) {
            System.out.print("Word's Position: ");
            for (int i = 0; i < positions.size(); i++) {
                System.out.print(positions.get(i));
                if (i < positions.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
}