import java.util.Arrays;  

public class Room {
    public static void main(String[] args) {
        try {
            System.out.println("Hello, World!");

            char[] alphabet = new char[26];

            // Populate the array with alphabets
            for (int i = 0; i < 26; i++) {
                alphabet[i] = (char) ('A' + i);
            }

            Table[] tables = new Table[1];

            for (int i=0; i<tables.length; i++) {
                int startIndex = i*5;
                char[] philosopherNames = Arrays.copyOfRange(alphabet, startIndex, startIndex+5);
                tables[i] = new Table(philosopherNames);
                Thread t = new Thread(tables[i]);
                t.start();
                //Thread.sleep(((int) (4 * 1000)));
            }

            
        } catch (Exception e) {
            System.out.println("Exiting error from Room class");
        }
        
    }
}