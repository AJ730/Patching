package nl.tudelft.instrumentation.patching;

import java.util.Objects;

public class FitnessPair implements Comparable<FitnessPair>{

    int individual;

    double fitness;

    public FitnessPair(int individual, double fitness) {
        this.individual = individual;
        this.fitness = fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FitnessPair that = (FitnessPair) o;
        return individual == that.individual && Double.compare(that.fitness, fitness) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual, fitness);
    }

    @Override
    public int compareTo(FitnessPair o) {
        return Double.compare(this.fitness, o.fitness);
    }
}
