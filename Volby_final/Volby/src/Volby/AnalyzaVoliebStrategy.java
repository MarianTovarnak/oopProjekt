package Volby;

/**
 * Rozhranie definujúce stratégiu analýzy volieb.
 */
public interface AnalyzaVoliebStrategy {
  
	/**
     * Metóda na vykonanie analýzy volieb.
     * 
     * @param volby objekt reprezentujúci volby, na ktoré sa má analýza vykonať
     * @return výsledok analýzy vo forme reťazca
     */
    String vykonajAnalyzu(Volby volby);
}
