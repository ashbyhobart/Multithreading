import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.HashMap;

import java.util.concurrent.*;







class Scribe extends Thread {
    private int ID;
    private int trav;
    private int trail;


    Scribe(int id, int tempTrail, int tempTrav) {
        this.ID = id;
        this.trail = tempTrail;
        this.trav = tempTrav;

    }


    public void run() {

        try {
            HashMap<String, Integer> partialOI = new HashMap<>();

            for (Integer num = this.trail; num < this.trav; num++) {
                String hashBrown = Treasure.hash(num.toString());
                partialOI.put(hashBrown, num);
            }

            Treasure.mutex.acquire();
            Treasure.oghmaInfinium.putAll(partialOI);
            Treasure.mutex.release();

            Treasure.barrier[this.ID].release();


        } catch (Exception exc) {
            System.out.println(exc);
        }

    }
}

public class Treasure {

    public String denote;
    private static boolean found;
    public static boolean halt;
    private int booty;
    private int max;

    private String contents;
    private int lenContents;

    public static int N;
    public static int total;
    private HashSet<Integer> visited;
    public static Semaphore[] barrier;

    public static HashMap<String, Integer> oghmaInfinium = new HashMap<>();
    public static volatile Semaphore mutex = new Semaphore(1);

    public Treasure(String filename, int N) {

        this.denote = filename;
        this.found = false;
        this.booty = 0;
        this.max = 0;
        this.halt = false;
        this.N = N;
        this.visited= new HashSet<>();

        barrier = new Semaphore[N];
        for (int i = 0; i < N; i++){
            barrier[i] = new Semaphore(0);
        }

        try {
            BufferedReader bf = new BufferedReader(new FileReader(denote));
            StringBuilder builder = new StringBuilder();
            String currentLine = bf.readLine();
            while (currentLine != null) {
                builder.append(currentLine);
                currentLine = bf.readLine();
            }
            this.contents = builder.toString();
            this.lenContents = this.contents.length();
        }

        catch (Exception e) {
            System.out.println(e);
        }

        this.total = this.lenContents + (int)(this.lenContents * 0.2);
        


        initHashMap();
        oghmaInfinium.put(hash("add"), -1);
        oghmaInfinium.put(hash("mul"), -2);
        oghmaInfinium.put(hash("div"), -3);

    }








    public String goBeyond(String to_unhash, int goFrom) {
        String input;

        /* if we are here, the input is not mul,add, or div, it is a number*/
        for(int i = goFrom; ; i++){
            input = Integer.toString(i);
            String couldBe = hash(input);
            this.oghmaInfinium.put(couldBe, i);

            if(couldBe.equals(to_unhash)){
                this.max = i;
                return input;
            }
        }

    }



    public int findTreasure(int k0){

        for(int k = 0; k < this.N; k++){

            try{
                barrier[k].acquire();
            } catch(Exception e){
            }
        }

        String h11 = this.contents.substring(k0, k0 + 32);
        String h12 = this.contents.substring(k0 + 32, k0 + 64);

        String k11 = oghmaInfinium.get(h11).toString();
        String k12 = oghmaInfinium.get(h12).toString();

        int left;

        if (k11.equals("-1")){
            left = Integer.parseInt(k12) * 2;
        } else if (k11.equals("-2")){
            left = Integer.parseInt(k12) * 3;
        } else if (k11.equals("-3")){
            left = Integer.parseInt(k12) / 3;
        } else {
            left = Integer.parseInt(k11);
        }

        if (left < this.lenContents && !visited.contains(left)) {
            visited.add(left);
            findT(left);
        }

        int right;

        if (k12.equals("-1")){
            right = Integer.parseInt(k11) * 2;
        } else if (k12.equals("-2")){
            right = Integer.parseInt(k11) *3;
        } else if (k12.equals("-3")){
            right = Integer.parseInt(k11) / 3;
        } else{
            right = Integer.parseInt(k12);
        }

        if (right < this.lenContents && !visited.contains(right)){
            visited.add(right);
            findT(right);
        }

        halt = true;
        return this.booty;

    }


    private void findT(int offset){

        if (this.found){
            return;
        }

        if (offset > this.lenContents) {

            if (offset % 2 == 1){
                this.booty = offset;
                this.found = true;
                return;
            } else{
                return;
            }

        }

        String h11 = this.contents.substring(offset, offset + 32);
        String h12 = this.contents.substring(offset + 32, offset + 64);
        String k11 = "";
        String k12 = "";

        if (oghmaInfinium.containsKey(h11)) {
            k11 = oghmaInfinium.get(h11).toString();
        } else {
            k11 = goBeyond(h11, this.lenContents);
        }

        if (oghmaInfinium.containsKey(h12)) {
            k12 = oghmaInfinium.get(h12).toString();
        } else {
            k12 = goBeyond(h12, this.lenContents);
        }


        int left;

        if (k11.equals("-1")){
            left = Integer.parseInt(k12) * 2;
        } else if (k11.equals("-2")){
            left = Integer.parseInt(k12) * 3;
        } else if (k11.equals("-3")){
            left = Integer.parseInt(k12) / 3;
        } else {
            left = Integer.parseInt(k11);
        }

        if (!visited.contains(left)) {
            visited.add(left);
            findT(left);
        }


        int right;

        switch (k12) {
            case "-1":
                right = Integer.parseInt(k11) * 2;
                break;
            case "-2":
                right = Integer.parseInt(k11) * 3;
                break;
            case "-3":
                right = Integer.parseInt(k11) / 3;
                break;
            default:
                right = Integer.parseInt(k12);

                break;
        }

        if (!visited.contains(right)){
            //System.out.println(right);
            visited.add(right);
            findT(right);
        }

    }



    public static void main(String args[]){
        //int provided = 176605;
        //int numCPUs = 12;
        //Treasure t = new Treasure("graph3.txt", numCPUs);


        int provided = Integer.parseInt(args[0]);
        Treasure t;
        int numCPUs;

        if(args.length > 2){
            numCPUs = Integer.parseInt(args[2]);
            t = new Treasure(args[1], Integer.parseInt(args[2]));
        } else{
            numCPUs = 22;
        }

        t = new Treasure(args[1], numCPUs);


        System.out.println(t.findTreasure(provided));
        System.exit(0);



    }


    public  void  initHashMap(){
        int trail = 0;
        int trav = (this.total) / this.N;

        for (int i = 0; i < N; i++){
            Scribe drone = new Scribe( i, trail, trav);
            trail = trav + 1;
            trav += (this.total) / this.N;
            drone.start();
        }

    }


    static String hash(String to_hash_input){
        String res = "";


        try {
            byte[] message_in_bytes = to_hash_input.getBytes("ascii");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(message_in_bytes);
            res = String.format("%032x", new BigInteger(1, digest));


        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println(e);
        }
        return res;



    }
}
