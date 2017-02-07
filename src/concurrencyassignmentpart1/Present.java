package concurrencyassignmentpart1;

public class Present {
    
    String Gift_Type;
    boolean gender;
    boolean colour;
    
    
    public Present(String name, boolean gender, boolean colour) {
        
        this.Gift_Type = name;
        this.gender = gender;
        this.colour = colour;
    }
   
    /**
     * A way to obtain a String 
     * representation of an Object.
     * @return 
     */
    @Override
    public String toString() {
        return "Gift " + Gift_Type + "\t" + "wrapped in " + (gender?(colour? "blue" : "red"):(colour? "pink" : "silver")) +" wrapping paper";
    }
}