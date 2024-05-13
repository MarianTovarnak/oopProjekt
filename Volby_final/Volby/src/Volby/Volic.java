package Volby;

import java.io.Serializable;

/**
 * Trieda reprezentujúca občana, ktorý môže voliť rozšírená o možnosť serializácie.
 */
public class Volic extends Obcan implements Serializable {
    private Boolean zahlasoval; // Príznak, či volič už hlasoval

    /**
     * Konštruktor triedy.
     *
     * @param id identifikátor voliča
     */
    public Volic(int id) {
        super(id); // Volanie konštruktora nadradenej triedy
    }
    
    /**
     * Parametrický konštruktor triedy.
     *
     * @param id identifikátor voliča
     * @param m  meno voliča
     * @param p  priezvisko voliča
     */
    public Volic(int id, String m, String p) {
        super(id, m, p); // Volanie konštruktora nadradenej triedy 
        this.zahlasoval = false; // Nastavenie príznaku, že volič ešte nezahlasoval
    }
    
    /**
     * Metóda na získanie ID voliča.
     *
     * @return identifikátor voliča
     */
    public int getIdVolic() {
        return super.getIdObcan(); // Získanie ID voliča zdedené od triedy Obcan
    }
    
    /**
     * Metóda na získanie informácie, či volič už hlasoval.
     *
     * @return true, ak volič už hlasoval, inak false
     */
    public Boolean getHlasoval() {
        return this.zahlasoval;
    }
    
    /**
     * Metóda na nastavenie príznaku, že volič už hlasoval.
     */
    public void zahlasoval() {
        this.zahlasoval = true;
    }
    
    /**
     * Metóda pre reprezentáciu objektu vo forme reťazca pre serializáciu.
     *
     * @return reťazcová reprezentácia objektu
     */
    @Override
    public String toString() {
        return "Volic{" +
                "zahlasoval=" + zahlasoval +
                '}';
    }
}
