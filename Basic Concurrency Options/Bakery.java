import java.util.*;

//Bakery algorithm implementation for 5 threads

public class Bakery extends Thread {
    
    private final String LINE_A = "We hold these truths to be self-evident, that all men are created equal,";
    private final String LINE_B = "that they are endowed by their Creator with certain unalienable Rights,";
    private final String LINE_C = "that among these are Life, Liberty and the pursuit of Happiness.";
    
    private int whichThread;
    static volatile boolean[] choosing = new boolean[5];
    static volatile int[] tickets = new int[5];
    
    public Bakery(int id){
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
    
    
    
    
    
    
    public void hold(int id) {
        choosing[id] = true;
        tickets[id] = getMax(tickets) + 1;
        choosing[id] = false;
        
        for (int j = 0; j < 5; j++) {
            
            if (j == id){
                continue;
            }
            
            while (choosing[j]) {}
            while (tickets[j] != 0 && (tickets[id] > tickets[j] || (tickets[id] == tickets[j] && id > j))) {}
            
        }
    }
    
    
    
    private void free(int id) {
        tickets[id] = 0;
    }    
    
    
    public void run() {
        //int other = (this.whichThread + 1) % 2;
        
        
        //Entry Protocol
        for(int i = 0; i < 5; i++){
            //Entry Protocol
            hold(this.whichThread);
            
            printCurrent(i, this.whichThread, 1);
            spin(20);
            printCurrent(i, this.whichThread, 2);
            spin(20);
            printCurrent(i, this.whichThread, 3);
            spin(20);
            
            
            //Exit Protocol
            free(this.whichThread);
            
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
        Bakery m = new Bakery(0);
        Bakery n = new Bakery(1);
        Bakery o = new Bakery(2);
        Bakery p = new Bakery(3);
        Bakery q = new Bakery(4);
        
        m.start();
        n.start();
        o.start();
        p.start();
        q.start();
        
    }
    
}

