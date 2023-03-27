package nl.tudelft.instrumentation.patching;

import java.util.HashSet;
import static nl.tudelft.instrumentation.patching.PatchingLab.opsMap;
import static nl.tudelft.instrumentation.patching.PatchingLab.testResults;

public class FitnessCalc {


    public FitnessCalc() {
    }

    /**
     * Get pased tests from an individual
     *
     * @param individual Individual
     * @return number of passed tests
     */
    static int getPassedTestsFromIndividual(Individual individual) {

        int acc = 0;
        HashSet<Integer> tests = new HashSet<>();

        for (String operator : individual.getGenes()) {
            for (int testInx : opsMap.get(operator)) {
                if (tests.contains(testInx)) continue;
                if (testResults.get(testInx)) acc++;
                tests.add(testInx);
            }

        }

        return acc;
    }

    /**
     * Get fitness of an individual
     *
     * @param individual Individual
     * @return fitness
     */
    static double getFitness(Individual individual) {
        int passed = getPassedTestsFromIndividual(individual);
        return passed / (double) OperatorTracker.tests.size();
    }


}
