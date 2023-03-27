package nl.tudelft.instrumentation.patching;

import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.frequency;

public class PatchingLab {

        static boolean isFinished = false;

        static HashMap<String, HashSet<Integer>> opsMap; //operator, List<test Nmrs>

        static List<Boolean> testResults;

        static int generationNmr = 0;

        private static final double uniformRate = 0.5;
        private static final int tournamentSize = 10;
        private static final boolean elitism = true;
        private static final int topN = 5;
        private static  Population current;

        static final String[] ops = new String[]{"==", "!=", "<=", "<", ">", ">="};

        /**
         * Initialize the population
         */
        static void initialize() {
                // initialize the population based on OperatorTracker.operators
                opsMap = new HashMap<>();
                current = new Population(20, true);
        }



        /**
         * Encountered new operator for ints.
         * @param operator operator
         * @param left left
         * @param right right
         * @param operator_nr operator_nr
         * @return if replacement equals operator
         */
        // encounteredOperator gets called for each operator encountered while running tests
        static boolean encounteredOperator(String operator, int left, int right, int operator_nr) {
                // Do something useful

                String replacement = OperatorTracker.operators[operator_nr];

                if(opsMap.get(replacement) == null){
                        opsMap.put(replacement, new HashSet<>());
                }
                else {
                        opsMap.get(replacement).add(OperatorTracker.current_test);
                }


                if (replacement.equals("!=")) return left != right;
                if (replacement.equals("==")) return left == right;
                if (replacement.equals("<")) return left < right;
                if (replacement.equals(">")) return left > right;
                if (replacement.equals("<=")) return left <= right;
                if (replacement.equals(">=")) return left >= right;
                return false;
        }

        /**
         * Encountered New operator for boolean
         * @param operator operator
         * @param left left
         * @param right right
         * @param operator_nr operator_nr
         * @return if replacement equals operator
         */
        static boolean encounteredOperator(String operator, boolean left, boolean right, int operator_nr) {
                // Do something useful
                String replacement = OperatorTracker.operators[operator_nr];

                if(opsMap.get(replacement) == null){
                        opsMap.put(replacement, new HashSet<>());
                }
                else {
                        opsMap.get(replacement).add(OperatorTracker.current_test);
                }

                if (replacement.equals("!=")) return left != right;
                if (replacement.equals("==")) return left == right;
                return false;
        }






        //////////////////////////////GA Methods/////////////////////////////////////////////////////////////

        public static Population evolvePopulation(Population pop) {
                Population newPopulation = new Population(pop.size(), false);

                // Keep our best individual
                if (elitism) {
                        newPopulation.saveIndividual(0, pop.getFittest());
                }

                // Crossover population
                int elitismOffset;
                if (elitism) {
                        elitismOffset = 1;
                } else {
                        elitismOffset = 0;
                }
                // Loop over the population size and create new individuals with
                // crossover
                for (int i = elitismOffset; i < pop.size(); i++) {
                        Individual indiv1 = tournamentSelection(pop);
                        Individual indiv2 = tournamentSelection(pop);
                        Individual newIndiv = crossover(indiv1, indiv2);
                        newPopulation.saveIndividual(i, newIndiv);
                }

                // Mutate population
                for (int i = elitismOffset; i < newPopulation.size(); i++) {
                        mutate(newPopulation.getIndividual(i));
                }


                return newPopulation;
        }





        ////////////////////////////////////////////Core methods////////////////////////////////////////////////


        private static Individual tournamentSelection(Population pop) {
                // Create a tournament population
                Population tournament = new Population(tournamentSize, false);
                // For each place in the tournament get a random individual
                for (int i = 0; i < tournamentSize; i++) {
                        int randomId = (int) (Math.random() * pop.size());
                        tournament.saveIndividual(i, pop.getIndividual(randomId));
                }
                // Get the fittest
                return tournament.getFittest();
        }


        private static Individual crossover(Individual indiv1, Individual indiv2) {
                Individual newSol = new Individual(indiv1.id + indiv2.id);
                // Loop through genes
                for (int i = 0; i < indiv1.size(); i++) {
                        // Crossover
                        if (Math.random() <= uniformRate) {
                                newSol.setGene(i, indiv1.getGene(i));
                        } else {
                                newSol.setGene(i, indiv2.getGene(i));
                        }
                }
                return newSol;
        }

        // Mutate an individual
        private static void mutate(Individual indiv) {
                // Loop through genes
                ArrayList<Integer> susGenesIndexes = indiv.getTopNSusGenes(topN);

                String mutated = ops[new Random().nextInt(ops.length)];

                int replace = new Random().nextInt(susGenesIndexes.size());

                indiv.setGene(replace, mutated);
        }




        ///////////////////////////////////////////////EntryPoint////////////////////////////

        static void run() {
                System.out.println("Started run");
                initialize();

                while (!isFinished) {
//                        OperatorTracker.operators = current.

                        testResults = OperatorTracker.runAllTests();

                        System.out.println("Generation: " + generationNmr + " Fittest: " + current.getFittest().getFitness());
//
//                        for(Individual in : current.individuals){
//                                System.out.println("Individual" +in+ in.getFitness());
//                        }

                        current = evolvePopulation(current);
                        generationNmr++;
                        opsMap.clear();

                }
        }



        public static void output(String out) {
                // This will get called when the problem code tries to print things,
                // the prints in the original code have been removed for your convenience

                // System.out.println(out);
        }
}