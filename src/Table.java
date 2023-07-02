
public class Table {
    private char[] philosopherNames;
    private String name;
    private Philosopher[] philosophers = new Philosopher[5];

    Table(char[] philosopherNames,String name) {
        this.philosopherNames = philosopherNames;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void removePhilosopher(Philosopher philosopher) {
        for (int i =0 ; i < this.philosophers.length ; i++) {
            if (this.philosophers[i].getName() == philosopher.getName()) {
                releaseForks(philosopher);
                philosopher.interrupt();
                this.philosophers = removeElement(this.philosophers, i);
            }
        }
    }

    public void releaseForks(Philosopher philosopher) {
        Object leftFork = philosopher.getLeftFork();  
        Object rightFork = philosopher.getRightFork();
        synchronized (leftFork) {
            synchronized (rightFork) {
                leftFork.notifyAll();  // Notify waiting threads on the left fork
                rightFork.notifyAll(); // Notify waiting threads on the right fork
            }
        }
    }

    public static Philosopher[] removeElement(Philosopher[] array, int index) {
        if (index < 0 || index >= array.length) {
            return array; // Return the original array if index is out of bounds
        }
    
        Philosopher[] newArray = new Philosopher[array.length - 1];
    
        for (int i = 0, j = 0; i < array.length; i++) {
            if (i != index) {
                newArray[j] = array[i];
                j++;
            }
        }
    
        return newArray;
    }

    
    public void start() {
        
            System.out.println("Running table : "+ this.name);
            Philosopher[] philosophers = new Philosopher[5];
            Object[] forks = new Object[philosophers.length];

            for (int i = 0; i < forks.length; i++) {
                forks[i] = new Object();
            }

           
            for (int i = 0; i < philosophers.length; i++) {
                Object leftFork = forks[i];
                Object rightFork = forks[(i + 1) % forks.length];

                philosophers[i] = new Philosopher(leftFork, rightFork, this.philosopherNames[i], this.name);
                this.philosophers[i] = philosophers[i];

                Thread t = new Thread(philosophers[i]);
                philosophers[i].setThreadId(t.getId());
                t.start(); 
            } 
    }

    public Philosopher[] getPhilosophers() {
        return this.philosophers;
    }
}
