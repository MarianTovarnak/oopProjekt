package Volby;

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
	
	private static void hlasovanie(Volby v) {
		int cislo_s = -1;
		String cisla_k = null;
		
		System.out.print("\nZoznam politickych stran:\n");
		ArrayList<String> strany = v.zoznam_stran();
		for (String s : strany) {
			System.out.println(s);
		}
		
		System.out.print("Vyberte si cislo politickej strany: ");
		Scanner scan= new Scanner(System.in);
		
		try {
			cislo_s = scan.nextInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.print("\nZoznam kandidatov strany:\n");
		ArrayList<String> kand = v.zoznam_kandidatov_strany(cislo_s);
		for (String k : kand) {
			System.out.println(k);
		}
		
		System.out.print("\nVypiste max. 4 cisla kandidatov oddelenych ciarkou:\n");
		scan= new Scanner(System.in);
		try {
			cisla_k = scan.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.print("\nVas vyber: " + cisla_k);
	}


	public static void main(String[] args) {
		System.out.println("main" );
		Volby volby = new Volby();
		
		//naplnenie array obcania
		volby.import_obcan();
		//naplnejie array pstrany
		volby.import_pstrany();
		
		while(true) {
			if(overenie_obcana(volby)) {
				if(!volby.zapis_do_volicov()) {
					System.out.println("Uz ste hlasovali." );
				} else {
					hlasovanie(volby);
				}
			}			
		}
		
	}
}
