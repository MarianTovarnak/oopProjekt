package Volby;

import java.io.Serializable;

public class Volic extends Obcan implements Serializable {
	private Boolean zahlasoval;
	
	public Volic(int id) {
		super(id);
	}
	public Volic(int id, String m, String p) {
		super(id, m, p);
		this.zahlasoval = false;
	}
	
	public int getIdVolic() {
		return super.getIdObcan();
	}
	
	public Boolean getHlasoval() {
		return this.zahlasoval;
	}
	
	public void zahlasoval() {
		this.zahlasoval = true;
	}
	
    @Override
    public String toString() {
        return "Volic{" +
                "zahlasoval=" + zahlasoval +
                '}';
    }

}
