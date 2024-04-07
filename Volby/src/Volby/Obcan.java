package Volby;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Obcan {
	private int id;
	private String meno;
	private String priezvisko;
	private LocalDate datum_n;
	
	
	public Obcan(int id, String m, String p, String dn) {
		this.id = id;
		this.meno = new String(m);
		this.priezvisko = new String(p);
		
		String[] r = dn.split("\\.");
		this.datum_n = LocalDate.of(Integer.parseInt(r[2]), Integer.parseInt(r[1]), Integer.parseInt(r[0]));
	}
	
	public Obcan(int id, String m, String p) {
		this.id = id;
		this.meno = new String(m);
		this.priezvisko = new String(p);
		this.datum_n = null;
	}
	
	public Obcan(int id) {
		this.id = id;
	}
	
	public int getIdObcan() {
		return this.id;
	}
	
	public String getPmeno() {
		return this.priezvisko + " "+ this.meno;
	}

	public String getMeno() {
		return this.meno;
	}

	public String getPriezvisko() {
		return this.priezvisko;
	}

	public Boolean jePlnolety() {
		LocalDate now = LocalDate.now();
		Period p = Period.between(this.datum_n, now);
		return (p.get(ChronoUnit.YEARS) < 18) ? false : true;
	}

}
