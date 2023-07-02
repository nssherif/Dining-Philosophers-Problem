
import java.util.Random;

public class Philosopher extends Thread {
    private final Object leftFork;
    private final Object rightFork;
    private char name;
    private boolean deadlocked = false;
    private String tableName;
    private long threadId;
    private Boolean isExitRequested = true;

    Philosopher(Object left, Object right, char name, String tableName) {
        this.leftFork = left;
        this.rightFork = right;
        this.name = name;
        this.tableName = tableName;
    }

    private void doAction(String action) throws InterruptedException {
        int sleepTimer = 0;
        String outputMsg = ""; 
        Random random = new Random();
        switch(action) {
            case "thinking":
                // sleepTimer = random.nextInt(11); // Think for a random number between 0 (inclusive) and 11 (exclusive);
                sleepTimer = 3;
                outputMsg = "Thinking";
                break;
            case "pickUpLeftFork":
                sleepTimer = 4; //Wait for 4 seconds
                outputMsg = "Picking up left fork";
                break;
            case "pickUpRightForkAndEat":
                sleepTimer = 3; //Eat for 3 seconds
                outputMsg = "Picking up right fork and eating";
                break;
            case "putDownRightFork":
                sleepTimer = 0; //Instantaniously drop right fork
                outputMsg = "Putting down right fork and stopped eating";
                break;
            case "putDownLeftFork":
                sleepTimer = 0; //Instantaniously drop left fork
                outputMsg = "Putting down left fork";
                break;
            default:
                sleepTimer = 0;
                outputMsg = "Default case activated";
            }

        System.out.println(this.tableName + " ----- Philospher with name = " + this.name + " is understaking the following action -> " + outputMsg);
        Thread.sleep(((int) (sleepTimer * 1000)));
        return;
    }

    public boolean isDeadlocked() {
        return this.deadlocked;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public long getThreadId() {
        return this.threadId;
    }

    public char getPhilospherName() {
        return this.name;
    }

    public String getTableName() {
        return this.tableName;
    }

    public Object getLeftFork() {
        return this.leftFork;
    }

    public Object getRightFork() {
        return this.rightFork;
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                doAction("thinking"); // thinking
                synchronized (this.leftFork) {
                    doAction("pickUpLeftFork");
                    while(this.isExitRequested) {
                        this.leftFork.wait();
                    }
                    this.deadlocked = true;
                    synchronized (this.rightFork) {
                        this.deadlocked = false;
                        doAction("pickUpRightForkAndEat"); // eating
                        doAction("putDownRightFork");
                    }
                    doAction("putDownLeftFork");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
