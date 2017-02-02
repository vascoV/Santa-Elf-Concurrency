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
      * Add a value to the buffer.  If the buffer is full, 
      */
    public void addToSleight(Present item){
        
        counter++;
        sleight[counter-1] = item; //if 120 so you are 119
        
//        System.out.println("Producing Gifts: " + "\t" + "counter: " + counter);
//         System.out.println("################################################");
    }
    
    public Present extract(){
        
        Present item = sleight[counter-1];
        counter--;
        
//         System.out.println("Remove Gifts: " + "\t" + "counter: " + counter);
        
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