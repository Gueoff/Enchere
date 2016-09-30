package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import serveur.Objet;
import serveur.Vente;


public class Client extends UnicastRemoteObject  implements Acheteur {

	private static final long serialVersionUID = 1L;
	private static final String adresseServeur = "172.16.134.156:8090/enchere";
	private Vente serveur;
	private String pseudo;
	private EtatClient etat;
	private Chrono chrono;
	
	public Client(String pseudo, Vente serveur) throws Exception {
		super();
		this.pseudo = pseudo;
		this.serveur = serveur;
		etat = EtatClient.ATTENTE;
		chrono = new Chrono(60000); // Chrono d'1min
		
		// Inscription
		serveur.inscriptionAcheteur(pseudo, this);
		System.out.println(pseudo + " est ajouté à la liste d'acheteurs.");
	}
	
	public Client(String pseudo) throws Exception {
		super();
		this.pseudo = pseudo;
		this.serveur = connexionServeur();
		etat = EtatClient.ATTENTE;
		chrono = new Chrono(60000); // Chrono d'1min
		
		// Inscription
		serveur.inscriptionAcheteur(pseudo, this);
		System.out.println(pseudo + " est ajouté à la liste d'acheteurs.");
	}

	public static Vente connexionServeur() {
		try {

			Vente serveur = (Vente) Naming.lookup("//" + adresseServeur);
			System.out.println("Connexion au serveur " + adresseServeur + " réussi. ");
			return serveur;

		} catch (Exception e) {
			System.out.println("Connexion au serveur " + adresseServeur + " impossible.");
			return null;
		}
	}

	public Vente getServeur() {
		return serveur;
	}

	public void setServeur(Vente serveur) {
		this.serveur = serveur;
	}
	
	@Override
	public String getPseudo() {
		return pseudo;
	}
	
	@Override
	public void nouvelleSoumission(String nom, String description, int prix) throws RemoteException {
		//serveur.addObjet(new Objet(nom, description, prix));
	}

	@Override
	public void objetVendu() throws RemoteException{
		etat = EtatClient.TERMINE;
		chrono.start();
	}

	@Override
	public void nouveauPrix(int prix) throws RemoteException, Exception {
		if(!chrono.getFini() & etat != EtatClient.ATTENTE) {
			serveur.rencherir(prix, this);
			etat = EtatClient.RENCHERI;
		}
	}
	
	public static void main(String[] argv) throws Exception{
		try {
			Client c = new Client("toto");
			c.objetVendu();
			int cpt = 0;
			while(true) {
				System.out.println( cpt + " " + c.chrono.getFini());
				Thread.sleep(1000);
				cpt++;
			}
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
