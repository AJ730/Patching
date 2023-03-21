package nl.tudelft.instrumentation.symbolic;

import java.util.Objects;

public class Pair  {
    int size;
    boolean triggered;


    @Override
    public String toString() {
        return "{lineNmr:" + size +
                ", branch triggered:" + triggered;
    }

    Pair(int size, boolean triggered) {
        this.triggered = triggered;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Double.compare(pair.size, this.size) == 0 && this.triggered == pair.triggered;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, triggered);
    }


}