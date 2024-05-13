package Volby;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Trieda reprezentujúca kandidáta volebnej strany, rozšírená o možnosť serializácie.
 */
public class Kandidat extends Obcan implements Serializable {
    private int pocet_h; // Počet získaných hlasov

    /**
     * Konštruktor triedy.
     *
     * @param id identifikátor kandidáta
     * @param m meno kandidáta
     * @param p priezvisko kandidáta
     * @param dn dátum narodenia kandidáta
     */
    public Kandidat(int id, String m, String p, LocalDate dn) {
        super(id, m, p, dn); // Volanie konštruktora nadradenej triedy
        this.setPocet_h(0); // Inicializácia počtu hlasov
    }

    /**
     * Metóda na získanie ID kandidáta.
     *
     * @return ID kandidáta
     */
    public int getIdkandidat() {
        return super.id;
    }

    /**
     * Prekrytá metóda getInfo pre získanie informácií o kandidátovi -> meno a vek.
     *
     * @return informácie o kandidátovi
     */
    @Override
    public String getInfo() {
        LocalDate now = LocalDate.now(); // Aktuálny dátum
        Period p = Period.between(this.datum_n, now); // Výpočet veku kandidáta
        return this.priezvisko + " " + this.meno + " vek. " + p.get(ChronoUnit.YEARS); // Vrátenie informácií o kandidátovi
    }

    /**
     * Metóda na získanie počtu získaných hlasov.
     *
     * @return počet získaných hlasov
     */
    public int getPocet_h() {
        return pocet_h;
    }

    /**
     * Metóda na nastavenie počtu získaných hlasov.
     *
     * @param pocet počet získaných hlasov
     */
    public void setPocet_h(int pocet) {
        this.pocet_h = pocet;
    }

    /**
     * Metóda na pridanie hlasu pre kandidáta.
     */
    public void pridajHlas() {
        this.pocet_h++;
    }

    /**
     * Predefinovaná metóda na serializáciu objektu.
     *
     * @return reťazec reprezentujúci objekt
     */
    @Override
    public String toString() {
        return "Kandidat{" +
                "pocet_h=" + pocet_h +
                '}';
    }
}
