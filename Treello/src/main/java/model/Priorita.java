package model;

public class Priorita {
	public Priorita(String p, int nPriorita) {
		super();
		this.p = p;
		this.nPriorita = nPriorita;
	}
	private String p;
	private int nPriorita;
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public int getnPriorita() {
		return nPriorita;
	}
	public void setnPriorita(int nPriorita) {
		this.nPriorita = nPriorita;
	}
}
