package fr.anarchick.anapi.java;

public record Pair<K, V>(K first, V second) {

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) return false;
        if (o instanceof Pair obj) {
            return (obj.first == first && obj.second == second);
        }
        return false;
    }

    @Override
    public Pair<K, V> clone() {
        return new Pair(first, second);
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    @Override
    public String toString() {
        return "Pair(" + first + ", " + second + ")";
    }

    /**
     * @return New instance with inverted values
     */
    public Pair<V, K> invert() {
        return new Pair<V, K>(second, first);
    }

}
