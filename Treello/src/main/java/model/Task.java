package model;

public class Task {

	public Task(int id, String nome, Sezione sezione) {
		super();
		this.id = id;
		this.nome = nome;
		this.sezione = sezione;
	}
	public int getId() {
		return id;
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
	public Sezione getSezione() {
		return sezione;
	}
	public void setSezione(Sezione sezione) {
		this.sezione = sezione;
	}
	private int id;
	private String nome;
	private Sezione sezione;
}
