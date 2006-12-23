package bdd;

public class Responsable extends Enseignant {

	private static int nbPers =0;
	private Promotion promo;
	
	/**
	 * @param num_personne
	 * @param username
	 * @param password
	 * @param nom
	 * @param prenom
	 * @param adresse
	 * @param email
	 * @param tel
	 * @param promo
	 */
	public Responsable(String num_personne, String username, String password, String nom, String prenom, String adresse, String email, int tel, Promotion promo) {
		super(num_personne, username, password, nom, prenom, adresse, email, tel);
		this.promo = promo;
	}

	
	
	
	

}
