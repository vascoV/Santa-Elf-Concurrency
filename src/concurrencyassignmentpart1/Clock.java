package concurrencyassignmentpart1;

public class Clock extends Thread {

    private volatile int count = 0;
    boolean stop = false;
    boolean finished = false;
    final int MAXTICK = 60*8;  //60 minutes * 8 hours
    final int TICKTIME = 1000; 

    private static final Clock instance = new Clock();

    
    public static Clock getInstance() {
        return instance;
    }

    public int getTick() {
        return count;
    }

    public boolean isHour() {
        return count/60 == 0;
    }

    public boolean isStopped() {
        return finished;
    }

    @Override
    public void run() {
        while (count < MAXTICK) {
            count++;
            try {   
                Thread.sleep(TICKTIME);
            } catch (InterruptedException ex) {}
        }       
        finished = true;
    }

    @Override
    public String toString() {
        return "Time " + count + ": ";
    }
}
