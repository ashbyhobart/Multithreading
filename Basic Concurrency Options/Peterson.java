import java.util.*;

//Peterson implementation of 2 process critical section

public class Peterson extends Thread {
    
    private final String LINE_A = "We hold these truths to be self-evident, that all men are created equal,";
    private final String LINE_B = "that they are endowed by their Creator with certain unalienable Rights,";
    private final String LINE_C = "that among these are Life, Liberty and the pursuit of Happiness.";
    
    private int whichThread;
    static volatile boolean[] flags = new boolean[2];
    static volatile int polite;
    
    public Peterson(int id){
        whichThread = id;
    }
    
    public void printCurrent(int currentIteration, int currentThread, int lineNum){
        
        switch (lineNum){
            case 1:
                System.out.println("[" + currentThread + ", " + currentIteration + "] " + LINE_A);
                break;
            case 2:
                System.out.println("[" + currentThread + ", " + currentIteration + "] " + LINE_B);
                break;
            case 3:
                System.out.println("[" + currentThread + ", " + currentIteration + "] " + LINE_C);
                break;
            default:
                System.out.println("Error in switch statement");
        }
        
    }
    
    
    public void spin(int bound){
        Random x = new Random();
        
        try {
            int timeUnder = x.nextInt(bound + 1);
            sleep(timeUnder);
        } 
        catch (InterruptedException e) { }
    }
    
    
    
    
    public void run() {
        int other = (this.whichThread + 1) % 2;
        
        
        //Entry Protocol
        for(int i = 0; i < 5; i++){
            
            flags[this.whichThread] = true; //I want to go
            polite = other; //But let me let the other guy go
            
            while(flags[other] && polite == other){}; //Busy wait until other guy is ready
                
                
            //Critical Section
            printCurrent(i, this.whichThread, 1);
            spin(20);
            printCurrent(i, this.whichThread, 2);
            spin(20);
            printCurrent(i, this.whichThread, 3);
            spin(20);
            
            
            //Exit Protocol
            flags[this.whichThread] = false; //I'm all set
        }
    }
    
    
    
    public static void main(String[] args){
        Peterson m = new Peterson(0);
        Peterson n = new Peterson(1);
        
        m.start();
        n.start();
        
    }
    
}

