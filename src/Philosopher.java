
public class Philosopher implements Runnable {
    private final Object leftFork;
    private final Object rightFork;
    private char name;
    private boolean deadlocked = false;

    Philosopher(Object left, Object right, char name) {
        this.leftFork = left;
        this.rightFork = right;
        this.name = name;
    }

    private void doAction(String action) throws InterruptedException {
        int sleepTimer = 0;
        String outputMsg = ""; 
        switch(action) {
            case "thinking":
                sleepTimer = 5; //Think for 5 seconds
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

        System.out.println("Philospher with name = " + this.name + " is understaking the following action -> " + outputMsg);
        Thread.sleep(((int) (sleepTimer * 1000)));
        return;
    }

    public boolean isDeadlocked() {
        return this.deadlocked;
    }

    @Override
    public void run() {
        try {
            while (true) {
                doAction("thinking"); // thinking
                synchronized (leftFork) {
                    doAction("pickUpLeftFork");
                    this.deadlocked = true;
                    synchronized (rightFork) {
                        this.deadlocked = false;
                        doAction("pickUpRightForkAndEat"); // eating
                        doAction("putDownRightFork");
                    }
                    doAction("putDownLeftFork");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (DeadlockException e) {
            System.out.println("Deadlock reached");
        }
    }
    static class DeadlockException extends RuntimeException {
        // Empty class implementation
    }
}
