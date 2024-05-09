package Volby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZakladnaAnalyzaVolieb {
	protected Volby volby;
	protected int pocethlasov;
    protected ArrayList<String> poradieS;
    protected ArrayList<String> poradieK;

	public ZakladnaAnalyzaVolieb(Volby v) {
		this.volby = v;
		pocet_hlasov_spolu();
		poradie_stran();
		poradie_kandidatov();
	}
	
	public int pocetHlasov() {
		return pocethlasov;
	}
	
	public ArrayList<String> poradieStran() {
		return poradieS;
	}
	
	public ArrayList<String> poradieKandidatov() {
		return poradieK;
	}
	
	protected void pocet_hlasov_spolu() {
		this.pocethlasov=0;
        List<Pstrana> stranypor = new ArrayList<>(this.volby.pstrany);

        for (Pstrana  pstr : stranypor) {
        	this.pocethlasov +=pstr.getPocet_h();
        }
	}
	
	private void poradie_stran() {
		this.poradieS = new ArrayList<>();
        List<Pstrana> stranypor = new ArrayList<>(this.volby.pstrany);
		
		IntPropertyComparator<Pstrana> comparator = new IntPropertyComparator<>(Pstrana::getPocet_h);

		Collections.sort(stranypor, comparator.reversed());
		
		int i=1;
        for (Pstrana  pstr : stranypor) {
        	this.poradieS.add((i++)+". "+pstr.getNazov()+" ("+pstr.getPocet_h()+")");
        }
	}
	
	public void poradie_kandidatov() {
		ArrayList<Kandidat> z_kandidatov = new ArrayList<>();
		
		for (Pstrana s : this.volby.pstrany) {
			z_kandidatov.addAll(s.get_kandidati());
		}	

		this.poradieK = new ArrayList<>();

		int i=1;
		IntPropertyComparator<Kandidat> comparator = new IntPropertyComparator<>(Kandidat::getPocet_h);
		Collections.sort(z_kandidatov, comparator.reversed());
        for (Kandidat  pkan : z_kandidatov) {
        	this.poradieK.add((i++)+". "+ pkan.getInfo() +" ("+pkan.getPocet_h()+")");
        }
	}
	
}