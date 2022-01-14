package model;

import javafx.scene.Node;

public class Task {

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getPriorita() {
		return priorita;
	}
	public void setPriorita(int priorita) {
		this.priorita = priorita;
	}
	public Boolean getCompletata() {
		if(completata==1)
			return true;
		return false;
	}
	public void setCompletata(int completata) {
		this.completata = completata;
	}
	public Task(int id, String nome, Sezione sezione) {
		super();
		this.id = id;
		this.nome = nome;
		this.sezione = sezione;
	}
	public Task(int id,String nome, Sezione sezione, String data, int priorita, int completata) {
		super();
		this.id = id;
		this.nome = nome;
		this.sezione = sezione;
		this.data = data;
		this.priorita = priorita;
		this.completata = completata;
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
	private String data;
	private int priorita;
	private int completata;
	
}
