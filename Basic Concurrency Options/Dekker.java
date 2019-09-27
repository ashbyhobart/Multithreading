import java.util.*;

//Implementation of Dekker's Algorithm for 2 concurrent processes
public class Dekker extends Thread {
    
    private final String LINE_A = "We hold these truths to be self-evident, that all men are created equal,";
    private final String LINE_B = "that they are endowed by their Creator with certain unalienable Rights,";
    private final String LINE_C = "that among these are Life, Liberty and the pursuit of Happiness.";
    
    private int whichThread;
    static volatile boolean[] flags = new boolean[2];
    static volatile int polite;
    
    public Dekker(int id){
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
            
            flags[this.whichThread] = true;
            
            while(flags[other]){
                
                if(polite == other){
                    flags[this.whichThread] = false;
                    
                    while(flags[other]){};
                    
                    flags[this.whichThread] = true;
                }
            }
                
                
            //Critical Section
            printCurrent(i, this.whichThread, 1);
            spin(20);
            printCurrent(i, this.whichThread, 2);
            spin(20);
            printCurrent(i, this.whichThread, 3);
            spin(20);
            
            
            //Exit Protocol
            polite = other;
            flags[this.whichThread] = false;
        }
    }
    
    
    
    public static void main(String[] args){
        Dekker m = new Dekker(0);
        Dekker n = new Dekker(1);
        
        m.start();
        n.start();
        
    }
    
}

