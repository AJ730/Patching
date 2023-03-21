package nl.tudelft.instrumentation.patching;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.*;

import static java.util.Collections.frequency;

public class PatchingLab {

    static Random r = new Random();
    static boolean isFinished = false;

    static HashMap<String, HashSet<Integer>> opsMap;


    static void initialize() {
        // initialize the population based on OperatorTracker.operators
        opsMap = new HashMap<>();
    }

    // encounteredOperator gets called for each operator encountered while running tests
    static boolean encounteredOperator(String operator, int left, int right, int operator_nr) {
        // Do something useful

        String replacement = OperatorTracker.operators[operator_nr];

        opsMap.computeIfAbsent(replacement, k -> new HashSet<>());
        opsMap.get(replacement).add(OperatorTracker.current_test); // the


        if (replacement.equals("!=")) return left != right;
        if (replacement.equals("==")) return left == right;
        if (replacement.equals("<")) return left < right;
        if (replacement.equals(">")) return left > right;
        if (replacement.equals("<=")) return left <= right;
        if (replacement.equals(">=")) return left >= right;
        return false;
    }

    static boolean encounteredOperator(String operator, boolean left, boolean right, int operator_nr) {
        // Do something useful
        String replacement = OperatorTracker.operators[operator_nr];

        opsMap.computeIfAbsent(replacement, k -> new HashSet<>());
        opsMap.get(replacement).add(OperatorTracker.current_test);

        if (replacement.equals("!=")) return left != right;
        if (replacement.equals("==")) return left == right;
        return false;
    }

    static double getFitness(List<Boolean> tests) {
        return frequency(tests, true) / (double) tests.size();
    }

    static double tarantulaScore(String operator, List<Boolean> tests) {
        int totalFailed = frequency(tests, false);
        int totalPassed = frequency(tests, true);

        double failed = frequency(opsMap.get(operator), false);
        double top = failed / totalFailed;

        double passed = frequency(opsMap.get(operator), true);

        return top + passed / totalPassed;
    }

    static void run() {
        initialize();

        // Place the code here you want to run once:
        // You want to change this of course, this is just an example
        // Tests are loaded from resources/rers2020_test_cases. If you are you are using
        // your own tests, make sure you put them in the same folder with the same
        // naming convention.

        System.out.println("Entered run");
        List<Boolean> tests = OperatorTracker.runAllTests();

        double fitnessVal = getFitness(tests);
        System.out.println("Fitness Value = " + fitnessVal);


        while (!isFinished) {
            // Do things!
            try {
                System.out.println("Woohoo, looping!");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void output(String out) {
        // This will get called when the problem code tries to print things,
        // the prints in the original code have been removed for your convenience

        // System.out.println(out);
    }
}