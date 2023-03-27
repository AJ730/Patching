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
    static HashMap<String[], Double> topSelection;
    static ArrayList<String[]> crossover;
    static ArrayList<String[]> mutated;
    static HashMap<String[], Double> mutationScores;
    static String[] bestResult;
    static String[] overallBestResult;
    static final String[] opsString = new String[]{"==", "!=", "<=", "<", ">", ">="};

    static void initialize() {
        // initialize the population based on OperatorTracker.operators
        opsMap = new HashMap<>();
        population = new HashMap<>();
        topSelection = new HashMap<>();
        crossover = new ArrayList<>();
        mutated = new ArrayList<>();
        mutationScores = new HashMap<>();
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
        population.put(0, OperatorTracker.operators);
        for (int i = 1; i<size; i++){
            String[] operatorList = new String[OperatorTracker.operators.length];
            for (int j=0; j<operatorList.length; j++){
                operatorList[j] = OperatorTracker.operators[r.nextInt(OperatorTracker.operators.length)];
            }
            population.put(i, operatorList);
        }
    }

    static void populationSelection(int sizeLim) {
        for (int i = 0; i < population.size(); i++) {
            OperatorTracker.operators = population.get(i);
            List<Boolean> selection = OperatorTracker.runAllTests();
            double fitnessValSelection = getFitness(selection);
            topSelection.put(population.get(i), fitnessValSelection);
        }
        int limiter = topSelection.size() - sizeLim;

        for (int j = 0; j < limiter; j++) {
            double min = Collections.min(topSelection.values());
            ArrayList<String[]> funnyCode = new ArrayList<>();
            for (String[] entry : topSelection.keySet()) {
                if (topSelection.get(entry).equals(min)) {
                    funnyCode.add(entry);
                    break;
                }
            }
            for (String[] haha : funnyCode) {
                topSelection.remove(haha);
            }
        }
    }

    static void populationCrossover() {
        int cutoff = r.nextInt(OperatorTracker.operators.length);
        List<String[]> l = new ArrayList<>(topSelection.keySet());
        int limiter = topSelection.size()-1;
        for (int i = 0; i<limiter; i++) {
            for (int j =1; j<=limiter; j++) {
                if (i!=j) {
                    List<String> parent1 = Arrays.asList(l.get(i));
                    List<String> parent2 = Arrays.asList(l.get(j));
                    ArrayList<String> sub1 = new ArrayList<>();
                    sub1.addAll(parent1.subList(0, cutoff));
                    sub1.addAll(parent2.subList(cutoff, parent2.size()));
                    ArrayList<String> sub2 = new ArrayList<>();
                    sub2.addAll(parent2.subList(0, cutoff));
                    sub2.addAll(parent1.subList(cutoff, parent1.size()));
                    String[] child1 = sub1.toArray(new String[0]);
                    String[] child2 = sub2.toArray(new String[0]);
                    crossover.add(child1);
                    crossover.add(child2);
                }
            }
        }
    }

    static void populationMutation(double treshold) {
        // This part loops through the crossover children and mutates them
        for (String[] ele : crossover){
            HashMap<String, Double> tarscores = new HashMap<>();
            List<String> ops = Arrays.asList(ele);
            OperatorTracker.operators = ele;
            List<Boolean> mutation = OperatorTracker.runAllTests();
            for (String s: opsMap.keySet()){
                tarscores.put(s,tarantulaScore(s, mutation));
            }
            for (String t: tarscores.keySet()){
                if (tarscores.get(t) > treshold){
                    String mutated = opsString[r.nextInt(opsString.length)];
                    for (int i = 0; i<ops.size(); i++){
                        if (ops.get(i).equals(t)){
                            float chance = r.nextFloat();
                            if (chance<0.05){
                                ops.set(i,mutated);
                            }
                        }
                    }
                }
            }
            // Calculates the fitness of the mutated stuff and adds them to mutationScores
            String[] brokencode = ops.toArray(new String[0]);
            OperatorTracker.operators = brokencode;
            List<Boolean> fitnessOfMutated = OperatorTracker.runAllTests();
            mutationScores.put(brokencode, getFitness(fitnessOfMutated));
        }
        // Dumb method to add the highest fitness scoring result to the bestresult
        double max = Collections.min(mutationScores.values());
        for (String[] entry : topSelection.keySet()) {
            if (topSelection.get(entry).equals(max)) {
                bestResult = entry;
            }
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
        overallBestResult = OperatorTracker.operators;
        while (!isFinished) {
            // Do things!
            // !!!!!!Set population size here!!!!!!
            generatePopulation(20);
            System.out.println("Population: "+population);
            populationSelection(3);
            System.out.println("Selection: "+topSelection);
            populationCrossover();
            System.out.println("Crossover: "+ crossover);
            populationMutation(0.50);
            System.out.println("Result of Mutation" + mutationScores);
            OperatorTracker.operators = bestResult;
            double fitnessVal1 = getFitness(OperatorTracker.runAllTests());
            if ( fitnessVal > fitnessVal1){
                bestResult = overallBestResult;
                OperatorTracker.operators = bestResult;
                System.out.println("Fitness Value has not improved over original = " + fitnessVal);
            }
            else{
            System.out.println("New Fitness Value after a run = " + fitnessVal1);}
            crossover.clear();
            mutationScores.clear();
        }
    }

    public static void output(String out) {
        // This will get called when the problem code tries to print things,
        // the prints in the original code have been removed for your convenience

        // System.out.println(out);
    }

    // Custom methods
}