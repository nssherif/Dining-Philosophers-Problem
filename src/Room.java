import java.util.Arrays;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

public class Room {
    static int timer = 0;
    private static List<Table> rooomTables = new ArrayList<Table>();
    private static List<Character> movedPhilosophers = new ArrayList<Character>();

    // Helper method to find the Thread object based on the thread ID
    private static Thread findThreadById(long threadId) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getId() == threadId) {
                return thread;
            }
        }
        return null;
    }

    // Helper method to find the Philosopher object based on the Thread object
    private static Philosopher findPhilosopherByThread(Thread thread) {
        for(Table table: rooomTables) {
            Philosopher[] philosphers = table.getPhilosophers();
            for (Philosopher philosopher : philosphers) {
                if (philosopher == null) {
                    continue;
                }
                if (philosopher.getThreadId() == thread.getId()) {
                    return philosopher;
                }
            }
        }
        return null;
    }

    // Helper method to remove philosopher from table
    private static void removePhilosopherFromTable(Philosopher philosopher) {
        if (philosopher!=null) {
            for(Table table: rooomTables) {
                if (table.getName() == philosopher.getTableName()) {
                    table.removePhilosopher(philosopher);
                }
            }
        }
    }

    // Helper method to move philosopher from 6th table
    private static void movePhilosopher(Philosopher philosopher) {
        if (philosopher!=null) {
            movedPhilosophers.add(philosopher.getPhilospherName());
            System.out.println("Moved philosopher name = "+ philosopher.getPhilospherName());
            if (movedPhilosophers.size() == 5) {
                char[] philosopherNames = new char[5];
                for (int i =0; i<philosopherNames.length; i++) {
                    philosopherNames[i] = movedPhilosophers.get(i);
                }
                rooomTables.add(new Table(philosopherNames, "Table6"));
                rooomTables.get(rooomTables.size() - 1).start();
                System.out.println("--------------------------------------------------------------------------------");
            }
        }
    }


    public static void checkDeadlock() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] threadIds = bean.findDeadlockedThreads();

        if (threadIds != null) {
            Thread thread = findThreadById(threadIds[threadIds.length-1]); //get thread of the last philosopher that caused the deadlock
            if (thread != null) {
                Philosopher philosopher = findPhilosopherByThread(thread);
                movePhilosopher(philosopher);
                if (philosopher != null) {
                    System.out.println("Last Philosopher that got stuck is = " + philosopher.getPhilospherName());
                }
                removePhilosopherFromTable(philosopher);
                String tableName = philosopher.getTableName();
                String indexStr = tableName.substring(tableName.length() - 1);
                int index = Integer.parseInt(indexStr);
                if (index == 6) { 
                    System.out.println("6th table is at a deadlock and it took the following seconds to get stuck = " + timer);
                    System.exit(0);
                }
                System.out.println(index);
                for (Table table: rooomTables) {
                    table.start();
                }
                
            }
        }
    }

    public static void main(String[] args) {
        

            char[] alphabet = new char[26];

            // Populate the array with alphabets
            for (int i = 0; i < 26; i++) {
                alphabet[i] = (char) ('A' + i);
            }

            Table[] tables = new Table[5];

            for (int i=0; i<tables.length; i++) {
                int startIndex = i*5;
                char[] philosopherNames = Arrays.copyOfRange(alphabet, startIndex, startIndex+5);
                tables[i] = new Table(philosopherNames, "Table" + Integer.toString(i));
                rooomTables.add(tables[i]);
                rooomTables.get(i).start();
            }

            // Check for deadlock after each second
            while (true) {
                try {
                    Thread.sleep(1000);
                    timer = timer+1;
                    System.out.println("Seconds: " + timer);
                    checkDeadlock();
                } catch (Exception e) {
                    // handle exception
                }   
            }
    }
}