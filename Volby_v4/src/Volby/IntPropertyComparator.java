package Volby;

import java.util.Comparator;
import java.util.function.Function;

public class IntPropertyComparator<T> implements Comparator<T> {
    private final Function<T, Integer> propertyExtractor;

    public IntPropertyComparator(Function<T, Integer> propertyExtractor) {
        this.propertyExtractor = propertyExtractor;
    }

    @Override
    public int compare(T obj1, T obj2) {
        int int1 = propertyExtractor.apply(obj1);
        int int2 = propertyExtractor.apply(obj2);
        return Integer.compare(int1, int2);
    }
}