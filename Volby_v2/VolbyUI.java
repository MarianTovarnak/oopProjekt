package Volby;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class VolbyUI {
	
	public static boolean overenie_obcana(Volby volby) {
		int login_id=0;
		System.out.println("Registracia volica");
		System.out.print("Zadajte svoje ID: ");
		Scanner scan= new Scanner(System.in);

		try {
			login_id = scan.nextInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int stav = volby.overenie_volica(login_id);
		
		if(stav == -2) {
			System.out.println("Nie ste v zozname obcanov SR." );
			return false;
		}
		if(stav == -1) {
			System.out.println("Nie ste plnolety." );
			return false;
		}
		
		return true;
	}
	
	private static int zoznam_p_stran(Volby v) {
		Scanner scan;
		int cislo_s = -1;

		System.out.print("\nZoznam politickych stran:\n");
		ArrayList<String> strany = v.zoznam_stran();
		for (String s : strany) {
			System.out.println(s);
		}
		
		System.out.print("Vyberte si cislo politickej strany: ");
		scan= new Scanner(System.in);
		
		try {
			cislo_s = scan.nextInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cislo_s;
	}
	
	private static void zoznam_kandidatov(Volby v, int cislo_s){
		System.out.print("\nZoznam kandidatov strany:\n");
		ArrayList<String> kand = v.zoznam_kandidatov_strany(cislo_s);
		for (String k : kand) {
			System.out.println(k);
		}
	}
	
	private static void hlasovanie(Volby v) {
		Scanner scan;
		int cislo_s;
		String cisla_k = null;
		
		int[] hlasy = null;
		
		cislo_s = zoznam_p_stran(v);
		zoznam_kandidatov(v, cislo_s);

		String[] parts=null;
		do {
			System.out.print("\nZadaj max 4 kandidatov oddelenych ciarkou:\n");
			scan= new Scanner(System.in);
				try {
					cisla_k = scan.nextLine();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(cisla_k != null && cisla_k!="" ) {
					parts = cisla_k.split(",");
				}
		} while(parts!=null && parts.length > 4);
			

		String hlaska;
		if(parts!=null && parts.length>0) {
			hlasy = new int[parts.length];
			for (int i = 0; i < parts.length; i++) {
				hlasy[i] = Integer.parseInt(parts[i]);
			}
			hlaska = "Dali ste hlas tymto kandidatom: "+cisla_k;			
		} else {
			hlaska = "Nevybrali ste ziadneho kandidata, hlas dostane iba strana.";
		}
			
		System.out.print(hlaska);
		System.out.print("\nZahlasovat? (A\\N)?\n");
		scan= new Scanner(System.in);
		String odpoved="";
		try {
			odpoved = scan.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (odpoved.contains("A") || odpoved.contains("a")) {
			if(v.zapis_hlasovanie(cislo_s, hlasy)) {
				System.out.print("\nDakujem za ucast vo volbach.\n");
			}
		}
		
		System.out.print("\nVas vyber: " + cisla_k);
	}

	public static int h_menu() {
		System.out.print("\nMenu:\n");
		System.out.print("1. Hlasovanie\n");
		System.out.print("2. Vysledky -> Zakladna analyza\n");
		System.out.print("3. Vysledky -> Detailnna analyza\n");
		System.out.print("4. Ulozit do suboru\n");
		System.out.print("5. Nacitat zo suboru\n");
		System.out.print("Zvol funkciu:\n");
		Scanner scan= new Scanner(System.in);
		int volba=0;
		try {
			volba = scan.nextInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return volba;
	}
	
	private static  void clearconsole() {
		for (int i = 0; i < 50; i++) {
		    System.out.println();
		}
	}
	
    public static void savetofile(Volby volbysave) {
	    try {
	        FileOutputStream fileOut = new FileOutputStream("volby.ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(volbysave);
	        out.close();
	        fileOut.close();
	        //System.out.println("Data boli uložen do souboru 'volby.ser'.");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }
    
    public static Volby loadfromFile() {
    	Volby citajVolby = null;
        try {
            FileInputStream fileIn = new FileInputStream("volby.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            citajVolby = (Volby) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return citajVolby;
        /*
        if (citajVolby != null) {
            System.out.println("Načtený objekt: " + citajVolby);
        }
        */
    }
	
	public static void main(String[] args) {
		Volby volby = new Volby();
		
		//naplnenie array obcania
		volby.import_obcan();
		//naplnejie array pstrany
		volby.import_pstrany();
		
		while(true) {
			int hm =  h_menu();
			clearconsole();
			switch(hm) {
			  case 1:
				  if(overenie_obcana(volby)) {
					  if(!volby.zapis_do_volicov()) {
						  System.out.println("Uz ste hlasovali." );
					  } else {
						  hlasovanie(volby);
					  }
				  }
				  savetofile(volby);
				  break;
			  case 2:
				  ZakladnaAnalyzaVolieb analyza = new ZakladnaAnalyzaVolieb();
				  analyza.analyzaVysledok(volby);
				  
				  System.out.println("Pocet odovzdanych hlasov: "+analyza.pocetHlasov());
				  
				  System.out.println("Poradie stran: ");
				  ArrayList<String> poradieStr= analyza.poradieStran();
			      for (String  pstr : poradieStr) {
			    	  System.out.println(pstr);
			      }
			      
			      System.out.println("\nPoradie kandidatov: ");
			      ArrayList<String> poradieK= analyza.poradieKandidatov();
			      for (String  pk : poradieK) {
			    	  System.out.println(pk);
			      }
			    break;
			  case 3:
				  DetailnaAnalyzaVolieb analyzad = new DetailnaAnalyzaVolieb();
				  analyzad.analyzaVysledok(volby);
				  
				  System.out.println("Pocet odovzdanych hlasov: "+analyzad.pocetHlasov());
				  
				  ArrayList<String> poradieStrd= analyzad.poradieStran();
				  
				  if(poradieStrd!=null) {
					  System.out.println("Poradie stran: ");
					  for (String  pstr : poradieStrd) {
						  System.out.println(pstr);
					  }
			      
					  System.out.println("\nZoznam zvolenych poslancov: ");
					  ArrayList<String> poradieKd= analyzad.poradieKandidatov();
					  if(poradieKd!=null) {
						  for (String  pk : poradieKd) {
							  System.out.println(pk);
						  }
					  }
				  } else {
					  System.out.println("Malo hlasov na zozstavenie parlamentu.");
				  }
			    break;
			  case 4:
				  savetofile(volby);
			    break;			  
			  case 5:
				  volby = loadfromFile();
				break;
			  default:
			    // code block
			}
		}
		
	}
}
