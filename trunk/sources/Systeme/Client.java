package Systeme;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import Systeme.Signal;
import bdd.Cours;
import bdd.Creneau;
import bdd.Enseignant;
import bdd.Groupe;
import bdd.Matiere;
import bdd.Personne;
import bdd.Salle;

import Interfaces.Interface_Connexion;
import Interfaces.Interface_EDT;
import Interfaces.Interface_Reservation;
/**
 * Classe principale du côté serveur qui instancie toutes les interfaces graphiques et communique avec le serveur.
 * @author Alexander Remen et Tonya Vo Thanh
 * <p>Presque toutes les méthodes de cette classe consiste à envoyer un signal au serveur et communiquer la réponse à une interface graphique</p> 
 */
public class Client {

	static final int port = 8080;
	private Socket soc;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int typeUtilisateur;
	
	/**
	 * Constructeur d'un client qui créer une connection socket avec le serveur
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Client() throws UnknownHostException, IOException, ClassNotFoundException
	{
		//Reseau local
		String host = InetAddress.getLocalHost().getHostAddress();
		
		//Reseau internet
		//String host = "adresse du site"
		
		soc = new Socket(host,port);
		//System.out.println("Connection etablie");
		//InputStream tmp = ;
		out = new ObjectOutputStream(soc.getOutputStream());
		in = new ObjectInputStream(soc.getInputStream());

	}
	/**
	 * Méthode qui envoi un signal au serveur et recupère le vecteur de vecteur de cours (les cours d'une semaine) à afficher. 
	 * @param semaine
	 * @return vecteur de vecteur de cours
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public  Vector<Vector<Cours>> recuperercoursdelasemaine(Jours semaine) throws IOException, ClassNotFoundException{
		Signal sig_visu = new Signal("visualiser_EDT");
		sig_visu.addParametre(semaine);
		Signaler(sig_visu);
		return  (Vector<Vector<Cours>>)in.readObject();
	}
	
	/**
	 * Méthode qui envoi un signal au serveur et recupère le vecteur de vecteur de cours d'une promotion
	 * @param promo
	 * @return Vector<Vector>Cours>> vecteur de vecteur de cours
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Vector<Vector<Cours>> recupererEDT(String promo) throws IOException, ClassNotFoundException{
		Signal s;
		if(promo.compareTo("")==0) s = new Signal("recuperer_EDT"); 
		else 
		{
			s = new Signal("recuperer_EDT_Promo");
			s.addParametre(promo);
		}
		Signaler(s);
		return (Vector<Vector<Cours>>)in.readObject();
	}
	
	/**
	 * Méthode qui instancie une interface graphique emploi du temps et l'affiche. 
	 * Elle envoi un signal au serveur pour récuperer la liste d'emails à afficher
	 * dans la fenêtre liste_contacts.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void Afficher_Emploi_du_temps() throws IOException, ClassNotFoundException{
		Interface_EDT Graphic_EDT= new Interface_EDT();
		
		
		Graphic_EDT.Afficher_EDT(this);
		/* initialisation de la fenetre mail */
		Signal s = new Signal("afficher_liste_contacts");
		Signaler(s);
		Vector<Personne> ListePersonne = (Vector<Personne>)in.readObject();
		Graphic_EDT.init_fenetre_mail(ListePersonne,this);
		/* Si c'est un inspecteur */
		if(typeUtilisateur==Personne.RESPONSABLE)
		{
			Interface_Reservation FenetreReservation = new Interface_Reservation();
			FenetreReservation.Affiche_Interface_Reservation(this);
		}
	}
	
	/**
	 * Méthode qui envoi un signal au serveur quel type d'emploi du temps un responsable veut visualiser (salle ou promotion)
	 * @param type
	 */
	public void Choisir_EDT(int type)
	{
		Signal s = new Signal("Choisir_EDT");
		s.addParametre(type);
		try {
			Signaler(s);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	/**
	 * Méthode qui envoi un signal au serveur et recupère le vecteur de personne avec les emails, essentiellement pour l'API de test
	 * @return Vector<Personne> vecteur de personne
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Vector<Personne> Recuperer_Email() throws IOException, ClassNotFoundException
	{
		Signal s = new Signal("Recuperer_Email");
		Signaler(s);
		return ((Vector<Personne>)(in.readObject()));
	}
	
	/**
	 * Méthode qui envoi un signal avec le login et le mot de passe au serveur et retourne un boolean
	 * @param login
	 * @param mdp
	 * @return true si connection ok, false sinon
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Boolean Connexion(String login, String mdp) throws IOException, ClassNotFoundException
	{
		Signal s = new Signal("Connexion");
		
		s.addParametre(login);
		s.addParametre(mdp);
		Signaler(s);

		Boolean ok = ((Boolean)in.readObject());
		if(ok) typeUtilisateur = ((Integer)in.readObject());
		
		return ok;
	}
	/**
	 * Méthode qui envoi un signal au serveur avec le cours à supprimer et renvoi un boolean
	 * @param cours_a_supprimer
	 * @return true si ca c'est bien passé, false sinon
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Boolean Supprimer_cours(Cours cours_a_supprimer) throws IOException, ClassNotFoundException
	{
		Signal s = new Signal("Supprimer_EDT");
		
		s.addParametre(cours_a_supprimer);
		Signaler(s);

		return (Boolean)in.readObject();
	}
	/**
	 * Méthode qui envoi un signal au serveur avec le cours à ajouter (pour l'interface graphique)
	 * @param mat
	 * @param salle
	 * @param cren
	 * @param gp
	 * @param ens
	 * @return true si ca c'est bien passé, false sinon
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Boolean Ajouter_Cours(Matiere mat, Salle salle, Creneau cren, Groupe gp, Enseignant ens) throws IOException, ClassNotFoundException{
		Signal s = new Signal("Saisir_EDT");
		
		s.addParametre(mat);
		s.addParametre(salle);
		s.addParametre(cren);
		s.addParametre(gp);
		s.addParametre(ens);
		Signaler(s);
		
		Boolean ok = ((Boolean)in.readObject());
		if(!ok){Exception e=((Exception)in.readObject());}
		
		return ok;
	}
	/**
	 * Méthode qui envoi un signal au serveur avec le cours à ajouter (pour l'API de test)
	 * @param matiere
	 * @param salle
	 * @param creneau
	 * @param groupe
	 * @return boolean true si ok, false sinon
	 * @throws Exception
	 */
	public Boolean Ajouter_Cours(String matiere, String salle, Creneau creneau, String groupe) throws Exception {
		Signal s = new Signal("Saisir_EDT2");
		
		s.addParametre(matiere);
		s.addParametre(salle);
		s.addParametre(creneau);
		s.addParametre(groupe);
		Signaler(s);
		
		Boolean ok = ((Boolean)in.readObject());
		if(!ok) throw ((Exception)in.readObject());
		return ok;
		
	}
	
	/**
	 * Méthode qui envoi un signal au serveur avec l'email à envoyer
	 * @param email
	 * @param sujet
	 * @param Message
	 * @return boolean true si envoie_email ok, false sinon
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Boolean Envoi_email(String email, String sujet, String Message) throws IOException, ClassNotFoundException {
		Signal s = new Signal("envoi_email");
		
		s.addParametre(email);
		s.addParametre(sujet);
		s.addParametre(Message);
		Signaler(s);
		
		return ((Boolean)in.readObject());
	}
	
	/**
	 * Méthode qui envoi un signal au serveur et récupère les listes pour remplir les JComboBox de l'interface graphique reservation
	 * @return tableau de vecteur (Vector[])
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Vector[] Recup_Listes_Reservation() throws IOException, ClassNotFoundException{
		
		Signal s = new Signal("recuperer_listes");
		Signaler(s);
		return ((Vector[])in.readObject());
	}
	
	/**
	 * Méthode qui envoi le signal construit en paramètre
	 * @param s
	 * @throws IOException
	 */
	public void Signaler(Signal s) throws IOException
	{
		out.writeObject(s);
		//System.out.println("envoi d'un signal "+s.getNom());
	}
	
	/**
	 * Méthode qui ferme la connection socket au serveur
	 *
	 */
	public void FermerConnexion()
	{
		try {
			Signaler(new Signal("close"));
			out.close();
			in.close();
			soc.close();
		} catch (IOException e) {
			//System.out.println("Erreur fermeture socket");
		}
	}
	
	/**
	 * Le main coté client. Instancie un client et une interface connection.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Client c = new Client();
			//System.out.println("Client cree");
			/* Un signal de test pour verifier que la connection marche bien */
			//Signal s = new Signal("Test");
			//c.Signaler(s);

			Interface_Connexion Login = new Interface_Connexion();
			Login.affiche_login_screen(c);
			
			//c.FermerConnexion();
			//System.out.println("Fermeture client");
			
		} catch (UnknownHostException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}
		
	}

	

}
