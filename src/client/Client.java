package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import serveur.Objet;
import serveur.Vente;

public class Client extends UnicastRemoteObject implements Acheteur {

	private static final long serialVersionUID = 1L;
	private static final String adresseServeur = "localhost:8090/enchere";

	private String pseudo;
	private VueClient vue;
	private Vente serveur;
	private Objet currentObjet;
	private EtatClient etat = EtatClient.ATTENTE;
	private Chrono chrono = new Chrono(30000, this); // Chrono de 30sc

	public Client(String pseudo) throws RemoteException {
		super();
		this.chrono.start();
		this.pseudo = pseudo;
		this.serveur = connexionServeur();
		this.currentObjet = serveur.getObjet();
	}

	public static Vente connexionServeur() {
		try {
			Vente serveur = (Vente) Naming.lookup("//" + adresseServeur);
			System.out.println("Connexion au serveur " + adresseServeur + " reussi.");
			return serveur;
		} catch (Exception e) {
			System.out.println("Connexion au serveur " + adresseServeur + " impossible.");
			e.printStackTrace();
			return null;
		}
	}

	public void inscription() throws Exception {
		serveur.inscriptionAcheteur(pseudo, this);
		//vue.changerGUI(this.vue.getAttentePanel());
	}

	public void encherir(int prix) throws RemoteException, Exception {		
		if (prix <= this.currentObjet.getPrixCourant() && prix != -1) {
			System.out.println("Prix trop bas, ne soyez pas radin !");
		} else if (etat == EtatClient.RENCHERI) {
			System.out.println("Vous rencherissez de " + prix + " euros.");
			chrono.arreter();
			etat = EtatClient.ATTENTE;
			serveur.rencherir(prix, this);
		}
	}

	@Override
	public void objetVendu(String gagnant) throws RemoteException {
		this.currentObjet = serveur.getObjet();
		this.vue.actualiserObjet();
		
		if (gagnant != null) {
			this.vue.getLblEncherir().setText(gagnant + "a remporte l'enchere.");
			this.etat = EtatClient.TERMINE;
		}else{
			this.etat = EtatClient.RENCHERI;
			this.chrono.demarrer();
		}
		//this.vue.changerGUI(this.vue.getMainPanel());
	}

	@Override
	public void nouveauPrix(int prix, Acheteur gagnant) throws RemoteException {
		System.out.println("etat de "+this.pseudo + " : "+this.etat);
		
		try {
			System.out.println(gagnant.getPseudo() + " a remporté la manche. Nouveau prix de  l'enchère : " + prix + " euros.");
			this.currentObjet.setPrixCourant(prix);
			this.vue.actualiserPrix();
			this.etat = EtatClient.RENCHERI;
			this.chrono.demarrer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void nouvelleSoumission(String nom, String description, int prix) {
		Objet nouveau = new Objet(nom, description, prix);
		try {
			serveur.ajouterObjet(nouveau);
			System.out.println("Soumission de l'objet " + nom + " au serveur.");
		} catch (RemoteException e) {
			System.out.println("Impossible de soumettre un nouvel objet.");
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		try {
			Client c = new Client("Toto");
			c.inscription();
			System.out.println("En attente d'un deuxième client...");
			while(c.getEtat() == EtatClient.ATTENTE) {System.out.print("");} // Attente de l'appel de objetVendu() du serveur
			c.encherir(200);
			while(c.getEtat() == EtatClient.ATTENTE) {System.out.print("");} // Attente de l'appel de nouveauPrix() du serveur
			c.encherir(600);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// getters and setters
	public Objet getCurrentObjet() {
		return currentObjet;
	}

	@Override
	public long getChrono() {
		return chrono.getTemps();
	}

	public Vente getServeur() {
		return serveur;
	}

	public void setServeur(Vente serveur) {
		this.serveur = serveur;
	}

	public void setVue(VueClient vueClient) {
		vue = vueClient;
	}

	public EtatClient getEtat() {
		return this.etat;
	}
	
	@Override
	public String getPseudo() throws RemoteException {
		return pseudo;
	}
}
