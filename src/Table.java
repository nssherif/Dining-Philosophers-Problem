

public class Table implements Runnable {
    private char[] philosopherNames;

    Table(char[] philosopherNames) {
        this.philosopherNames = philosopherNames;
    }

    public void checkDeadlock(Philosopher[] philosophers) {
        System.out.println("Checking deadlock");
        boolean isDeadlock = true;
        for (Philosopher philosopher : philosophers) {
            if (!philosopher.isDeadlocked()){
                isDeadlock = false;
            }
        }

        if (isDeadlock) {
            System.out.println("Deadlock occured");

        }
    } 
    
    @Override
    public void run() {
        try {
            Philosopher[] philosophers = new Philosopher[5];
            Object[] forks = new Object[philosophers.length];

            for (int i = 0; i < forks.length; i++) {
                forks[i] = new Object();
            }

            for (int i = 0; i < philosophers.length; i++) {

                Object leftFork = forks[i];
                Object rightFork = forks[(i + 1) % forks.length];

                philosophers[i] = new Philosopher(leftFork, rightFork, this.philosopherNames[i]);

                Thread t = new Thread(philosophers[i]);
                t.start();
                //Thread.sleep(((int) (4 * 1000)));
            } 
            checkDeadlock(philosophers);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        
    }
}
