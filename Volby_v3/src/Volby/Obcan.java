package Volby;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Obcan implements Serializable {
	protected int id;
	protected String meno;
	protected String priezvisko;
	protected LocalDate datum_n;
	
	
	public Obcan(int id, String m, String p, String dn) {
		this.id = id;
		this.meno = new String(m);
		this.priezvisko = new String(p);
		
		String[] r = dn.split("\\.");
		this.datum_n = LocalDate.of(Integer.parseInt(r[2]), Integer.parseInt(r[1]), Integer.parseInt(r[0]));
	}
	
	public Obcan(int id, String m, String p, LocalDate dn) {
		this.id = id;
		this.meno = new String(m);
		this.priezvisko = new String(p);
		this.datum_n = dn;
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
	
	public String getInfo() {
		return this.priezvisko + " "+ this.meno + " nar. " + datum_n;
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
