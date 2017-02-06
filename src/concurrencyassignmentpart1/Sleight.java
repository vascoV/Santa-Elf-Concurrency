package concurrencyassignmentpart1;


public class Sleight {
    
    private final Present [] sleight;
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
        
        counter++;
        sleight[counter-1] = item;
    }
    
    public Present extract(){
        
        Present item = sleight[counter-1];
        counter--;    
        return item;
    }
    
    public boolean isFull(){
        
        boolean sleightFull = true;
        sleightFull = counter == Sleight_Size;
        return sleightFull;
    }
    
    public boolean isEmpty(){
       
        boolean sleightEmpty = true;
        sleightEmpty = counter == 0;
        return sleightEmpty;
    }
    
    public int getCounter(){
        return counter;
    }
}