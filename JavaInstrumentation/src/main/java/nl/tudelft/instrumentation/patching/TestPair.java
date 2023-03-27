package nl.tudelft.instrumentation.patching;

import java.util.Objects;

public class TestPair {
     int nmrPositiveTests;
     int nmrNegativeTests;

    public TestPair(int nmrPositiveTests, int nmrNegativeTests){
        this.nmrPositiveTests = nmrPositiveTests;
        this.nmrNegativeTests = nmrNegativeTests;
    }

    public void setNmrNegativeTests(int nmrNegativeTests) {
        this.nmrNegativeTests = nmrNegativeTests;
    }

    public void setNmrPositiveTests(int nmrPositiveTests) {
        this.nmrPositiveTests = nmrPositiveTests;
    }

    @Override
    public String toString() {
        return "TestPair{" +
                "nmrPositiveTests=" + nmrPositiveTests +
                ", nmrNegativeTests=" + nmrNegativeTests +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestPair testPair = (TestPair) o;
        return nmrPositiveTests == testPair.nmrPositiveTests && nmrNegativeTests == testPair.nmrNegativeTests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nmrPositiveTests, nmrNegativeTests);
    }
}
