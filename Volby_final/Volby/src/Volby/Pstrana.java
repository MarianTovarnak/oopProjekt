package Volby;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Trieda reprezentujúca politickú stranu, rozšírená o možnosť serializácie.
 */
public class Pstrana implements Serializable {
    private int id; // Identifikátor strany
    private String nazov; // Názov strany
    private String skratka; // Skratka strany
    private int pocet_h; // Počet získaných hlasov strany
    private ArrayList<Kandidat> kandidati; // Zoznam kandidátov strany

    /**
     * Konštruktor triedy s parametrami ID, názvom a skratkou strany.
     *
     * @param id       identifikátor strany
     * @param n        názov strany
     * @param s        skratka strany
     */
    public Pstrana(int id, String n, String s) {
        this.id = id;
        this.nazov = n; // Inicializácia názvu strany
        this.skratka = s; // Inicializácia skratky strany
        this.setPocet_h(0); // Inicializácia počtu hlasov na nulu
        this.kandidati = new ArrayList<Kandidat>(); // Inicializácia zoznamu kandidátov
    }

    /**
     * Metóda na získanie ID strany.
     *
     * @return ID strany
     */
    public int getIdStrana() {
        return this.id;
    }

    /**
     * Metóda na získanie názvu strany.
     *
     * @return názov strany
     */
    public String getNazov() {
        return this.nazov + " " + this.skratka;
    }

    /**
     * Metóda na získanie skratky strany.
     *
     * @return skratka strany
     */
    public String getSkratka() {
        return this.skratka;
    }

    /**
     * Metóda na získanie zoznamu kandidátov strany.
     *
     * @return zoznam kandidátov strany
     */
    public ArrayList<Kandidat> getKandidati() {
        return this.kandidati;
    }

    /**
     * Metóda na pridanie nového kandidáta do strany.
     *
     * @param kandidat nový kandidát
     */
    public void novy_kandidat(Kandidat kandidat) {
        kandidati.add(kandidat);
    }

    /**
     * Metóda na získanie počtu získaných hlasov strany.
     *
     * @return počet získaných hlasov
     */
    public int getPocet_h() {
        return pocet_h;
    }

    /**
     * Metóda na nastavenie počtu získaných hlasov strany.
     *
     * @param pocet počet získaných hlasov
     */
    public void setPocet_h(int pocet) {
        this.pocet_h = pocet;
    }

    /**
     * Metóda na pridanie hlasu pre stranu.
     */
    public void pridajHlas() {
        this.pocet_h++;
    }

    /**
     * Metóda na zostavenie zoznamu kandidátov strany ako reťazca.
     *
     * @return zoznam kandidátov
     */
    public ArrayList<String> zoznam_kandidatov_strany() {
        ArrayList<String> zk = new ArrayList<>();
        for (Kandidat kandidat : kandidati) {
            zk.add(kandidat.getIdObcan() + ". " + kandidat.getInfo());
        }
        return zk; // Vrátenie zoznamu kandidátov
    }

    /**
     * Predefinovaná metóda na serializáciu objektu.
     * 
     * @return reťazec reprezentujúci objekt
     */
    @Override
    public String toString() {
        return "Pstrana{" +
                "id=" + id +
                "nazov='" + nazov + '\'' +
                "skratka='" + skratka + '\'' +
                "pocet_h=" + pocet_h +
                ", kandidati=" + kandidati +
                '}';
    }
}
