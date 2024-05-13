package Volby;

import java.util.ArrayList;

/**
 * Stratégia pre vykonanie základnej analýzy volieb.
 */
public class ZakladnaAnalyzaStrategy implements AnalyzaVoliebStrategy {
	/**
     * Vykoná základnú analýzu volieb.
     *
     * @param volby objekt reprezentujúci voľby
     * @return výsledok základnej analýzy vo formáte textu
     */
    @Override
    public String vykonajAnalyzu(Volby volby) {
        
        String outtext = ""; // Premenná na ukladanie výstupného textu analyzy
        ZakladnaAnalyzaVolieb analyza = new ZakladnaAnalyzaVolieb(volby); // Vytvorenie inštancie triedy pre základnú analýzu volieb

        // Hlavička výpisu analýzy
        outtext += "Základný výpis výsledkov volieb - strany a kandidáti podľa počtu hlasov.\n";
        outtext += "--------------------------------------------------------------------------------\n";
        outtext += "Počet všetkých odovzdaných hlasov vo voľbách: " + analyza.pocetHlasov() + "\n\n";

        ArrayList<String> poradieStrd = analyza.poradieStran(); // Získanie poradia strán podľa počtu získaných hlasov

        // Ak je zoznam strán neprázdny, vypíšeme poradie strán a poradie kandidátov podľa získaných hlasov
        if (poradieStrd != null && !poradieStrd.isEmpty()) {
            outtext += "Poradie strán podľa počtu získaných hlasov:\n";
            for (String pstr : poradieStrd) {
                outtext += pstr + "\n";
            }

            outtext += "\nZoznam kandidatov podľa počtu získaných hlasov:\n";
            ArrayList<String> poradieKd = analyza.poradieKandidatov(); // Získanie poradia kandidátov podľa počtu získaných hlasov
            if (poradieKd != null && !poradieKd.isEmpty()) {
                for (String pk : poradieKd) {
                    outtext += pk + "\n";
                }
            } else {
                outtext += "Málo hlasov na zostavenie parlamentu.\n";
            }
        } else {
            outtext += "Málo hlasov na zostavenie parlamentu.\n"; 
        }
        return outtext; // Vrátenie výstupného textu analyzy
    }
}
