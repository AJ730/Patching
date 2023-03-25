package nl.tudelft.instrumentation.patching;
import com.google.common.collect.Lists;
import org.checkerframework.checker.units.qual.A;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.frequency;

public class PatchingLab {

    static Random r = new Random();
    static boolean isFinished = false;
    static HashMap<String, HashSet<Integer>> opsMap;
    static HashMap<Integer, String[]> population;
    static HashMap<Double, String[]> topSelection;
    static HashMap<Integer, String[]> crossover;

    static void initialize() {
        // initialize the population based on OperatorTracker.operators
        opsMap = new HashMap<>();
        population = new HashMap<>();
        topSelection = new HashMap<>();
        crossover = new HashMap<>();
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

    static void generatePopulation(int size) {
        for (int i = 0; i<size; i++){
            String[] operatorList = new String[OperatorTracker.operators.length];
            for (int j=0; j<operatorList.length; j++){
                operatorList[j] = OperatorTracker.operators[r.nextInt(OperatorTracker.operators.length)];
            }
            population.put(i, operatorList);
        }
    }

    static void populationSelection(int sizeLim) {
        for (int i = 0; i<population.size(); i++){
            OperatorTracker.operators = population.get(i);
            List<Boolean> selection = OperatorTracker.runAllTests();
            double fitnessValSelection = getFitness(selection);
            topSelection.put(fitnessValSelection, population.get(i));
        }
        List<Double> l = new ArrayList<Double>(topSelection.keySet());
        Collections.sort(l);
        int limiter = topSelection.size()-sizeLim;
        for (int j= 0; j< limiter; j++){
            topSelection.remove(l.get(j));
        }
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
        System.out.println("Initial Fitness Value = " + fitnessVal);
        while (!isFinished) {
            // Do things!
            // !!!!!!Set population size here!!!!!!
            generatePopulation(20);
            System.out.println("Population: "+population);
            populationSelection(3);
            System.out.println("Selection: "+topSelection);
            break;
        }
    }

    public static void output(String out) {
        // This will get called when the problem code tries to print things,
        // the prints in the original code have been removed for your convenience

        // System.out.println(out);
    }
}