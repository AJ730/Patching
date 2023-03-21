import java.io.BufferedReader;
import java.io.InputStreamReader;
import nl.tudelft.instrumentation.runner.CallableTraceRunner;
import nl.tudelft.instrumentation.symbolic.*;

public class Problem2 {

    static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    private String[] inputs = {"iA", "iB", "iC"};


    public String a1855872761 = "g";

    public boolean cf = true;

    public String a547336540 = "g";

    public void calculateOutput(String input) {
        cf = false;
        if(input.equals("iA")) {

        }
        if(input.equals("iB")){

        }
        if(input.equals("iC")){
            System.out.println("true");
        }

        System.out.println("false");
    }


    public static void main(String[] args) throws Exception {
        // init system and input reader
        Problem2 eca = new Problem2();

        // main i/o-loop
        while(true) {
            //read input
            String input = stdin.readLine();
            try {
                //operate eca engine output =
                eca.calculateOutput(input);
            } catch(IllegalArgumentException e) {
                System.err.println("Invalid input: " + e.getMessage());
            }
        }
    }



}

