package Volby;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Kandidat extends Obcan implements Serializable{
	private int pocet_h;
	
	public Kandidat(int id, String m, String p, LocalDate dn) {
		super(id, m, p, dn);
		this.setPocet_h(0);
	}
	
	public int getIdkandidat() {
		return super.id;
	}

	public String getInfo() {
		LocalDate now = LocalDate.now();
		Period p = Period.between(this.datum_n, now);
		return this.priezvisko + " "+ this.meno + " vek. "+p.get(ChronoUnit.YEARS);
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
	
    @Override
    public String toString() {
        return "Kandidat{" +
                "pocet_h=" + pocet_h +
                '}';
    }
	
}
