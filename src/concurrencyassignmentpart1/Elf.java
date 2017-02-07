package concurrencyassignmentpart1;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Elf extends Thread {

    Clock clock = Clock.getInstance();

    Sleight sleight;
    String elf_id;
    
    private final int rand = 2000;
    private int toyCount = 0;
    private int putOnSleight = 0;
    private int ticksWaiting = 0;
    private int hCount = 0;
    private int hTicks = 0;

    /**
     * Arbitrary list of Toys 
     */
    private static final String[] toys = { "Doll", "Train", "Car", "Dinosaur", "Drums", "Barbie", "Drone", "Guitar", "Puzzle", "Tablet", "PC" };

    /**
     * The PrintWriter class, is used to print formatted and human 
     * readable data in a stream. The text-output stream can be 
     * either OutputStream or Writer. It contains methods for printing
     * the primitive types as text-format representation instead as byte values.
     */
    PrintWriter writer;

    public Elf(String name, Sleight s) {
        
        this.elf_id = name;
        sleight = s;

        try {
            
            /**
             * Each text file is logged with the name
             * of the corresponding name
             */
            writer = new PrintWriter("Elf-" + elf_id + ".txt", "UTF-8");
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            
            System.out.println("Error in writing to file:" + "\t" + elf_id);
        }
    }

    /**
     * This method is used to add gifts into the sleight
     * by using the sleight object and the addToSleight
     * function which just take the chosen gift and add it 
     * at the sleight array  
     * @param p 
     */
    private void Produce(Present p) {
        sleight.addToSleight(p);
        putOnSleight++;
    }
    
    @Override
    public void run() {
        while (!clock.isStopped()) {

            /**
             * Choosing toys from the shelves
             * of the Toy Department
             */
            try {
                
                sleep((int) (Math.random() * rand));
                
            } catch (InterruptedException ex) {}

            /**
             * Storing a random toy from the toys array
             * into the toy variable of type String
             */
            String toy = toys[(int) (Math.random() * toys.length)];

            /**
             * boolean variable for detecting if
             * it is boy or girl
             * it return a random boolean true or false
             * If the gender is try it return boy otherwise
             * it return false.
             */
            boolean gender;
            boolean colour;
            colour = Math.random() < 0.5;
            gender = Math.random() < 0.5;
            
            writer.println(clock.toString() + "Elf " + elf_id + "\t" + "Selected toy " + toy + " for " +(gender?"boy.":"girl."));
            
            /**
             * Wrapping toy in a corresponding paper.
             * The color for wrapping is chosen random again if its boy 
             * the wrapping paper is either blue or red and if its girl
             * the color is pink or silver
             */
            try {
                
                sleep((int) (Math.random() * rand)); //pause the thread for random period of time
                
            } catch (InterruptedException ex) {}
            
            /**
             * The present class is used as an interface for 
             * the gifts which takes as arguments the type of
             * the gift and if its for boy or girl and randomly
             * return the present name if its for girl or boy
             * and wrapped into the corresponding paper
             */
            Present p = new Present(toy, gender, colour);

            writer.println(clock.toString() + "Elf " + elf_id + "\t" + "Wrapped toy " + toy + " in "+ (gender?(colour? "blue":"red"):(colour? "pink" : "silver")) + " wrapping paper."); 
           /**
            * Increasing the number of the 
            * wrapped Toys.
            */
            toyCount++; 
            
            /**
             * This synchronized method is used to lock the sleigh object.
             * I am using a loop to check if the sleight is full and that the clock
             * object is not terminated. If its true then the Elves are required to 
             * enter the waiting statement until the Santas empty the sleight.
             */
            synchronized(sleight){
                int startTime = clock.getTick();
                while(sleight.isFull() && clock.getState() != Thread.State.TERMINATED){    
                    try{
                        
                        /**
                         * causes current thread to wait until another thread 
                         * invokes the notifyAll() method for this object.
                         */
                        sleight.wait(); 
                    } catch (InterruptedException ex){}
                }
                
                /**
                 * This if statement check that the 
                 * sleight is not full so it can
                 * proceed to insert gifts
                 */
                if(!sleight.isFull()){ 
                    Produce(p);
                }
                int endTime = clock.getTick();
                ticksWaiting += endTime - startTime;
                
                /**
                 * Wakes up all threads that are 
                 * waiting on this object's monitor.
                 */
                sleight.notifyAll();
            }

            writer.println(clock.toString() + "Elf " + elf_id + "\t" + "Placed toy " + toy + " on sleigh.");
            writer.println();
            
            /**
             * A check if the clock counter
             * passed one hour if uses it store the ticks
             * waiting of the elf during the current hour
             * and increase the gift wrapped during this hour. 
             */
            if(clock.isHour()){
                hTicks = clock.getTick();
                hCount++;
            }
        }
        writer.close();
    }

     /**
     * A method that is printed at the end of the program,
     * to show the progress of the Santas at the end of the day
     */
    public void output() {
        System.out.println("Elf " + elf_id + "\t" +"Number of gifts wrapped: " + toyCount);
        System.out.println("Elf " + elf_id + "\t" +"Put gifts of the sleight: " + putOnSleight);
        System.out.println("Elf " + elf_id + "\t" +"Time Spent waiting: " + ticksWaiting + " ticks" + "\n");
    }

    /**
     * A method called inside the main class
     * to print hourly report for each Elf
     * into the console.
     */
    public void report() {
        String report = "For " +  clock.getTick()/60 + " hours " + "Elf " + elf_id + "\t" + "has wrapped " + hCount + " Gifts" + "\t" + "and have waited " + hTicks + " seconds";
        System.out.println(report);
        System.out.println();
    }

    /**
     * A method to get the current number
     * of the wrapped presents
     * @return 
     */
    public int getToyElfCount() {
        return toyCount;
    }
    
    
}