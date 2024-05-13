package Volby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Trieda implementujúca detailnú analýzu volieb rozšírením základnej triedy.
 */
public class DetailnaAnalyzaVolieb extends ZakladnaAnalyzaVolieb {
    private int pocetM = 10; // Počet mandátov
    private int prideleneM = 0; // Počet pridelených mandátov
    private double repvolcis; // Reálny počet voličov na mandát
    private List<Pstrana> pstranypor; // Zoradený zoznam politických strán
    private Map<Integer, Integer> pocetMpreS; // Mapa pridelených mandátov pre každú stranu
    private Map<Integer, Integer> pocetMpreZ; // Mapa zvyškov mandátov pre každú stranu

    /**
     * Konštruktor triedy.
     *
     * @param v objekt volieb
     */
    public DetailnaAnalyzaVolieb(Volby v) {
        super(v); // Volanie konštruktora nadradenej triedy
        this.repvolcis = (double) this.pocethlasov / (double) (this.pocetM + 1); // Výpočet počtu voličov na mandát
        this.pocetMpreS = new HashMap<>(); // Inicializácia mapy pridelených mandátov pre strany
        this.pocetMpreZ = new HashMap<>(); // Inicializácia mapy zvyškov mandátov pre strany
        pocet_mandatov(); // Metóda na pridelenie mandátov
        if (pocetM > prideleneM) { // Ak je ešte nedostatočný počet mandátov, rozdeľujeme zvyšky
            rozdel_zvysky();
        }
        this.poradieS = new ArrayList<>(); // Inicializácia zoznamu poradia strán
        this.poradieK = new ArrayList<>(); // Inicializácia zoznamu poradia kandidátov
        zoznam_kandidatov(); // Metóda na zostavenie zoznamu kandidátov
    }

    /**
     * Metóda na vrátenie počtu mandátov.
     *
     * @return počet mandátov
     */
    public int getPocetM() {
        return this.pocetM;
    }

    /**
     * Metóda na pridelenie mandátov stranám na základe počtu hlasov.
     */
    public void pocet_mandatov() {
        pstranypor = new ArrayList<>(super.volby.pstrany); // Získanie zoznamu strán
        IntPropertyComparator<Pstrana> comparator = new IntPropertyComparator<>(Pstrana::getPocet_h); // Komparátor na porovnávanie strán podľa počtu hlasov
        Collections.sort(pstranypor, comparator.reversed()); // Zoradenie strán zostupne podľa počtu hlasov

        for (Pstrana pstr : pstranypor) {
            if (this.repvolcis > 0 && (this.pocetM > this.prideleneM)) { // Ak je ešte dostatok mandátov a voličov
                double pocetk = (double) (pstr.getPocet_h() / this.repvolcis); // Vypočítame počet mandátov pre danú stranu
                int pocetz = (int) ((pocetk - (int) pocetk) * 100); // Získame desatinnú časť
                this.pocetMpreS.put(pstr.getIdStrana(), (int) pocetk); // Pridáme počet celých mandátov do mapy
                this.pocetMpreZ.put(pstr.getIdStrana(), pocetz); // Pridáme desatinnú časť do mapy
                this.prideleneM += pocetk; // Pridáme počet mandátov k celkovému počtu
            }
        }
    }

    /**
     * Metóda na rozdelenie zvyškov mandátov medzi strany.
     */
    private void rozdel_zvysky() {
        List<Map.Entry<Integer, Integer>> zoznam = new ArrayList<>(pocetMpreZ.entrySet()); // Vytvorenie zoznamu zvyškov mandátov

        Collections.sort(zoznam, new Comparator<Map.Entry<Integer, Integer>>() { // Zoradenie zvyškov zostupne
            @Override
            public int compare(Map.Entry<Integer, Integer> entry1, Map.Entry<Integer, Integer> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });

        Map<Integer, Integer> usporadaneHodnoty = new LinkedHashMap<>(); // Usporiadanie hodnôt do mapy
        for (Map.Entry<Integer, Integer> entry : zoznam) {
            usporadaneHodnoty.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Integer, Integer> entry : usporadaneHodnoty.entrySet()) { // Rozdelenie zvyškov mandátov
            if ((entry.getValue() > 0) && (this.pocetM > this.prideleneM)) {
                int idstr = entry.getKey();
                this.pocetMpreS.put(idstr, this.pocetMpreS.get(idstr) + 1);
                this.prideleneM++;
            }
        }
    }

    /**
     * Metóda na zostavenie zoznamu kandidátov pre každú stranu.
     */
    public void zoznam_kandidatov() {
        int j = 1; // Inicializácia čítača pre strany
        int p = 1; // Inicializácia čítača pre kandidátov
        for (Pstrana pstr : pstranypor) { // Pre každú stranu
            ArrayList<Kandidat> z_kandidatov = new ArrayList<>(); // Získanie zoznamu kandidátov pre danú stranu
            z_kandidatov = pstr.getKandidati(); // Získanie zoznamu kandidátov
            IntPropertyComparator<Kandidat> comparator = new IntPropertyComparator<>(Kandidat::getPocet_h); // Komparátor na porovnávanie kandidátov podľa počtu hlasov
            Collections.sort(z_kandidatov, comparator.reversed()); // Zoradenie kandidátov zostupne podľa počtu hlasov
            if (this.pocetMpreS.containsKey(pstr.getIdStrana())) { // Ak má strana pridelené mandáty
                int pk = this.pocetMpreS.get(pstr.getIdStrana()); // Získanie počtu pridelených mandátov pre danú stranu
                this.poradieS.add((j++) + ". " + pstr.getNazov() + " (" + pk + ")"); // Pridanie názvu strany do zoznamu
                int i = 1;
                for (Kandidat pkan : z_kandidatov) { // Pre každého kandidáta
                    if (i <= pk) { // Ak ešte má strana pridelené mandáty
                        this.poradieK.add((p++) + ". " + pkan.getInfo() + " (" + pkan.getPocet_h() + ") " + pstr.getSkratka()); // Pridanie kandidáta do zoznamu
                        i++;
                    }
                }
            }
        }
    }
}
