package Volby;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Komparátor na porovnávanie objektov podľa vlastnosti typu Integer.
 */
public class IntPropertyComparator<T> implements Comparator<T> {
    private final Function<T, Integer> propertyExtractor;

    /**
     * Konštruktor triedy.
     *
     * @param propertyExtractor funkcia na extrahovanie vlastnosti typu Integer z objektu
     */
    public IntPropertyComparator(Function<T, Integer> propertyExtractor) {
        this.propertyExtractor = propertyExtractor;
    }

    /**
     * Metóda na porovnanie dvoch objektov na základe ich int hodnoty.
     *
     * @param obj1 prvý porovnávaný objekt
     * @param obj2 druhý porovnávaný objekt
     * @return výsledok porovnania
     */
    @Override
    public int compare(T obj1, T obj2) {
        int int1 = propertyExtractor.apply(obj1);
        int int2 = propertyExtractor.apply(obj2);
        return Integer.compare(int1, int2); // Porovnanie hodnôt
    }
}
