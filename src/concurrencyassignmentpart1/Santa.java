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

    List<Present> sack;
    
    private final int Sack_Capacity = 10;

    int toyCount = 0;
    int ticksWaiting = 0;
    int hCount = 0;
    private int lastPrint = 0;

    PrintWriter writer;
    
    public Santa(String name, Sleight s) {
        
        this.sack = new ArrayList<>();
        buf = s;
        this.santa_id = name;

        try {
            
            /**
             * Each text file has the name of the corresponding Santa
             */
            writer = new PrintWriter("Santa-" + santa_id + ".txt", "UTF-8");
       
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        
            System.out.println("Error in writing to file" + "\t" + santa_id);
        }
    }

    public Present consume() {
        
        Present nextItem = buf.extract();
        return nextItem;
    }

    @Override
    public void run() {
        
        while (!clock.isStopped()) {
            
            while(sack.size() < Sack_Capacity) {
                synchronized(buf){ 
                    int startTime = clock.getTick();
                    while(buf.isEmpty() && clock.getState() != Thread.State.TERMINATED){

                        try{
//                            System.out.println(santa_id + " is" + " Waiting....");
                            buf.wait(); 
                        } catch (InterruptedException ex){}
                    }
                    int endTime = clock.getTick();
                    ticksWaiting += endTime - startTime;
                    Present s = consume();
                    writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Took toy " + s + " from sleigh.");//                     buf.notifyAll();
                    sack.add(s);
                    buf.notifyAll();
                }
                    if(sack.size() >= 6 && buf.isEmpty()){      
                    break;
                    }
            }
            
            /**
             * Santa need some time to Walk back
             * to his own department
             */
            try {
                
               sleep(ThreadLocalRandom.current().nextInt(1500, 4000));     
            } catch (InterruptedException ex) {}

            writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Walked back to his own allocated department");
      

            for (Iterator<Present> i = sack.iterator(); i.hasNext() && !clock.isStopped();) {
               
                /**
                 * Santa spent some time with child
                 * to give presents
                 */
                try { 
                
                    sleep(ThreadLocalRandom.current().nextInt(1500, 4000)); 
                } catch (InterruptedException ex) {}

                Present g = i.next();

                writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Gives Toy " + g);
                writer.println();
                
                i.remove();
                toyCount++;
            }
            
             /**
             * Santa need some time to Walk back
             * to the toy department
             */
            try {
                
                sleep(ThreadLocalRandom.current().nextInt(1500, 4000));
            } catch (InterruptedException ex) {}

            writer.println(clock.toString() + "Santa " + santa_id + "\t" +"Walked back to the Toy Store!");
            
            if((int) (clock.getTick()/60) > lastPrint){
                report();
                lastPrint++;
                hCount++;
            }
        }
       writer.close();
    }
    
    public void output() {
        System.out.println("Santa " + santa_id + "\t" + "Number of gifts gave: " + toyCount);
        System.out.println("Santa " + santa_id + "\t" + "Time Spent waiting: " + ticksWaiting + " minutes");
        System.out.println("Santa " + santa_id + "\t" + "Toys left in bag: " + sack.size() + "\n");
    }
    
     public void report() {
        String report = "Santa " + santa_id + "\t" + "gave " + hCount + " Gifts  For: " +  clock.getTick()/60 + " hour ";
        System.out.println(report);
        System.out.println();
    }
}
