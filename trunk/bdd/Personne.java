package bdd;

import java.io.Serializable;

public abstract class Personne implements Serializable{

	/*Declaration des Attributs*/
	protected String num_personne;
	protected String username;
	protected String password;
	protected String nom;
	protected String prenom;
	protected String adresse;
	protected String email;
	protected int tel;
	
	
	/**
	 * @param username
	 * @param password
	 * @param nom
	 * @param prenom
	 * @param adresse
	 * @param email
	 * @param tel
	 */
	public Personne(String num_personne,String username, String password, String nom, String prenom, String adresse, String email, int tel) {
		this.num_personne=num_personne;
		this.username = username;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.email = email;
		this.tel = tel;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public int getTel() {
		return tel;
	}
	public void setTel(int tel) {
		this.tel = tel;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the num_personne
	 */
	public String getNum_personne() {
		return num_personne;
	}
	

	public String toString() {
		String text = "Personne "+num_personne+" : "+this.nom+" , "+this.prenom+"\nLogin : "+this.username+" mdp : "+this.password;
		return text;
	}

	
		
}
