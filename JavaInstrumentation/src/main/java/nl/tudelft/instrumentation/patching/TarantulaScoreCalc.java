package nl.tudelft.instrumentation.patching;

import java.util.ArrayList;
import java.util.HashSet;

import static java.util.Collections.frequency;
import static nl.tudelft.instrumentation.patching.PatchingLab.opsMap;
import static nl.tudelft.instrumentation.patching.PatchingLab.testResults;

public class TarantulaScoreCalc {


    static final double epsilon = 0.00001;
    /**
     * Get results for an operator
     * @param testsForOperator testsForOperator
     * @return operator
     */
    public static ArrayList<Boolean> getResultsForOp( HashSet<Integer> testsForOperator) {
        ArrayList<Boolean> resultsForOperator = new ArrayList<>();

        for(Integer index : testsForOperator){
            resultsForOperator.add(testResults.get(index));
        }

        return resultsForOperator;
    }


    public static double tarantulaScore(String operatorNumber) {

        int totalFailed = frequency(testResults, false);
        int totalPassed = frequency(testResults, true);

        ArrayList<Boolean> resultForOp = getResultsForOp(opsMap.get(operatorNumber));

        double failed = frequency(resultForOp, false);
        double top = failed / (totalFailed+epsilon);


        double passed = frequency(resultForOp, true);
        return top  / (top+epsilon + (passed/(totalPassed+epsilon)));
    }

}
