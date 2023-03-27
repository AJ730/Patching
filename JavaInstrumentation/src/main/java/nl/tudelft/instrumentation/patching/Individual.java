package nl.tudelft.instrumentation.patching;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;


import static nl.tudelft.instrumentation.patching.PatchingLab.ops;
import static nl.tudelft.instrumentation.patching.TarantulaScoreCalc.tarantulaScore;

public class Individual {

    static int defaultGeneLength = ops.length;

    private String[] genes = new String[defaultGeneLength];
    int id;
    // Cache
    private int fitness = 0;

    public Individual(int id){
        this.id = id;
    }

    // Create a random individual
    public void generateIndividual() {
        for (int i = 0; i < size(); i++) {
            String gene = ops[Math.round(new Random().nextInt(defaultGeneLength))];
            genes[i] = gene;
        }
    }

    /* Public methods */

    public String getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, String  value) {
        genes[index] = value;
        fitness = 0;
    }



    public String[] getGenes() {
        return genes;
    }

    public int size() {
        return genes.length;
    }

    public double getFitness() {
        return FitnessCalc.getFitness(this);
    }

    public ArrayList<Integer> getTopNSusGenes(int topN){

        ArrayList<TarantulaPair> genesT = new ArrayList<>();
        for(int i = 0; i < this.size(); i++){
            genesT.add(new TarantulaPair(i, genes[i], tarantulaScore(genes[i])));
        }

        ArrayList<Integer> mutatedTarantulaScore = genesT.stream()
                .sorted((a,b)->Double.compare(b.tarantulaScore, a.tarantulaScore))
                .limit(topN).map(a -> a.index)
                .collect(Collectors.toCollection(ArrayList::new));

        return mutatedTarantulaScore;
    }



    @Override
    public String toString() {

        return ""+id;
    }
}