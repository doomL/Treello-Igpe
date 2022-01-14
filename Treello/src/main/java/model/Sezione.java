package model;

public class Sezione {
	
	public int getId() {
		return id;
	}
	public Sezione(int id, String nome, Progetto progetto) {
		super();
		this.id = id;
		this.nome = nome;
		this.progetto = progetto;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Progetto getProgetto() {
		return progetto;
	}
	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}
	private int id;
	private String nome;
	private Progetto progetto;

}
