package Volby;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Trieda reprezentujúca občana, rozšírená o možnosť serializácie.
 */
public class Obcan implements Serializable {
    protected int id; // Identifikátor občana
    protected String meno; // Meno občana
    protected String priezvisko; // Priezvisko občana
    protected LocalDate datum_n; // Dátum narodenia občana

    /**
     * Konštruktor triedy s parametrami ID, menom, priezviskom a dátumom narodenia vo formáte reťazca.
     *
     * @param id identifikátor občana
     * @param m meno občana
     * @param p priezvisko občana
     * @param dn dátum narodenia občana vo formáte reťazca (dd.mm.yyyy)
     */
    public Obcan(int id, String m, String p, String dn) {
        this.id = id;
        this.meno = new String(m); // Inicializácia mena občana
        this.priezvisko = new String(p); // Inicializácia priezviska občana
        
        // Rozdelenie dátumu narodenia na den, mesiac a rok a inicializácia objektu LocalDate
        String[] r = dn.split("\\.");
        this.datum_n = LocalDate.of(Integer.parseInt(r[2]), Integer.parseInt(r[1]), Integer.parseInt(r[0]));
    }
    
    /**
     * Konštruktor triedy s parametrami ID, menom, priezviskom a dátumom narodenia vo formáte objektu LocalDate.
     *
     * @param id identifikátor občana
     * @param m meno občana
     * @param p priezvisko občana
     * @param dn dátum narodenia občana
     */
    public Obcan(int id, String m, String p, LocalDate dn) {
        this.id = id;
        this.meno = new String(m); // Inicializácia mena občana
        this.priezvisko = new String(p); // Inicializácia priezviska občana
        this.datum_n = dn; // Inicializácia dátumu narodenia občana
    }

    /**
     * Konštruktor triedy s parametrami ID, menom a priezviskom.
     *
     * @param id identifikátor občana
     * @param m meno občana
     * @param p priezvisko občana
     */
    public Obcan(int id, String m, String p) {
        this.id = id;
        this.meno = new String(m); // Inicializácia mena občana
        this.priezvisko = new String(p); // Inicializácia priezviska občana
        this.datum_n = null; // Nastavenie dátumu narodenia na null
    }

    /**
     * Konštruktor triedy s parametrom ID.
     *
     * @param id identifikátor občana
     */
    public Obcan(int id) {
        this.id = id;
    }
    
    /**
     * Metóda na získanie ID občana.
     *
     * @return identifikátor občana
     */
    public int getIdObcan() {
        return this.id;
    }
    
    /**
     * Metóda na získanie informácií o občanovi (meno, priezvisko, dátum narodenia).
     *
     * @return informácie o občanovi vo forme reťazca
     */
    public String getInfo() {
        return this.priezvisko + " " + this.meno + " nar. " + datum_n;
    }

    /**
     * Metóda na získanie plného mena občana.
     *
     * @return plné meno občana
     */
    public String getPmeno() {
        return this.priezvisko + " " + this.meno;
    }

    /**
     * Metóda na získanie mena občana.
     *
     * @return meno občana
     */
    public String getMeno() {
        return this.meno;
    }

    /**
     * Metóda na získanie priezviska občana.
     *
     * @return priezvisko občana
     */
    public String getPriezvisko() {
        return this.priezvisko;
    }

    /**
     * Metóda na overenie, či je občan plnoletý.
     *
     * @return true, ak je občan plnoletý, inak false
     */
    public Boolean jePlnolety() {
        LocalDate now = LocalDate.now();
        Period p = Period.between(this.datum_n, now); // Výpočet veku občana
        return (p.get(ChronoUnit.YEARS) < 18) ? false : true; // Overenie, či je občan starší ako 18 rokov
    }

    /**
     * Predefinovaná metóda na serializáciu objektu.
     *
     * @return reprezentácia objektu vo forme reťazca
     */
    @Override
    public String toString() {
        return "Obcan{" +
                "id=" + id +
                ", meno='" + meno + '\'' +
                ", priezvisko='" + priezvisko + '\'' +
                ", datum_n=" + datum_n +
                '}';
    }
}
