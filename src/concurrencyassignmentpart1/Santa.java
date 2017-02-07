package concurrencyassignmentpart1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Santa extends Thread {

    Clock clock = Clock.getInstance();

    Sleight buf;
    String santa_id;

    /**
     * A list used for the sack to store
     * the gifts in up to 10 for each sack
     */
    List<Present> sack;
    
    private final int Sack_Capacity = 10;

    int toyCount = 0;
    int ticksWaiting = 0;
    int hCount = 0;

    /**
     * The PrintWriter class, is used to print formatted and human 
     * readable data in a stream. The text-output stream can be 
     * either OutputStream or Writer. It contains methods for printing
     * the primitive types as text-format representation instead as byte values.
     */
    PrintWriter writer;
    
    public Santa(String name, Sleight s) {
        
        this.sack = new ArrayList<>(); //initializing an Array List for the Sack object of type List
        buf = s;
        this.santa_id = name;

        try {
            
            /**
             * Each text file is logged with the name
             * of the corresponding name
             */
            writer = new PrintWriter("Santa-" + santa_id + ".txt", "UTF-8");
       
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        
            System.out.println("Error in writing to file" + "\t" + santa_id);
        }
    }

    /**
     * A function that removes gifts
     * from the sleight and returns
     * the removed gift
     * @return 
     */
    public Present consume() {
        Present nextItem = buf.extract();
        return nextItem;
    }

    @Override
    public void run() {
        
        while (!clock.isStopped()) {
            
            /**
             * A while loop to check if the sack is full
             * If its true then it continues to the synchronized
             * method. 
             */
            while(sack.size() < Sack_Capacity) {
                
              /**
                * This synchronized method is used to lock the sleigh object.
                * I am using a loop to check if the sleight is empty and that the clock
                * object is not terminated. If its true then the Santas are required to 
                * enter the waiting statement until the Elves insert gifts into the sleight.
                */
                synchronized(buf){ 
                    int startTime = clock.getTick();
                    while(buf.isEmpty() && clock.getState() != Thread.State.TERMINATED){

                        try{
                            
                          /**
                            * causes current thread to wait until another thread 
                            * invokes the notifyAll() method for this object.
                            */
                            buf.wait(); 
                        } catch (InterruptedException ex){}
                    }
                    int endTime = clock.getTick();
                    ticksWaiting += endTime - startTime;
                    Present s = consume();
                    writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Took toy " + s + " from sleigh.");
                    writer.println();
                    sack.add(s); //adding gifts into the sleight 
                    
                    /**
                    * Wakes up all threads that are 
                    * waiting on this object's monitor.
                    */
                    buf.notifyAll();
                }
                    /**
                     * A check that the Santa collect at least 6 
                     * gift from the sleight before he goes back
                     * to his allocated department
                     */
                    if(sack.size() >= 6 && buf.isEmpty()){ break; }
            }
            
            /**
             * Santa spend some time to Walk back
             * to his allocated department
             */
            try {
                
               sleep(ThreadLocalRandom.current().nextInt(1500, 4000));     
            } catch (InterruptedException ex) {}

            writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Walked back to his own allocated department.");
            writer.println();
      

            /**
             * Iterator enables you to cycle through a 
             * collection, obtaining or removing elements. 
             */
            for (Iterator<Present> i = sack.iterator(); i.hasNext() && !clock.isStopped();) {
               
                /**
                 * Santa spent some time with
                 * each child to give presents.
                 */
                try { 
                
                    sleep(ThreadLocalRandom.current().nextInt(1500, 4000)); 
                } catch (InterruptedException ex) {}

                Present g = i.next();

                writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Gives Toy " + g + ".");
                writer.println();
                
                i.remove(); //removes the given gift from the sack
                toyCount++;
            }
            
             /**
             * Santa need some time to Walk back
             * to the toy department
             */
            try {
                
                sleep(ThreadLocalRandom.current().nextInt(1500, 4000));
            } catch (InterruptedException ex) {}

            writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Walked back to the Toy Store.");
            writer.println();
            hCount++; // incresing the number of gift given 
        }
       writer.close();
       
       /**
        * Using synchronized with a lock the sleight
        * to wake up the Elves when Santas end their
        * working day to end their current action 
        * finish the working day too.
        */
       synchronized(buf){
           buf.notifyAll();
       } 
       
    }
    
    /**
     * A method that is printed at the end of the program,
     * to show the progress of the Santas at the end of the day
     */
    public void output() {
        System.out.println("Santa " + santa_id + "\t" + "Number of gifts gave: " + toyCount);
        System.out.println("Santa " + santa_id + "\t" + "Time Spent waiting: " + ticksWaiting + " minutes");
        System.out.println("Santa " + santa_id + "\t" + "Toys left in bag: " + sack.size() + "\n");
    }
    
    /**
     * A method called inside the main class
     * to print hourly report for each Santa
     * into the console.
     */
    public void report() {
        String report = "Santa " + santa_id + "\t" + "gave " + hCount + " Gifts  For: " +  clock.getTick()/60 + " hour ";
        System.out.println(report);
        System.out.println();
    }

    /**
     * A method that get The current number
     * number of given toys
     * @return 
     */
    public int getToySantaCount() {
        return toyCount;
    }
    
    /**
     * A method that returns the current sack size
     * @return 
     */
    public int getSackSize(){
        return sack.size();
    }
}
