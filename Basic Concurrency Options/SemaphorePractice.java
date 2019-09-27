import java.util.*;
import java.util.concurrent.Semaphore;

//Uses a semaphore to enable concurrency between 5 processes

public class SemaphorePractice extends Thread {
    
    private final String LINE_A = "We hold these truths to be self-evident, that all men are created equal,";
    private final String LINE_B = "that they are endowed by their Creator with certain unalienable Rights,";
    private final String LINE_C = "that among these are Life, Liberty and the pursuit of Happiness.";
    
    private int whichThread;
    static volatile boolean[] choosing = new boolean[5];
    static volatile int[] tickets = new int[5];
    static volatile Semaphore guardian = new Semaphore(1, false);
    
    public SemaphorePractice(int id){
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
        //int other = (this.whichThread + 1) % 2;
        
        
        //Entry Protocol
        for(int i = 0; i < 5; i++){
            //Entry Protocol
            try{
                guardian.acquire();
            } catch(InterruptedException e){
            }
            
            printCurrent(i, this.whichThread, 1);
            spin(20);
            printCurrent(i, this.whichThread, 2);
            spin(20);
            printCurrent(i, this.whichThread, 3);
            spin(20);
            
            
            //Exit Protocol
            guardian.release();
            
        }
    }
    
    
    public static int getMax(int[] arr){
        int max = arr[0];
        
        for(int i : arr){
            
            if(i > max){
                max = i;
            }
        }
        
        return max;
    }
    
    
    
    public static void main(String[] args){
        SemaphorePractice m = new SemaphorePractice(0);
        SemaphorePractice n = new SemaphorePractice(1);
        SemaphorePractice o = new SemaphorePractice(2);
        SemaphorePractice p = new SemaphorePractice(3);
        SemaphorePractice q = new SemaphorePractice(4);
        
        m.start();
        n.start();
        o.start();
        p.start();
        q.start();
        
    }
    
}

