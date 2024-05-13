package Volby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ZakladnaAnalyzaVolieb - výpočty pre zoradenie strán a kandidatov poľa počtu hlasov.
 */
public class ZakladnaAnalyzaVolieb {
    protected Volby volby; // Inštancia objektu Volby pre analýzu
    protected int pocethlasov; // Celkový počet odovzdaných hlasov
    protected ArrayList<String> poradieS; // Zoznam strán a ich poradie podľa počtu získaných hlasov
    protected ArrayList<String> poradieK; // Zoznam kandidátov a ich poradie podľa počtu získaných hlasov

    /**
     * Konštruktor triedy pre základnú analýzu volieb.
     *
     * @param v objekt reprezentujúci voľby
     */
    public ZakladnaAnalyzaVolieb(Volby v) {
        this.volby = v; 
        pocet_hlasov_spolu(); 
        poradie_stran(); 
        poradie_kandidatov(); 
    }
    
    /**
     * Metóda na získanie celkového počtu odovzdaných hlasov.
     *
     * @return celkový počet odovzdaných hlasov
     */
    public int pocetHlasov() {
        return pocethlasov;
    }
    
    /**
     * Metóda na získanie zoznamu strán a ich poradia podľa počtu získaných hlasov.
     *
     * @return zoznam strán a ich poradie podľa počtu získaných hlasov
     */
    public ArrayList<String> poradieStran() {
        return poradieS;
    }
    
    /**
     * Metóda na získanie zoznamu kandidátov a ich poradia podľa počtu získaných hlasov.
     *
     * @return zoznam kandidátov a ich poradie podľa počtu získaných hlasov
     */
    public ArrayList<String> poradieKandidatov() {
        return poradieK;
    }
    
    /**
     * Metóda na výpočet celkového počtu odovzdaných hlasov.
     */
    protected void pocet_hlasov_spolu() {
        this.pocethlasov = 0; 
        List<Pstrana> stranypor = new ArrayList<>(this.volby.pstrany);

        // Prechádzanie zoznamom strán a sčítanie počtu hlasov
        for (Pstrana pstr : stranypor) {
            this.pocethlasov += pstr.getPocet_h();
        }
    }
    
    /**
     * Metóda na zistenie poradia strán podľa počtu získaných hlasov.
     */
    private void poradie_stran() {
        this.poradieS = new ArrayList<>(); // Inicializácia zoznamu pre poradie strán
        List<Pstrana> stranypor = new ArrayList<>(this.volby.pstrany); // Zoznam politických strán

        // Komparátor na porovnávanie počtu hlasov v objektoch Pstrana
        IntPropertyComparator<Pstrana> comparator = new IntPropertyComparator<>(Pstrana::getPocet_h);

        // Zoradenie strán podľa počtu získaných hlasov zostupne
        Collections.sort(stranypor, comparator.reversed());

        int i = 1; 
        // Naplnenie zoznamu s poradím strán
        for (Pstrana pstr : stranypor) {
            this.poradieS.add((i++) + ". " + pstr.getNazov() + " (" + pstr.getPocet_h() + ")");
        }
    }
    
    /**
     * Metóda na zistenie poradia kandidátov podľa počtu získaných hlasov.
     */
    public void poradie_kandidatov() {
        ArrayList<Kandidat> z_kandidatov = new ArrayList<>(); // Zoznam kandidátov

        // Prechádzanie politických strán a získanie zoznamu kandidátov
        for (Pstrana s : this.volby.pstrany) {
            z_kandidatov.addAll(s.getKandidati());
        }

        this.poradieK = new ArrayList<>();

        int i = 1; 
        IntPropertyComparator<Kandidat> comparator = new IntPropertyComparator<>(Kandidat::getPocet_h);
        // Zoradenie kandidátov podľa počtu získaných hlasov zostupne
        Collections.sort(z_kandidatov, comparator.reversed());
        // Naplnenie zoznamu s poradím kandidátov
        for (Kandidat pkan : z_kandidatov) {
            this.poradieK.add((i++) + ". " + pkan.getInfo() + " (" + pkan.getPocet_h() + ")");
        }
    }
}
