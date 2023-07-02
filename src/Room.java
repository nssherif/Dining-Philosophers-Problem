import java.util.Arrays;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.util.*;

public class Room {
    static int timer = 0;
    private static Table[] rooomTables = new Table[1];
    private static Table spareTable;
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
            if (movedPhilosophers.size() == 5) {
                char[] philosopherNames = movedPhilosophers.toString().toCharArray();
                spareTable = new Table(philosopherNames, "Table6");
                spareTable.start();
            }
        }
    }


    public static void checkDeadlock() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] threadIds = bean.findDeadlockedThreads();

        if (threadIds != null) {
            Thread thread = findThreadById(threadIds[threadIds.length-1]); //get thread of the last philosopher that caused the deadlock
            System.out.println("threadID"+ thread.getId());
            if (thread != null) {
                Philosopher philosopher = findPhilosopherByThread(thread);
                movePhilosopher(philosopher);
                if (philosopher != null) {
                    System.out.println("Philosopher that got stick is = " + philosopher.getName());
                }
                removePhilosopherFromTable(philosopher);
            }
            //System.exit(0);
        } else {
            //System.out.println("No deadlock detected.");
        }
    }

    public static void main(String[] args) {
        try {

            char[] alphabet = new char[26];

            // Populate the array with alphabets
            for (int i = 0; i < 26; i++) {
                alphabet[i] = (char) ('A' + i);
            }

            Table[] tables = new Table[1];

            for (int i=0; i<tables.length; i++) {
                int startIndex = i*5;
                char[] philosopherNames = Arrays.copyOfRange(alphabet, startIndex, startIndex+5);
                tables[i] = new Table(philosopherNames, "Table" + Integer.toString(i));
                rooomTables[i] = tables[i];
                rooomTables[i].start();
            }

            // Check for deadlock after each second
            while (true) {
                Thread.sleep(1000);
                timer = timer+1;
                System.out.println("Seconds: " + timer);
                checkDeadlock();
            }

            
        } catch (Exception e) {
            System.out.println("Exiting error from Room class = "+ e);
            System.out.println("Line number = "+ e.getStackTrace()[0].getLineNumber());
        }
        
    }
}