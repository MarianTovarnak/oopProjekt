package Volby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Volby implements Serializable {
	private Obcan prihlaseny;
	private ArrayList<Obcan> obcania;
	protected ArrayList<Pstrana> pstrany;
	private ArrayList<Volic> volici;
	
	public Volby() {
		this.obcania = new ArrayList<Obcan>();
		this.pstrany = new ArrayList<Pstrana>();
		this.volici = new ArrayList<Volic>();
		this.prihlaseny = null;
	}

	// import udajov zo suboru obcan.csv
	public void import_obcan() {
		 String file = "obcan.csv";
	     BufferedReader reader = null;
	     try {
	    	 reader = new BufferedReader(new FileReader(file));
	     } catch (FileNotFoundException e) {
	    	 e.printStackTrace();
	     }

	     while (true) {
	    	 String line = null;
	    	 try {
	    		 line = reader.readLine();
	    	} catch (IOException e) {
	    		e.printStackTrace();
			}
	        if (line == null) break;

           String[] parts = line.split(";");
           Obcan novaOsoba = new Obcan(Integer.valueOf(parts[0]), parts[1], parts[2], parts[3]);
           this.obcania.add(novaOsoba);
	     }

	     try {
	    	 reader.close();
	     } catch (IOException e) {
			e.printStackTrace();
		}
	}

	// import udajov zo suboru pstrany.csv
	public void import_pstrany() {
		 String file = "pstrany.csv";
		 int pc_obcan = 0;
	     BufferedReader reader = null;
	     try {
	    	 reader = new BufferedReader(new FileReader(file));
	     } catch (FileNotFoundException e) {
	    	 e.printStackTrace();
	     }

	     while (true) {
	    	String line = null;
	    	try {
	    		 line = reader.readLine();
	    	} catch (IOException e) {
	    		e.printStackTrace();
			}
	        if (line == null) break;

            String[] parts = line.split(";");
            Pstrana novaStrana = new Pstrana(Integer.valueOf(parts[0]), parts[1], parts[2]);
            
            for (int i = pc_obcan; i < (pc_obcan+8) && i < obcania.size(); i++) {
            	Obcan pom = obcania.get(i);
            	if(pom.jePlnolety()) {
            		Kandidat k = new Kandidat(pom.getIdObcan(), pom.meno, pom.priezvisko, pom.datum_n);
            		novaStrana.novy_kandidat(k);
            	}
            }
            pstrany.add(novaStrana);
            pc_obcan +=8;  
	     }

	     try {
	    	 reader.close();
	     } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Obcan getPrihlaseny() {
		return this.prihlaseny;
	}
	
	public int overenie_volica(int login_id) {
		for (Obcan osoba : this.obcania) {
			if(osoba.getIdObcan() == login_id) {
				if(osoba.jePlnolety()) {
					this.prihlaseny = osoba;
					return 0;
				} else {
					return -1; //ak nie je plnolety
				}
			}
		}
		return -2; // ak nie je v zozname obcanov
	}
	
	public boolean zapis_do_volicov() {
		boolean nasiel = false;
		int ido = this.prihlaseny.getIdObcan();
		for (Volic v : this.volici) {
			if(ido == v.getIdVolic()) {
				if(v.getHlasoval()) {
					return false;
				} else {
					nasiel = true;
				}
			}
		}
		if(!nasiel) {
			Volic volic = new Volic(this.prihlaseny.getIdObcan(), this.prihlaseny.getMeno(), this.prihlaseny.getPriezvisko());
			volici.add(volic);
		}
		return true;
	}
	
	public ArrayList<String> zoznam_stran() {
		ArrayList<String> strany = new ArrayList<>();
		for (Pstrana s : this.pstrany) {
			strany.add(s.getIdStrana()+". "+s.getNazov());
		}
		return strany;
	}

	public Map<Integer, String> zoznam_stran_asc() {
		Map<Integer, String> strany = new HashMap<>();
		for (Pstrana s : this.pstrany) {
			strany.put(s.getIdStrana(), s.getIdStrana()+". "+s.getNazov());
		}
		return strany;
	}
	
	public Map<Integer, Pstrana> zoznam_stran_obj() {
		Map<Integer, Pstrana> strany = new HashMap<>();
		for (Pstrana s : this.pstrany) {
			strany.put(s.getIdStrana(), s);
		}
		return strany;
	}
	
	public ArrayList<String> zoznam_kandidatov_strany(int cislo_s) {
		ArrayList<String> z_kandidatov = new ArrayList<>();
		for (Pstrana s : this.pstrany) {
			if(cislo_s == s.getIdStrana()) {
				z_kandidatov =  s.zoznam_kandidatov_strany();
				break;
			}
		}
		return z_kandidatov;
	}
	
	public ArrayList<Kandidat> getKandidati_by_ids(int cislo_s) {
		for (Pstrana s : this.pstrany) {
			if(cislo_s == s.getIdStrana()) {
				return s.getKandidati();
			}
		}
		return null;
	}
	
    private boolean obsahujeCislo(ArrayList<Integer> pole, int hladaneCislo) {
		for (int cislo : pole) {
			if (cislo == hladaneCislo) {
				return true;
			}
		}
        return false;
    }
    
	public boolean zapis_hlasovanie(int c_strany, ArrayList<Integer> cisla_k) {
		// zapocitanie hlasu strane
		for (Pstrana s : this.pstrany) {
			if(s.getIdStrana()==c_strany) {
				s.pridajHlas();
				
				ArrayList<Kandidat> kanstr = s.getKandidati(); 
				for (Kandidat k : kanstr) {
					if(cisla_k!=null && obsahujeCislo(cisla_k, k.getIdObcan())) { //zapocitanie hlasu vybranym kandidatom
						k.pridajHlas();
					}
				}
				break;
			}
		}

		// zapise volicovi, ze uspesne hlasoval
		int ido = this.prihlaseny.getIdObcan();
		for (Volic v : this.volici) {
			if(ido == v.getIdVolic()) {
				v.zahlasoval();
				return true;
			}
		}

		return false;
	}

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
