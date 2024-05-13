package Volby;

import java.util.ArrayList;

/**
 * Trieda implementujúca rozhranie AnalyzaVoliebStrategy, zodpovedná za detailnú analýzu volieb.
 */
public class DetailnaAnalyzaStrategy implements AnalyzaVoliebStrategy {
    
    /**
     * Metóda vykonáva detailnú analýzu volieb na základe poskytnutých dát.
     *
     * @param volby Objekt obsahujúci údaje o voľbách
     * @return Výsledok detailnej analýzy voľieb vo forme reťazca
     */
    @Override
    public String vykonajAnalyzu(Volby volby) {
        // Inicializácia premennej pre výstupný text
    	String outtext="";
        
        // Vytvorenie objektu pre detailnú analýzu volieb
        DetailnaAnalyzaVolieb analyzaD = new DetailnaAnalyzaVolieb(volby);

        // Hlavička výstupného textu pre detailnú analýzu volieb
        outtext += "Detailný výpis výsledkov volieb - obsadenosť parlamentu poslancami.\n";
        outtext += "-----------------------------------------------------------------------------\n";
        outtext += "Výsledky volieb a rozdelenie " + analyzaD.getPocetM() + " mandátov.\n";
        
        // Výpis počtu odovzdaných hlasov
        outtext += "Počet všetkých odovzdaných hlasov vo voľbách: " + analyzaD.pocetHlasov() + "\n\n";

        // Kontrola, či je dostatočný počet hlasov na zostavenie parlamentu min 10
        if(analyzaD.getPocetM()>analyzaD.pocetHlasov()) {
        	outtext += "Málo hlasov na zostavenie parlamentu.\n";
        } else {
            // Získanie poradia strán a zvolených kandidátov
            ArrayList<String> poradieStr = analyzaD.poradieStran();
            ArrayList<String> poradieKd = analyzaD.poradieKandidatov();
    
            // Ak sú zoznamy strán a kandidátov neprázdne, vypíšeme ich
            if (poradieStr != null && !poradieStr.isEmpty() && poradieKd != null && !poradieKd.isEmpty()) {
            	outtext += "Poradie strán s počtom získaných poslaneckých miest:\n";
                for (String pstr : poradieStr) {
                	outtext += pstr + "\n";
                }
    
                outtext += "\nZoznam zvolených poslancov (za ktorú stranu):\n";
                for (String pk : poradieKd) {
                	outtext += pk + "\n";
                }
            } else {
            	outtext += "Málo hlasov na zostavenie parlamentu.\n";
            }
        }
        return outtext; // Vrátenie výstupného textu
    }

}
