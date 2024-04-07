package Volby;


public class Kandidat extends Obcan{
	private int pocet_h;
	
	public Kandidat(int id, String m, String p) {
		super(id, m, p);
		this.setPocet_h(0);
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
	
}
