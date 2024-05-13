package Volby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;


/**
 * Trieda reprezentujúca volebný systém.
 */
public class Volby implements Serializable {
    private Obcan prihlaseny; // Aktuálne prihlásený občan
    private ArrayList<Obcan> obcania; // Zoznam občanov
    protected ArrayList<Pstrana> pstrany; // Zoznam politických strán
    private ArrayList<Volic> volici; // Zoznam voličov
    
    /**
     * Konštruktor triedy Volby.
     */
    public Volby() {
        this.obcania = new ArrayList<Obcan>(); // Inicializácia zoznamu občanov
        this.pstrany = new ArrayList<Pstrana>(); // Inicializácia zoznamu politických strán
        this.volici = new ArrayList<Volic>(); // Inicializácia zoznamu voličov
        this.prihlaseny = null; // Nastavenie aktuálneho prihláseného občana na null
    }

    /**
     * Metóda na importovanie údajov o občanoch zo súboru obcan.csv.
     */
    public void import_obcan() {
        String file = "obcan.csv"; // Názov vstupného súboru
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file)); // 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (true) {
            String line = null;

            try {
                line = reader.readLine(); // Čítanie riadka zo súboru
            } catch (IOException e) {
                e.printStackTrace(); 
            }

            if (line == null) break;

            String[] parts = line.split(";"); // Rozdelenie riadka podľa oddelovača ";"
            Obcan novaOsoba = new Obcan(Integer.valueOf(parts[0]), parts[1], parts[2], parts[3]); // Vytvorenie nového objektu občana na základe údajov zo súboru
            this.obcania.add(novaOsoba); // Pridanie občana do zoznamu
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metóda na importovanie údajov o politických stranách zo súboru pstrany.csv.
     */
    public void import_pstrany() {
        String file = "pstrany.csv"; 
        int pc_obcan = 0; // Počet občanov
        BufferedReader reader = null; 

        try {
            reader = new BufferedReader(new FileReader(file)); 
        } catch (FileNotFoundException e) {
            e.printStackTrace(); 
        }

        while (true) {
            String line = null; 

            try {
                line = reader.readLine(); // Čítanie riadka zo súboru
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line == null) break; 

            String[] parts = line.split(";"); // Rozdelenie riadka podľa oddelovača ";"
            Pstrana novaStrana = new Pstrana(Integer.valueOf(parts[0]), parts[1], parts[2]); // Vytvorenie nového objektu politického strany

            // Pridanie kandidátov do strany na základe údajov zo súboru občan.csv
            // každej strane po 8 občanov ak je plnoletý
            for (int i = pc_obcan; i < (pc_obcan + 8) && i < obcania.size(); i++) {
                Obcan pom = obcania.get(i);
                if (pom.jePlnolety()) {
                    Kandidat k = new Kandidat(pom.getIdObcan(), pom.meno, pom.priezvisko, pom.datum_n);
                    novaStrana.novy_kandidat(k);
                }
            }

            pstrany.add(novaStrana); // Pridanie politického strany do zoznamu
            pc_obcan += 8;
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
    
    /**
     * Metóda na získanie aktuálne prihláseného občana.
     * 
     * @return Aktuálne prihlásený občan.
     */
    public Obcan getPrihlaseny() {
        return this.prihlaseny;
    }
    
    /**
     * Metóda na overenie voliča podľa ID.
     *
     * @param login_id ID voliča na overenie.
     * @return Výsledok overenia: 0 ak je plnoletý, -1 ak nie je plnoletý, -2 ak nie je v zozname občanov.
     */
    public int overenie_volica(int login_id) {
        for (Obcan osoba : this.obcania) {
            if (osoba.getIdObcan() == login_id) {
                if (osoba.jePlnolety()) {
                    this.prihlaseny = osoba; // Nastavenie aktuálneho prihláseného občana
                    return 0;
                } else {
                    return -1; // Ak nie je plnoletý
                }
            }
        }
        return -2; // Ak nie je v zozname občanov
    }
    
    /**
     * Metóda na zápis do zoznamu voličov.
     * 
     * @return True ak bol zápis úspešný, false inak.
     */
    public boolean zapis_do_volicov() {
        boolean nasiel = false; // Premenná indikujúca, či sa našiel volič
        int ido = this.prihlaseny.getIdObcan(); // ID prihláseného občana
        for (Volic v : this.volici) {
            if (ido == v.getIdVolic()) {
                if (v.getHlasoval()) {
                    return false; // Ak už volič hlasoval
                } else {
                    nasiel = true;
                }
            }
        }
        if (!nasiel) {
            Volic volic = new Volic(this.prihlaseny.getIdObcan(), this.prihlaseny.getMeno(), this.prihlaseny.getPriezvisko()); // Vytvorenie nového voliča
            volici.add(volic); // Pridanie voliča do zoznamu
        }
        return true;
    }
    
    /**
     * Metóda na získanie zoznamu politických strán.
     * 
     * @return Zoznam politických strán vo forme reťazca.
     */
    public ArrayList<String> zoznam_stran() {
        ArrayList<String> strany = new ArrayList<>();
        for (Pstrana s : this.pstrany) {
            strany.add(s.getIdStrana() + ". " + s.getNazov());
        }
        return strany;
    }

    /**
     * Metóda na získanie zoznamu politických strán usporiadaných vzostupne podľa ID.
     * 
     * @return Mapa politických strán usporiadaných vzostupne podľa ID.
     */
    public Map<Integer, String> zoznam_stran_asc() {
        Map<Integer, String> strany = new HashMap<>();
        for (Pstrana s : this.pstrany) {
            strany.put(s.getIdStrana(), s.getIdStrana() + ". " + s.getNazov());
        }
        return strany;
    }
    
    /**
     * Metóda na získanie zoznamu politických strán ako objektov.
     * 
     * @return Mapa politických strán.
     */
    public Map<Integer, Pstrana> zoznam_stran_obj() {
        Map<Integer, Pstrana> strany = new HashMap<>();
        for (Pstrana s : this.pstrany) {
            strany.put(s.getIdStrana(), s);
        }
        return strany;
    }
    
    /**
     * Metóda na získanie zoznamu kandidátov politických strán.
     * 
     * @param cislo_s Číslo politického strany.
     * @return Zoznam kandidátov politických strán vo forme reťazca.
     */
    public ArrayList<String> zoznam_kandidatov_strany(int cislo_s) {
        ArrayList<String> z_kandidatov = new ArrayList<>();
        for (Pstrana s : this.pstrany) {
            if (cislo_s == s.getIdStrana()) {
                z_kandidatov =  s.zoznam_kandidatov_strany();
                break;
            }
        }
        return z_kandidatov;
    }
    
    /**
     * Metóda na získanie kandidátov politických strán podľa ich ID.
     * 
     * @param cislo_s Číslo politického strany.
     * @return Zoznam kandidátov politických strán.
     */
    public ArrayList<Kandidat> getKandidati_by_ids(int cislo_s) {
        for (Pstrana s : this.pstrany) {
            if (cislo_s == s.getIdStrana()) {
                return s.getKandidati();
            }
        }
        return null;
    }
    
    // Metóda na overenie, či zoznam obsahuje zadané číslo
    private boolean obsahujeCislo(ArrayList<Integer> pole, int hladaneCislo) {
        for (int cislo : pole) {
            if (cislo == hladaneCislo) {
                return true;
            }
        }
        return false;
    }
    
    // Metóda na zápis hlasovania
    public boolean zapis_hlasovanie(int c_strany, ArrayList<Integer> cisla_k) {
        // Zapocitanie hlasov strane a kandidátom
        for (Pstrana s : this.pstrany) {
            if (s.getIdStrana() == c_strany) {
                s.pridajHlas();
                
                ArrayList<Kandidat> kanstr = s.getKandidati(); 
                for (Kandidat k : kanstr) {
                    if (cisla_k != null && obsahujeCislo(cisla_k, k.getIdObcan())) { // Zapocitanie hlasov vybranym kandidatom
                        k.pridajHlas();
                    }
                }
                break;
            }
        }
        // Zapísanie voličovi, že úspešne hlasoval
        int ido = this.prihlaseny.getIdObcan();
        for (Volic v : this.volici) {
            if (ido == v.getIdVolic()) {
                v.zahlasoval();
                return true;
            }
        }

        return false;
    }
    
    /**
     * Metóda na reprezentáciu objektu triedy Volby vo forme reťazca.
     * 
     * @return Textová reprezentácia objektu triedy Volby.
     */
    @Override
    public String toString() {
        return "Volby{" +
                "prihlaseny=" +prihlaseny +
                ", obcania=" +obcania +
                ", pstrany=" +pstrany +
                ", volici=" +volici +
                '}';
    }
}
