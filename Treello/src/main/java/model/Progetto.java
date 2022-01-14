package model;

import java.util.List;

public class Progetto {
	
	@Override
	public String toString() {
		return "Progetto [id=" + id + ", nome=" + nome + ", utente=" + utente + "]";
	}
	public Progetto(int id, String nome, Utente utente) {
		super();
		this.id = id;
		this.nome = nome;
		this.utente = utente;
	}
	public Progetto() {
		// TODO Auto-generated constructor stub
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
	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	public List<Utente> getUtenti() {
		return utenti;
	}
	public void setUtenti(List<Utente> utenti) {
		this.utenti = utenti;
	}
	public int getCompletato() {
		return completato;
	}
	public void setCompletato(int completato) {
		this.completato = completato;
	}
	public String getSfondo() {
		return sfondo;
	}
	public void setSfondo(String sfondo) {
		this.sfondo = sfondo;
	}
	private int id;
	private String nome;
	private Utente utente;
	private List<Utente> utenti;
	private int completato;
	private String sfondo;
	
	public Progetto(int id, String nome, Utente utente, int completato, String sfondo) {
		super();
		this.id = id;
		this.nome = nome;
		this.utente = utente;
		this.completato = completato;
		this.sfondo = sfondo;
	}

}
