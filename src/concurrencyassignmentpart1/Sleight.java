package concurrencyassignmentpart1;


public class Sleight {
    
    private final Present [] sleight; //Initilizing an array of type Present for storring all the gifts
    private int counter = 0; //next empty slot in the sleight (buffer)
    private static  int Sleight_Size;
    
    public Sleight(int size){
        sleight = new Present[size];
        Sleight_Size = size;
    }
    
    /**
      * Add a value to the buffer. 
      */
    public void addToSleight(Present item){
        
        counter++; //increasing the empty slots
        sleight[counter-1] = item;
    }
    
    /**
     * Removing item from the buffer.
     * @return 
     */
    public Present extract(){
        
        Present item = sleight[counter-1];
        counter--;    
        return item;
    }
    
    /**
     * Return true if the sleight is full
     * @return 
     */
    public boolean isFull(){
        
        boolean sleightFull = true;
        sleightFull = counter == Sleight_Size;
        return sleightFull;
    }
    
    /**
     * Return true if the sleight is empty
     * @return 
     */
    public boolean isEmpty(){
       
        boolean sleightEmpty = true;
        sleightEmpty = counter == 0;
        return sleightEmpty;
    }
    
    /**
     * Return the available gifts inside
     * the Sleight
     * @return 
     */
    public int getCounter(){
        return counter;
    }
}