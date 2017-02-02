package concurrencyassignmentpart1;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ConcurrencyAssignmentPart1 {

    public static void main(String[] args) {
        
        System.out.println("Start of the Working Day" + "\n");
        
        Clock clock = Clock.getInstance();
        
        /**
         * Constructor passes the max number 
         * of gifts in to the sleight
         */
        Sleight sleight = new Sleight(120);

        Santa santa1 = new Santa("Kevin", sleight);
        Santa santa2 = new Santa("John", sleight);
        Santa santa3 = new Santa("Marc", sleight);
        
        Elf elf_1 = new  Elf("Jane", sleight);
        Elf elf_2 = new Elf("Jack", sleight);
        Elf elf_3 = new Elf("Alex", sleight);
        Elf elf_4 = new Elf("Suzan", sleight);
        Elf elf_5 = new Elf("Bill", sleight);
        
        /**
         * Begin execution of the threads
         */
        santa1.start();
        santa2.start();
        santa3.start();
        
        elf_1.start();
        elf_2.start();
        elf_3.start();
        elf_4.start();
        elf_5.start();
        
        clock.start();
        
        /**
         * Causes the current thread 
         * to pause execution until 
         * each thread terminates
         */
        try {
            
            elf_1.join();
            elf_2.join();
            elf_3.join();
            elf_4.join();
            elf_5.join();
            
            santa1.join();
            santa2.join();
            santa3.join();
            
        } catch (InterruptedException ex) { }
        
        
        /**
         * Output on the console the 
         * activities of the day
         */
        elf_1.output();
        elf_2.output();
        elf_3.output();
        elf_4.output();
        elf_5.output();
        
        System.out.println("\n");
        
        santa1.output();
        santa2.output();
        santa3.output();
        
        System.out.println("\n");
        System.out.println("Toys left into the sleigh at the end of the day: " + sleight.getCounter() + "\n");
        System.out.print("\n");
        System.out.println("End of the Working Day");
    }
}
