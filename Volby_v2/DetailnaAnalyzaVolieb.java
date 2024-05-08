package Volby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DetailnaAnalyzaVolieb implements AnalyzaVolieb {
	private int pocetM = 10;
	private int prideleneM =0;
	private double repvolcis;
	private List<Pstrana> pstranypor;
	private Map<Integer, Integer> pocetMpreS;
	private Map<Integer, Integer> pocetMpreZ;
	private Volby volby;
    private int pocethlasov;
    private ArrayList<String> poradieS;
    private ArrayList<String> poradieK; 
  

	@Override
	public void analyzaVysledok(Volby v) {
		this.volby = v;
		pocet_hlasov_spolu();
		this.repvolcis = (double)this.pocethlasov/(double)(this.pocetM+1);
		pocetMpreS = new HashMap<>();
		pocetMpreZ = new HashMap<>();
		pocet_mandatov();
	}
	
	public int pocetHlasov() {
		return pocethlasov;
	}
	
	public ArrayList<String> poradieStran() {
		return this.poradieS;
	}
	
	public ArrayList<String> poradieKandidatov() {
		return poradieK;
	}
	
	// spocitanie vsetkych hlasov vo volbach pre stranu
	private void pocet_hlasov_spolu() {
		this.pocethlasov=0;
        List<Pstrana> stranypor = new ArrayList<>(this.volby.pstrany);

        for (Pstrana  pstr : stranypor) {
        	this.pocethlasov +=pstr.getPocet_h();
        }
	}

	//usporiadanie stran podla ziskanych hlasov + rozdelenie mandatov
	private void pocet_mandatov() {
        pstranypor = new ArrayList<>(this.volby.pstrany);
		IntPropertyComparator<Pstrana> comparator = new IntPropertyComparator<>(Pstrana::getPocet_h);
		Collections.sort(pstranypor, comparator.reversed());
		
        for (Pstrana  pstr : pstranypor) {
    		if(this.repvolcis>0) {
    			int pocetk = (int) ( pstr.getPocet_h()/this.repvolcis);
    			int pocetz = (int) ( pstr.getPocet_h()%this.repvolcis);
    		    this.pocetMpreS.put(pstr.getIdStrana(), pocetk);
    		    this.pocetMpreZ.put(pstr.getIdStrana(), pocetz);
    		    this.prideleneM +=pocetk;
    		}
        }

		if(pocetM > prideleneM) {
			rozdel_zvysky();
		}
		this.poradieS = new ArrayList<>();
		this.poradieK = new ArrayList<>();
		zoznam_kandidatov();
	}
	/*
	private void pocet_mandatov() {
		this.poradieS = new ArrayList<>();
        List<Pstrana> stranypor = new ArrayList<>(this.volby.pstrany);
		
		IntPropertyComparator<Pstrana> comparator = new IntPropertyComparator<>(Pstrana::getPocet_h);

		Collections.sort(stranypor, comparator.reversed());
		int i=1;
		this.poradieK = new ArrayList<>();
        for (Pstrana  pstr : stranypor) {
    		if(this.repvolcis>0) {
    			int pocetk = (int) ( pstr.getPocet_h()/this.repvolcis);
    			int pocetz = (int) ( pstr.getPocet_h()%this.repvolcis);
    		    this.pocetMpreS.put(pstr.getIdStrana(), pocetk);
    		    this.pocetMpreZ.put(pstr.getIdStrana(), pocetz);
    		    this.prideleneM +=pocetk;
    		    if(pocetk>0) {
    		    	this.poradieS.add((i++)+". "+pstr.getNazov()+" ("+pstr.getPocet_h()+")");
    		    	zoznam_kandidatov(pstr, pocetk);
    		    }
    		}
        }
	}
*/

	// ak chybaju mandaty, tak sa rozdeluju podla najvyssieho zvysku po deleni hlasov
	private void rozdel_zvysky() {

        List<Map.Entry<Integer, Integer>> zoznam = new ArrayList<>(pocetMpreZ.entrySet());

        Collections.sort(zoznam, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> entry1, Map.Entry<Integer, Integer> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });

        Map<Integer, Integer> usporadaneHodnoty = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : zoznam) {
            usporadaneHodnoty.put(entry.getKey(), entry.getValue());
        }
	
        for (Map.Entry<Integer, Integer> entry : usporadaneHodnoty.entrySet()) {
        	if((entry.getValue() > 0) && (this.pocetM > this.prideleneM)) {
        		int idstr = entry.getKey();
            	this.pocetMpreS.put(idstr, this.pocetMpreS.get(idstr)+1);
            	this.prideleneM++;
        	}
        }
	}
	public void zoznam_kandidatov() {
		int j=1;
        for (Pstrana  pstr : pstranypor) {
        	ArrayList<Kandidat> z_kandidatov = new ArrayList<>();
    		z_kandidatov = pstr.get_kandidati();
    		IntPropertyComparator<Kandidat> comparator = new IntPropertyComparator<>(Kandidat::getPocet_h);
    		Collections.sort(z_kandidatov, comparator.reversed());
    		if (this.pocetMpreS.containsKey(pstr.getIdStrana())) {
    			int pk=this.pocetMpreS.get(pstr.getIdStrana());
    			this.poradieS.add((j++)+". "+pstr.getNazov()+" ("+pk+")"); //naplnenie pola s nazvom strany a...
	    		int i = 1;
	    		for (Kandidat  pkan : z_kandidatov) {
	    	    	if(i<=pk) {
	    	    		this.poradieK.add(pkan.getInfo() +" ("+pkan.getPocet_h()+") "+pstr.getSkratka());//naplnenie pola s nazvom poslanca a...
	    	    		i++;
	    	        }
	    		}
    		}
        }
	}
	
}