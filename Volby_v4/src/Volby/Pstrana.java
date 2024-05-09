package Volby;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pstrana implements Serializable {
	private int id;
	private String nazov;
	private String skratka;
	private int pocet_h;
	private ArrayList<Kandidat> kandidati;
	
	public Pstrana(int id, String n, String s) {
		this.id = id;
		this.nazov = n;
		this.skratka = s;
		this.setPocet_h(0);
		this.kandidati = new ArrayList<Kandidat>();
	}

	public int getIdStrana() {
		return this.id;
	}
	
	public String getNazov() {
		return this.nazov+" "+this.skratka;
	}
	
	public String getSkratka() {
		return this.skratka;
	}

	public ArrayList<Kandidat> getKandidati() {
		return this.kandidati;
	}
	
	public void novy_kandidat(Kandidat kandidat) {
		kandidati.add(kandidat);
	}

	public int getPocet_h() {
		return pocet_h;
	}
	
	public ArrayList<Kandidat> get_kandidati() {
		return this.kandidati;
	}

	public void setPocet_h(int pocet) {
		this.pocet_h = pocet;
	}
	
	public void pridajHlas() {
		this.pocet_h++;
	}
	
	public ArrayList<String> zoznam_kandidatov_strany() {
		ArrayList<String> zk = new ArrayList<>();
        for (Kandidat kandidat : kandidati) {
        	zk.add(kandidat.getIdObcan()+". "+kandidat.getInfo());
        }
        return zk;
	}
    
    @Override
    public String toString() {
        return "Pstrana{" +
                "id=" + id +
                "nazov='" + nazov + '\'' +
                "skratka='" + skratka + '\'' +
                "pocet_h=" + pocet_h +
                ", kandidati=" +kandidati +
                '}';
    }
}
