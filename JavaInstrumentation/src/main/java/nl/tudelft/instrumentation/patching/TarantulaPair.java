package nl.tudelft.instrumentation.patching;

import java.util.Comparator;
import java.util.Objects;

public class TarantulaPair implements Comparable<TarantulaPair> {

    int index;
    String operator;

    double tarantulaScore;

    public TarantulaPair(int index, String operator, double tarantulaScore) {
        this.index = index;
        this.operator = operator;
        this.tarantulaScore = tarantulaScore;
    }

    @Override
    public String toString() {
        return "TarantulaPair{" +
                "operator=" + operator +
                ", tarantulaScore=" + tarantulaScore +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TarantulaPair that = (TarantulaPair) o;
        return Double.compare(that.tarantulaScore, tarantulaScore) == 0 && Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, tarantulaScore);
    }

    @Override
    public int compareTo(TarantulaPair o) {
        return Double.compare(this.tarantulaScore , o.tarantulaScore);
    }
}
