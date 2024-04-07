package Volby;

import java.util.ArrayList;
import java.util.List;

public class Pstrana {
	private int id;
	private String nazov;
	private int pocet_h;
	private ArrayList<Kandidat> kandidati;
	
	public Pstrana(int id, String n) {
		this.id = id;
		this.nazov = n;
		this.setPocet_h(0);
		this.kandidati = new ArrayList<Kandidat>();
	}

	public int getIdStrana() {
		return this.id;
	}
	
	public String getNazov() {
		return this.nazov;
	}
	
	public void novy_kandidat(Kandidat kandidat) {
		kandidati.add(kandidat);
	}

	public int getPocet_h() {
		return pocet_h;
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
        	zk.add(kandidat.getIdObcan()+". "+kandidat.getPmeno());
            //System.out.println(kandidat.getIdObcan()+". "+kandidat.getPmeno());
        }
        return zk;
	}
}
